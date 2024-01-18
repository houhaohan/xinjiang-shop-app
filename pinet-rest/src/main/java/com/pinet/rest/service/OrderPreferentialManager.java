package com.pinet.rest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.DiscountTypeEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.vo.PreferentialVo;
import com.pinet.rest.factory.PromotionStrategyFactory;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单优惠
 */
@Component
public class OrderPreferentialManager {

    private final ICouponService couponService;
    private final ICustomerMemberService customerMemberService;
    private final ICouponProductService couponProductService;

    public OrderPreferentialManager(ICouponService couponService,
                                    ICustomerMemberService customerMemberService,
                                    ICouponProductService couponProductService){
        this.couponService =  couponService;
        this.customerMemberService =  customerMemberService;
        this.couponProductService =  couponProductService;
    }

    public PreferentialVo doPreferential(Long customerId, Long customerCouponId, BigDecimal orderProductPrice,List<OrderProduct> orderProducts){
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

        Coupon coupon = couponService.getByCustomerCouponId(customerCouponId);
        if(coupon.getUseVip() == 0){
            orderDiscounts.clear();
        }
        PromotionStrategyFactory promotionStrategyFactory = new PromotionStrategyFactory(coupon);
        if(coupon.getUseProduct() == 1){
            //全部商品
            orderDiscounts.addAll(promotionStrategyFactory.create().apply(orderProductPrice));
        }else if(coupon.getUseProduct() == 2){
            //部分商品
            List<Long> shopProdIds = orderProducts.stream().map(OrderProduct::getShopProdId).collect(Collectors.toList());
            QueryWrapper<CouponProduct> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("product_id");
            queryWrapper.eq("coupon_id",coupon.getId());
            queryWrapper.in("product_id",shopProdIds);
            List<Long> productIds = couponProductService.listObjs(queryWrapper,productId-> Long.valueOf(productId.toString()));
            orderProducts = orderProducts.stream().filter(item -> productIds.contains(item.getShopProdId())).collect(Collectors.toList());
            orderDiscounts.addAll(promotionStrategyFactory.create().apply(orderProducts));
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
