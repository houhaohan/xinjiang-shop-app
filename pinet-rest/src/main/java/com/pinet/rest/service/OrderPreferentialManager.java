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

    private ICouponService couponService;

    private ICustomerMemberService customerMemberService;

    public OrderPreferentialManager(ICouponService couponService,ICustomerMemberService customerMemberService){
        this.couponService =  couponService;
        this.customerMemberService =  customerMemberService;
    }


    public PreferentialVo doPreferential(Long customerId, Long customerCouponId, BigDecimal orderProductPrice){
        PreferentialVo preferentialVo = new PreferentialVo();
        List<OrderDiscount> orderDiscounts = new ArrayList<>();

        BigDecimal discountAmount = BigDecimal.ZERO;
        CustomerMember customerMember = customerMemberService.getByCustomerId(customerId);
        //店帮主、会员 折扣
        if(customerMember != null){
            //优惠后金额
            MemberLevelEnum e = MemberLevelEnum.getEnumByCode(customerMember.getMemberLevel());
            BigDecimal discountedPrice = BigDecimalUtil.multiply(orderProductPrice, MemberLevelEnum._10.getDiscount());
            discountAmount = BigDecimalUtil.subtract(orderProductPrice,discountedPrice);
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg(e.getMsg() + e.getDiscount().multiply(new BigDecimal(10)) + "折优惠")
                    .setDiscountAmount(discountAmount)
                    .setType(DiscountTypeEnum.VIP_1.getCode());
            orderDiscounts.add(orderDiscount);
        }

        //优惠券折扣
        if(customerCouponId == null){
            preferentialVo.setOrderDiscounts(orderDiscounts);
            preferentialVo.setOrderProductPrice(orderProductPrice);
            preferentialVo.setDiscountAmount(discountAmount);
            preferentialVo.setProductDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,discountAmount));
           return preferentialVo;
        }
        //清空店帮主的优惠数据
        orderDiscounts.clear();
        Coupon coupon = couponService.getByCustomerCouponId(customerCouponId);
        //校验
        if(coupon.getType() == 1){
            //满减券
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg("满减优惠券")
                    .setDiscountAmount(coupon.getCouponPrice())
                    .setType(DiscountTypeEnum.VIP_1.getCode());
            orderDiscounts.add(orderDiscount);

            preferentialVo.setOrderProductPrice(orderProductPrice);
            preferentialVo.setOrderDiscounts(orderDiscounts);
            preferentialVo.setDiscountAmount(coupon.getCouponPrice());
            preferentialVo.setProductDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,coupon.getCouponPrice()));
        }else if(coupon.getType() == 2){
            //折扣券
            BigDecimal productDiscountAmount = BigDecimalUtil.multiply(orderProductPrice, new BigDecimal(coupon.getDiscount() * 0.01));
            discountAmount = BigDecimalUtil.subtract(orderProductPrice,productDiscountAmount);
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg("折扣优惠券")
                    .setDiscountAmount(discountAmount)
                    .setType(DiscountTypeEnum.VIP_1.getCode());
            orderDiscounts.add(orderDiscount);

            preferentialVo.setOrderDiscounts(orderDiscounts);
            preferentialVo.setOrderProductPrice(orderProductPrice);
            preferentialVo.setDiscountAmount(discountAmount);
            preferentialVo.setProductDiscountAmount(productDiscountAmount);
        }
        return preferentialVo;
    }


}
