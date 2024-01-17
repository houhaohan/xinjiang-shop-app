package com.pinet.rest.service;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.enums.DiscountTypeEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.vo.PreferentialVo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单优惠
 */
@Component
public class OrderPreferentialManager {

    private final ICouponService couponService;

    private final ICustomerMemberService customerMemberService;

    public OrderPreferentialManager(ICouponService couponService,ICustomerMemberService customerMemberService){
        this.couponService =  couponService;
        this.customerMemberService =  customerMemberService;
    }


    public PreferentialVo doPreferential(Long customerId, Long customerCouponId, BigDecimal orderProductPrice){
        PreferentialVo preferentialVo = new PreferentialVo();
        List<OrderDiscount> orderDiscounts = new ArrayList<>();

        preferentialVo.setOrderDiscounts(orderDiscounts);
        preferentialVo.setDiscountAmount(BigDecimal.ZERO);
        preferentialVo.setProductDiscountAmount(orderProductPrice);
        preferentialVo.setOrderProductPrice(orderProductPrice);

        CustomerMember customerMember = customerMemberService.getByCustomerId(customerId);
        //店帮主、会员 折扣
        if(customerMember != null){
            //优惠后金额
            MemberLevelEnum e = MemberLevelEnum.getEnumByCode(customerMember.getMemberLevel());
            BigDecimal discountedPrice = BigDecimalUtil.multiply(orderProductPrice, MemberLevelEnum._10.getDiscount());
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg(e.getMsg() + BigDecimalUtil.stripTrailingZeros(BigDecimalUtil.multiply(e.getDiscount(),new BigDecimal("10"))) + "折优惠")
                    .setDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,discountedPrice))
                    .setType(DiscountTypeEnum.VIP_1.getCode());
            orderDiscounts.add(orderDiscount);
            preferentialVo.setOrderDiscounts(orderDiscounts);
            preferentialVo.setDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,discountedPrice));
            preferentialVo.setProductDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,preferentialVo.getDiscountAmount()));
            preferentialVo.setOrderProductPrice(orderProductPrice);
        }

        //优惠券折扣
        if(customerCouponId == null){
           return preferentialVo;
        }
        //清空店帮主的优惠数据
        Coupon coupon = couponService.getByCustomerCouponId(customerCouponId);
        if(coupon.getUseVip() == 0){
            orderDiscounts.clear();
            if(coupon.getType() == 1){
                //满减券
                OrderDiscount orderDiscount = new OrderDiscount();
                orderDiscount.setDiscountMsg("满减优惠券")
                        .setDiscountAmount(coupon.getCouponPrice())
                        .setType(DiscountTypeEnum.VIP_1.getCode());
                orderDiscounts.add(orderDiscount);
            }else if(coupon.getType() == 2){
                //折扣券
                BigDecimal productDiscountAmount = BigDecimalUtil.multiply(orderProductPrice, new BigDecimal(coupon.getDiscount() * 0.01));
                OrderDiscount orderDiscount = new OrderDiscount();
                orderDiscount.setDiscountMsg("折扣优惠券")
                        .setDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,productDiscountAmount))
                        .setType(DiscountTypeEnum.VIP_1.getCode());
                orderDiscounts.add(orderDiscount);
            }
        }else {
            //会员同享,先享受会员折扣再享受优惠券折扣
            if(coupon.getType() == 1){
                //满减券
                OrderDiscount orderDiscount = new OrderDiscount();
                orderDiscount.setDiscountMsg("满减优惠券")
                        .setDiscountAmount(coupon.getCouponPrice())
                        .setType(DiscountTypeEnum.VIP_1.getCode());
                orderDiscounts.add(orderDiscount);
            }else if(coupon.getType() == 2){
                //折扣券
                BigDecimal productDiscountAmount = BigDecimalUtil.multiply(orderProductPrice, new BigDecimal(coupon.getDiscount() * 0.01));
                OrderDiscount orderDiscount = new OrderDiscount();
                orderDiscount.setDiscountMsg("折扣优惠券")
                        .setDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,productDiscountAmount))
                        .setType(DiscountTypeEnum.VIP_1.getCode());
                orderDiscounts.add(orderDiscount);
            }
        }
        preferentialVo.setOrderDiscounts(orderDiscounts);
        BigDecimal discountAmount = orderDiscounts.stream().map(OrderDiscount::getDiscountAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        preferentialVo.setDiscountAmount(discountAmount);
        BigDecimal productDiscountAmount = BigDecimalUtil.subtract(orderProductPrice,discountAmount);
        preferentialVo.setProductDiscountAmount(productDiscountAmount.compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal("0.01") : productDiscountAmount);
        preferentialVo.setOrderProductPrice(orderProductPrice);
        return preferentialVo;
    }
}
