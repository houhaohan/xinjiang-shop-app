package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerCoupon;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.UpdateCouponStatusDto;
import com.pinet.rest.mapper.CustomerCouponMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.ICustomerCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.service.IShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

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
public class CustomerCouponServiceImpl extends ServiceImpl<CustomerCouponMapper, CustomerCoupon> implements ICustomerCouponService {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JmsUtil jmsUtil;

    @Resource
    private WxMaService wxMaService;

    @Resource
    private IShopService shopService;

    @Resource
    private ICustomerService customerService;

    @Override
    public List<CustomerCoupon> customerCouponList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();

        IPage<CustomerCoupon> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        IPage<CustomerCoupon> pageList = baseMapper.selectCustomerCouponList(page, userId);
        pageList.getRecords().forEach(this::setRule);
        return pageList.getRecords();
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
    public List<CustomerCoupon> customerCouponListDetailList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        IPage<CustomerCoupon> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        IPage<CustomerCoupon> pageList = baseMapper.selectCustomerCouponDetailList(page, userId);
        pageList.getRecords().forEach(this::setRule);
        return pageList.getRecords();
    }

    @Override
    public List<CustomerCoupon> customerCouponInvalidList(PageRequest pageRequest) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        IPage<CustomerCoupon> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        IPage<CustomerCoupon> pageList = baseMapper.selectcustomerCouponInvalidList(page, userId);
        pageList.getRecords().forEach(this::setRule);
        return pageList.getRecords();
    }

    @Override
    public List<CustomerCoupon> indexCouponList() {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        String redisKey = "qingshi:coupon:index:" + userId;
        String lastIdStr = redisUtil.get(redisKey);

        long lastId = 0L;

        if (!StringUtil.isBlank(lastIdStr)) {
            lastId = Long.parseLong(lastIdStr);
        }

        List<CustomerCoupon> customerCoupons = baseMapper.selectIndexCouponList(lastId, userId);

        if (CollUtil.isNotEmpty(customerCoupons)) {
            redisUtil.set(redisKey, customerCoupons.get(customerCoupons.size() - 1).getId().toString());
        }
        customerCoupons.forEach(this::setRule);
        return customerCoupons;
    }

    @Override
    public Boolean checkCoupon(Long customerCouponId, Long shopId, BigDecimal orderProdPrice) {
        CustomerCoupon customerCoupon = getById(customerCouponId);
        return checkCoupon(customerCoupon, shopId, orderProdPrice);
    }

    @Override
    public Boolean checkCoupon(CustomerCoupon customerCoupon, Long shopId, BigDecimal orderProdPrice) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        //优惠券不存在
        if (ObjectUtil.isNull(customerCoupon)) {
            return false;
        }

        //优惠券和使用人不符
        if (!customerId.equals(customerCoupon.getCustomerId())) {
            return false;
        }

        //校验店铺
        if (customerCoupon.getShopId() != 0 && !shopId.equals(customerCoupon.getShopId())) {
            return false;
        }

        //校验使用门槛
        if (customerCoupon.getThresholdAmount().compareTo(orderProdPrice) >= 0) {
            return false;
        }
        return true;
    }

    @Override
    public void couponWarn(Long customerCouponId) {
        CustomerCoupon customerCoupon = getById(customerCouponId);
        if (ObjectUtil.isNotNull(customerCoupon)) {
            jmsUtil.sendMsgQueue(QueueConstants.QING_COUPON_EXPIRE_WARN_NAME, customerCouponId.toString());
        }
    }

    @Override
    public void pushCouponExpireMsg(String data1, String data2, String data3, String data4, String data5, String openId) {
        try {

            wxMaService.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder()
                    .templateId("e13-yQdGgep28jJ7GJiRNFYwulIso5TNM4UOsfPE7jI")
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
        CustomerCoupon customerCoupon = getById(customerCouponId);
        String data1 = "你的优惠券将在1天后过期,请及时使用";
        String data2 = LocalDateTimeUtil.format(customerCoupon.getExpireTime(), "yyyy-MM-dd HH:mm:ss");
        String data3 = customerCoupon.getCouponAmount().toString();
        String data4 = "你的优惠券即将过期";
        String data5 = "所有门店可用";
        if (customerCoupon.getShopId() != null && customerCoupon.getShopId() > 0) {
            Shop shop = shopService.getById(customerCoupon.getShopId());
            data5 = shop.getShopName();
        }
        Customer customer = customerService.getById(customerCoupon.getCustomerId());

        pushCouponExpireMsg(data1, data2, data3, data4, data5, customer.getQsOpenId());
    }


    private void setRule(CustomerCoupon customerCoupon) {
        String msg2 = "2、本券一次使用一张,不限制商品,不可抵扣配送费及零星选配的辅料等附加费用";
        String msg3 = "3、本券不于其他优惠同享。(店帮主可与本券同使用)";


        StringBuilder msg = new StringBuilder();
        if (customerCoupon.getShopId() != null && customerCoupon.getShopId() > 0) {
            Shop shop = shopService.getById(customerCoupon.getShopId());
            msg.append("1、本券可用于").append(shop.getShopName()).append("使用,享受门店所有优惠。").append("\r\n").append(msg2)
                    .append("\r\n").append(msg3);
        } else {
            msg.append("1、本券全国门店通用,(部分特殊活动门店除外),下单前可与客服确认门店是否支持使用。").append("\r\n").append(msg2)
                    .append("\r\n").append(msg3);
        }

        customerCoupon.setRule(msg.toString());
    }

}
