package com.pinet.rest.mq.consumer;

import com.pinet.core.constants.ConfigConstant;
import com.pinet.core.util.DateUtils;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.entity.CouponGrantRecord;
import com.pinet.rest.entity.CustomerCoupon;
import com.pinet.rest.entity.SysConfig;
import com.pinet.rest.entity.enums.CouponReceiveStatusEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 会员权益活动
 */
@Component
@RequiredArgsConstructor
public class VipActivityListener {
    private final IVipUserService vipUserService;
    private final ICouponGrantRecordService couponGrantRecordService;
    private final ICustomerCouponService customerCouponService;
    private final ICouponService couponService;
    private final ISysConfigService sysConfigService;


    @JmsListener(destination = QueueConstants.VIP_ACTIVITY, containerFactory = "queueListener")
    public void send(String message) {
        Long customerId = Long.valueOf(message);
        Integer level = vipUserService.getLevelByCustomerId(customerId);
        if(Objects.equals(level, VipLevelEnum.VIP5.getLevel())){
            //月月霸王餐
            SysConfig sysConfig = sysConfigService.getByCode(ConfigConstant.YYBWC_COUPON_ID);
            Coupon coupon = couponService.getById(Long.valueOf(sysConfig.getVal()));
            if(coupon == null){
                return;
            }
            CouponGrantRecord couponGrantRecord = new CouponGrantRecord();
            couponGrantRecord.setCouponGrantId(0L);
            couponGrantRecord.setSubject("VIP5");
            couponGrantRecord.setNum(1);
            couponGrantRecord.setRemark("VIP5 月月霸王餐");
            couponGrantRecord.setGrantTime(LocalDateTime.now());
            couponGrantRecordService.save(couponGrantRecord);

            CustomerCoupon customerCoupon = new CustomerCoupon();
            customerCoupon.setCustomerId(customerId);
            customerCoupon.setCouponStatus(CouponReceiveStatusEnum.RECEIVED.getCode());
            customerCoupon.setExpireTime(DateUtils.addMonths(new Date(),1));
            customerCoupon.setCouponGrantId(couponGrantRecord.getCouponGrantId());
            customerCoupon.setCouponId(coupon.getId());
            customerCoupon.setCouponName(coupon.getName());
            customerCoupon.setCouponType(coupon.getType());
            customerCoupon.setCouponGrantRecordId(couponGrantRecord.getId());
            customerCouponService.save(customerCoupon);
        }
    }
}
