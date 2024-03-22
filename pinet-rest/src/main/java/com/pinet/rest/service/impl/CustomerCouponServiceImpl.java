package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.entity.CouponProduct;
import com.pinet.rest.entity.CustomerCoupon;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.dto.UpdateCouponStatusDto;
import com.pinet.rest.entity.enums.*;
import com.pinet.rest.entity.vo.CustomerCouponVo;
import com.pinet.rest.entity.vo.OrderProductVo;
import com.pinet.rest.mapper.CustomerCouponMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户优惠券 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
@Service
@Slf4j
@DS(DB.MASTER)
public class CustomerCouponServiceImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements ICustomerCouponService {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JmsUtil jmsUtil;

    @Resource
    private WxMaService wxMaService;

    @Resource
    private ICouponService couponService;

    @Resource
    private ICouponShopService couponShopService;

    @Autowired
    private ICouponProductService couponProductService;


    @Override
    public List<CustomerCouponVo> customerCouponList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();

        QueryWrapper<CustomerCoupon> queryWrapper = initWrapper(userId, true);
        queryWrapper.in("cc.coupon_status", 1,2)
                .orderByAsc("cc.coupon_status")
                .orderByDesc("cc.id")
                .last("limit " + ((pageRequest.getPageNum() - 1) * pageRequest.getPageSize()) + "," + pageRequest.getPageSize());
        return baseMapper.selectCustomerCouponList(queryWrapper);
    }

    @Override
    public boolean updateCouponStatus(UpdateCouponStatusDto dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        CustomerCoupon customerCoupon = getById(dto.getCustomerCouponId());
        if (ObjectUtil.isNull(customerCoupon) || !userId.equals(customerCoupon.getCustomerId())) {
            throw new PinetException("优惠券不存在");
        }

        if (customerCoupon.getCouponStatus() != 1) {
            throw new PinetException("只有未领取状态可更改");
        }
        customerCoupon.setCouponStatus(dto.getCouponStatus());
        return updateById(customerCoupon);
    }

    @Override
    public List<CustomerCouponVo> customerCouponListDetailList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        QueryWrapper<CustomerCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cc.customer_id", userId);
        queryWrapper.eq("cc.del_flag", 0);
        queryWrapper.orderByDesc("cc.id");
        queryWrapper.last("limit " + ((pageRequest.getPageNum() - 1) * pageRequest.getPageSize()) + "," + pageRequest.getPageSize());
        return baseMapper.selectCustomerCouponList(queryWrapper);
    }

    @Override
    public List<CustomerCouponVo> customerCouponInvalidList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        QueryWrapper queryWrapper = initWrapper(userId, false);
        queryWrapper.orderByDesc("cc.id");
        queryWrapper.last("limit " + ((pageRequest.getPageNum() - 1) * pageRequest.getPageSize()) + "," + pageRequest.getPageSize());
        return baseMapper.selectCustomerCouponList(queryWrapper);
    }

    @Override
    public List<CustomerCouponVo> indexCouponList() {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        String redisKey = "qingshi:coupon:index:" + userId;
        String lastIdStr = redisUtil.get(redisKey);

        long lastId = 0L;

        if (!StringUtil.isBlank(lastIdStr)) {
            lastId = Long.parseLong(lastIdStr);
        }
        QueryWrapper queryWrapper = initWrapper(userId, true);
        queryWrapper.gt("cc.id", lastId);
        queryWrapper.orderByDesc("cc.id");
        List<CustomerCouponVo> customerCoupons = baseMapper.selectCustomerCouponList(queryWrapper);

        if (CollUtil.isNotEmpty(customerCoupons)) {
            redisUtil.set(redisKey, customerCoupons.get(0).getId().toString());
        }
        return customerCoupons;
    }

    @Override
    public Boolean checkCoupon(Long customerCouponId, Long shopId, List<OrderProduct> orderProducts) {
        CustomerCouponVo customerCouponVo = baseMapper.selectCustomerCouponVoById(customerCouponId);
        return checkCoupon(customerCouponVo, shopId, orderProducts);
    }

    @Override
    public Boolean checkCoupon(CustomerCouponVo customerCoupon, Long shopId, List<OrderProduct> orderProducts) {
        BigDecimal orderProdPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        //优惠券不存在
        if (ObjectUtil.isNull(customerCoupon)) {
            return false;
        }

        //优惠券和使用人不符
        if (!customerId.equals(customerCoupon.getCustomerId())) {
            return false;
        }

        Coupon coupon = couponService.getById(customerCoupon.getCouponId());
        //校验优惠券是否有效
        try {
            validateCouponEffective(coupon);
        } catch (PinetException e) {
            return false;
        }


        //校验店铺
        if (coupon.getUseShop().equals(CouponShopEnum.SOME_SHOP.getCode())) {
            boolean exists = couponShopService.isExistsInShop(customerCoupon.getCouponId(), shopId);
            if (!exists) {
                return false;
            }
        }

        //校验商品 //校验使用门槛
        if (coupon.getUseProduct() == 1) {
            if (coupon.getUsePrice().compareTo(orderProdPrice) >= 0) {
                return false;
            }
        } else {
            List<Long> shopProdIds = orderProducts.stream().map(OrderProduct::getShopProdId).collect(Collectors.toList());
            QueryWrapper<CouponProduct> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("product_id");
            queryWrapper.eq("coupon_id", coupon.getId());
            queryWrapper.in("product_id", shopProdIds);
            List<Long> productIds = couponProductService.listObjs(queryWrapper, productId -> Long.valueOf(productId.toString()));
            BigDecimal sumPrice = orderProducts.stream().filter(item -> productIds.contains(item.getShopProdId())).map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (BigDecimalUtil.lt(sumPrice, coupon.getUsePrice())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void couponWarn(Long customerCouponId) {
        CustomerCoupon customerCoupon = getById(customerCouponId);
        if (ObjectUtil.isNotNull(customerCoupon)) {
            Date remindTime = DateUtil.offsetDay(customerCoupon.getExpireTime(), -1);
            if (remindTime.getTime() - System.currentTimeMillis() > 0) {
                jmsUtil.delaySend(QueueConstants.QING_COUPON_EXPIRE_WARN_NAME, customerCouponId.toString(), remindTime.getTime() - System.currentTimeMillis());
            }
        }
    }

    @Override
    public void pushCouponExpireMsg(String data1, String data2, String data3, String data4, String data5, String openId) {
        try {

            wxMaService.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder()
                    .templateId(CommonConstant.COUPON_EXPIRE_TEMPLATE_ID)
                    .data(Lists.newArrayList(
                            //描述
                            new WxMaSubscribeMessage.MsgData("thing3", data1),
                            //过期时间
                            new WxMaSubscribeMessage.MsgData("time4", data2),
                            //优惠券金额
                            new WxMaSubscribeMessage.MsgData("amount5", data3),
                            //温馨提示
                            new WxMaSubscribeMessage.MsgData("thing6", data4),
                            //商家名称
                            new WxMaSubscribeMessage.MsgData("thing9", data5)
                    ))
                    .toUser(openId)
                    .page("/pickCodePackage/list/Coupons")
                    .build());
        } catch (Exception e) {
            log.error("优惠券过期提醒发送失败,error{}", e);
        }

    }

    @Override
    public void pushCouponExpireMsg(Long customerCouponId) {
//        CustomerCoupon customerCoupon = getById(customerCouponId);
//        String data1 = "你的优惠券将在1天后过期,请及时使用";
//        String data2 = DateUtil.format(customerCoupon.getExpireTime(), "yyyy-MM-dd HH:mm:ss");
//        String data3 = customerCoupon.getCouponAmount().toString();
//        String data4 = "你的优惠券即将过期";
//        String data5 = "所有门店可用";
//        if (customerCoupon.getShopId() != null && customerCoupon.getShopId() > 0) {
//            Shop shop = shopService.getById(customerCoupon.getShopId());
//            data5 = shop.getShopName();
//        }
//        Customer customer = customerService.getById(customerCoupon.getCustomerId());
//
//        pushCouponExpireMsg(data1, data2, data3, data4, data5, customer.getQsOpenId());
    }

    @Override
    public void grantNewCustomerCoupon(Long customerId) {
        String redisKey = "qingshi:coupon:newCustomer2:" + 0L;
        String couponIdStr = redisUtil.get(redisKey);
        if (StringUtil.isNotBlank(couponIdStr)) {
            Long couponId = Long.valueOf(couponIdStr);
            receive(customerId, couponId);
        }
    }

    @Override
    public Long countByCustomerId(Long customerId) {
        QueryWrapper<CustomerCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.in("coupon_status", 1, 2);
        queryWrapper.gt("expire_time", new Date());
        return count(queryWrapper);
    }

    @Override
    public CustomerCoupon receive(Long couponId) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        return receive(customerId, couponId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerCoupon receive(Long customerId, Long couponId) {
        Coupon coupon = couponService.getById(couponId);
        //1、用户是否还能领取
        validateIsCanBeClaimed(coupon, customerId);

        //2、添加到用户优惠券表
        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setCustomerId(customerId);
        customerCoupon.setCouponId(couponId);
        if (coupon.getEffectType() == 1) {
            customerCoupon.setExpireTime(DateUtils.endOfDay(DateUtils.addDays(new Date(), coupon.getEffectDay())));
        } else if (coupon.getEffectType() == 2) {
            customerCoupon.setExpireTime(coupon.getPastTime());
        }
        customerCoupon.setCouponGrantId(0L);
        customerCoupon.setCouponName(coupon.getName());
        customerCoupon.setCouponType(coupon.getType());
        customerCoupon.setCouponStatus(CouponReceiveStatusEnum.RECEIVED.getCode());
        save(customerCoupon);

        //3、更新优惠券领取数量
        coupon.setClaimedNum(coupon.getClaimedNum()+1);
        couponService.updateById(coupon);
        return customerCoupon;
    }

    /**
     * 初始化优惠券查询条件
     *
     * @param userId
     * @param flag   失效时间 > 当前时间？
     * @return
     */
    private QueryWrapper initWrapper(Long userId, boolean flag) {
        QueryWrapper<CustomerCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cc.customer_id", userId);
        if (flag) {
            queryWrapper.gt("cc.expire_time", new Date());
        } else {
            queryWrapper.lt("cc.expire_time", new Date());
        }
        queryWrapper.eq("cc.del_flag", 0);
        return queryWrapper;
    }

    /**
     * 获取这个优惠券第一张的领取时间
     *
     * @param userId
     * @param couponId
     * @return
     */
    private Date getFirstCouponReceiveTime(Long userId, Long couponId) {
        Date time = baseMapper.getFirstCouponReceiveTime(userId, couponId);
        return time == null ? new Date() : time;
    }


    /**
     * 校验优惠券是否有效
     *
     * @param coupon
     */
    private void validateCouponEffective(Coupon coupon) {
        if (coupon == null || coupon.getDelFlag() == 1) {
            throw new PinetException(ApiExceptionEnum.COUPON_EXPIRED);
        }
        if (coupon.getDisableFlag() == 0) {
            throw new PinetException(ApiExceptionEnum.COUPON_EXPIRED);
        }
        if (CouponStatusEnum.NOT_STARTED.getCode() == coupon.getStatus()) {
            throw new PinetException(ApiExceptionEnum.COUPON_NOT_STARTED);
        }
        if (CouponStatusEnum.EXPIRED.getCode() == coupon.getStatus()) {
            throw new PinetException(ApiExceptionEnum.COUPON_EXPIRED);
        }
        if (coupon.getUseTime() != null && DateUtil.compare(new Date(), coupon.getUseTime()) < 0) {
            throw new PinetException(ApiExceptionEnum.COUPON_NOT_STARTED);
        }
        if (coupon.getPastTime() != null && DateUtil.compare(new Date(), coupon.getPastTime()) > 0) {
            throw new PinetException(ApiExceptionEnum.COUPON_EXPIRED);
        }
    }


    /**
     * 校验优惠券是否可继续领取
     *
     * @param coupon
     * @param userId
     */
    private void validateIsCanBeClaimed(Coupon coupon, Long userId) {
        //优惠券是否有效
        validateCouponEffective(coupon);
        //校验优惠券是否已领取完
        if (coupon.getClaimedNum() - coupon.getQuantity() == 0) {
            throw new PinetException(ApiExceptionEnum.COUPON_NO_QUANTITY);
        }
        QueryWrapper<CustomerCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", userId);
        queryWrapper.eq("coupon_id", coupon.getId());
        if (coupon.getRecType() == CouponClaimedTypeEnum.USER_LIMIT.getCode()) {
            long count = count(queryWrapper);
            if (count >= coupon.getRestrictNum()) {
                throw new PinetException(ApiExceptionEnum.COUPON_RECEIVE_UPPER_LIMIT);
            }
        } else if (coupon.getRecType() == CouponClaimedTypeEnum.TIME_LIMIT.getCode()) {
            Date firstCouponReceiveTime = getFirstCouponReceiveTime(userId, coupon.getId());
            queryWrapper.le("create_time", DateUtils.endOfDay(DateUtils.addDays(firstCouponReceiveTime, coupon.getRecCycle())));
            long count = count(queryWrapper);
            if (count >= coupon.getRestrictNum()) {
                throw new PinetException(ApiExceptionEnum.COUPON_RECEIVE_UPPER_LIMIT);
            }
        }
    }

}
