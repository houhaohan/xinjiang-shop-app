package com.pinet.rest.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.DB;
import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.UpdateCouponStatusDto;
import com.pinet.rest.entity.enums.*;
import com.pinet.rest.entity.request.UsableCouponRequest;
import com.pinet.rest.entity.vo.CustomerCouponVo;
import com.pinet.rest.mapper.CustomerCouponMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
@RequiredArgsConstructor
public class CustomerCouponServiceImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements ICustomerCouponService {
    private final RedisUtil redisUtil;
    private final JmsUtil jmsUtil;
    private final ICouponService couponService;
    private final ICouponShopService couponShopService;
    private final ICouponProductService couponProductService;

    @Override
    public List<CustomerCouponVo> customerCouponList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();

        Page<CustomerCoupon> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        QueryWrapper<CustomerCoupon> queryWrapper = initWrapper(userId, true);
        queryWrapper.in("cc.coupon_status", 1, 2)
                .orderByAsc("cc.coupon_status")
                .orderByDesc("cc.id");
        return baseMapper.selectCustomerCouponList(page, queryWrapper);
    }

    @Override
    public List<CustomerCouponVo> usableCouponList(UsableCouponRequest request) {
        List<CustomerCouponVo> list = customerCouponList(request);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        list.forEach(item -> {
            Boolean usable = this.checkCoupon(item, request.getShopId(), request.getOrderProducts());
            item.setIsUsable(usable);
        });
        return list;
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
        Page<CustomerCoupon> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        QueryWrapper<CustomerCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cc.customer_id", userId);
        queryWrapper.eq("cc.del_flag", 0);
        queryWrapper.orderByDesc("cc.id");
        return baseMapper.selectCustomerCouponList(page, queryWrapper);
    }

    @Override
    public List<CustomerCouponVo> customerCouponInvalidList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<CustomerCoupon> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        QueryWrapper queryWrapper = initWrapper(userId, false);
        queryWrapper.orderByDesc("cc.id");
        return baseMapper.selectCustomerCouponList(page, queryWrapper);
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
        Page<CustomerCoupon> page = new Page<>(1, 20);
        QueryWrapper queryWrapper = initWrapper(userId, true);
        queryWrapper.gt("cc.id", lastId);
        queryWrapper.orderByDesc("cc.id");
        List<CustomerCouponVo> customerCoupons = baseMapper.selectCustomerCouponList(page, queryWrapper);

        customerCoupons = customerCoupons.stream().filter(coupon -> coupon != null && coupon.getDisableFlag() == 1).collect(Collectors.toList());


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
            List<Long> productIds = couponProductService.getProdIdsByShopProdIdsAndCouponId(shopProdIds, coupon.getId());
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
        if (Objects.isNull(customerCoupon)) {
            return;
        }
        Date remindTime = DateUtil.offsetDay(customerCoupon.getExpireTime(), -1);
        if (remindTime.getTime() - System.currentTimeMillis() <= 0) {
            return;
        }
        jmsUtil.delaySend(QueueConstants.QING_COUPON_EXPIRE_WARN_NAME, customerCouponId.toString(), remindTime.getTime() - System.currentTimeMillis());

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
        queryWrapper.lambda()
                .eq(CustomerCoupon::getCustomerId, customerId)
                .in(CustomerCoupon::getCouponStatus, 1, 2)
                .gt(CustomerCoupon::getExpireTime, new Date());
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
        coupon.setClaimedNum(coupon.getClaimedNum() + 1);
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
        if (Objects.equals(CouponStatusEnum.NOT_STARTED.getCode(), coupon.getStatus())) {
            throw new PinetException(ApiExceptionEnum.COUPON_NOT_STARTED);
        }
        if (Objects.equals(CouponStatusEnum.EXPIRED.getCode(), coupon.getStatus())) {
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
        if (Objects.equals(coupon.getRecType(), CouponClaimedTypeEnum.USER_LIMIT.getCode())) {
            long count = count(queryWrapper);
            if (count >= coupon.getRestrictNum()) {
                throw new PinetException(ApiExceptionEnum.COUPON_RECEIVE_UPPER_LIMIT);
            }
        } else if (Objects.equals(coupon.getRecType(), CouponClaimedTypeEnum.TIME_LIMIT.getCode())) {
            Date firstCouponReceiveTime = getFirstCouponReceiveTime(userId, coupon.getId());
            queryWrapper.le("create_time", DateUtils.endOfDay(DateUtils.addDays(firstCouponReceiveTime, coupon.getRecCycle())));
            long count = count(queryWrapper);
            if (count >= coupon.getRestrictNum()) {
                throw new PinetException(ApiExceptionEnum.COUPON_RECEIVE_UPPER_LIMIT);
            }
        }
    }

}
