package com.pinet.rest.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.ConfigConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.CouponReceiveStatusEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 月月霸王餐
 * @author chengshuanghui
 */
@Component
@RequiredArgsConstructor
public class VipActivityListener {
    private final IVipUserService vipUserService;
    private final ICouponGrantRecordService couponGrantRecordService;
    private final ICustomerCouponService customerCouponService;
    private final ICouponService couponService;
    private final ISysConfigService sysConfigService;
    private final OrdersMapper ordersMapper;


    @JmsListener(destination = QueueConstants.VIP_ACTIVITY, containerFactory = "queueListener")
    @DSTransactional
    public void send(String message) {
        JSONObject messageObj = JSON.parseObject(message);
        Long customerId = messageObj.getLong("customerId");
        Long shopId = messageObj.getLong("shopId");
        Integer level = vipUserService.getLevelByCustomerId(customerId);
        String giftBagKey = VipLevelEnum.getEnumByCode(level).getGiftBagKey();
        SysConfig sysConfig = sysConfigService.getByCode(giftBagKey);
        if(sysConfig == null || StringUtil.isBlank(sysConfig.getVal())){
            return;
        }
        String[] couponIds = sysConfig.getVal().split(",");
        List<Coupon> coupons = couponService.listByIds(Arrays.asList(couponIds));
        List<Coupon> effectiveCoupons = filterEffectiveCoupons(level, shopId, customerId, coupons);
        if(CollectionUtils.isEmpty(effectiveCoupons)){
            return;
        }
        for(Coupon coupon : effectiveCoupons){
            this.send(coupon,customerId,shopId);
        }
    }

    /**
     * 发送优惠券
     * @param coupon
     * @param customerId
     */
    private void send(Coupon coupon,Long customerId,Long shopId){
        QueryWrapper<CustomerCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id",customerId);
        queryWrapper.eq("coupon_id",coupon.getId());
        queryWrapper.last("limit 1");
        CustomerCoupon customerCoupon = customerCouponService.getOne(queryWrapper);
        //已经发过的就不要发了
        if(customerCoupon != null){
            return;
        }

        CouponGrantRecord couponGrantRecord = new CouponGrantRecord();
        couponGrantRecord.setCouponGrantId(0L);
        couponGrantRecord.setSubject("VIP");
        couponGrantRecord.setNum(1);
        couponGrantRecord.setRemark("VIP升级礼包");
        couponGrantRecord.setGrantTime(LocalDateTime.now());
        couponGrantRecord.setShopId(shopId);
        couponGrantRecordService.save(couponGrantRecord);

        customerCoupon = new CustomerCoupon();
        customerCoupon.setCustomerId(customerId);
        customerCoupon.setCouponStatus(CouponReceiveStatusEnum.NOT_RECEIVE.getCode());
        customerCoupon.setExpireTime(DateUtils.addMonths(new Date(),1));
        customerCoupon.setCouponGrantId(couponGrantRecord.getCouponGrantId());
        customerCoupon.setCouponId(coupon.getId());
        customerCoupon.setCouponName(coupon.getName());
        customerCoupon.setCouponType(coupon.getType());
        customerCoupon.setCouponGrantRecordId(couponGrantRecord.getId());
        customerCouponService.save(customerCoupon);
    }

    /**
     * 过滤有效优惠券
     * @param level
     * @param shopId
     * @param customerId
     * @param coupons
     * @return
     */
    private List<Coupon> filterEffectiveCoupons(Integer level,Long shopId,Long customerId,List<Coupon> coupons){
        if(level < VipLevelEnum.VIP5.getLevel() ){
            return coupons;
        }

        SysConfig sysConfig = sysConfigService.getByCode(ConfigConstant.YYBWC_COUPON_ID);
        BigDecimal paidAmount = ordersMapper.selectShopPaidAmount(shopId, customerId);
        if(BigDecimalUtil.lt(paidAmount,VipLevelEnum.VIP5.getMinAmount())){
            //达不到 VIP5的会员，除去月月霸王餐优惠券
            return coupons.stream().filter(o -> !Objects.equals(Long.valueOf(sysConfig.getVal()), o.getId())).collect(Collectors.toList());
        }
        return coupons;
    }
}
