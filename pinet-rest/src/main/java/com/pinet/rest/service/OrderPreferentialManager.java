package com.pinet.rest.service;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.DiscountTypeEnum;
import com.pinet.rest.entity.enums.VipDiscountEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.vo.PreferentialVo;
import com.pinet.rest.factory.PromotionStrategyFactory;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单优惠
 * @author chengshuanghui
 */
@Component
public class OrderPreferentialManager {

    private final ICouponService couponService;
    private final ICouponProductService couponProductService;
    private final IVipUserService vipUserService;

    public OrderPreferentialManager(ICouponService couponService,
                                    IVipUserService vipUserService,
                                    ICouponProductService couponProductService){
        this.couponService =  couponService;
        this.vipUserService =  vipUserService;
        this.couponProductService =  couponProductService;
    }

    public PreferentialVo doPreferential(Long customerId, Long customerCouponId, BigDecimal orderProductPrice,List<OrderProduct> orderProducts){
        PreferentialVo preferentialVo = new PreferentialVo();
        List<OrderDiscount> orderDiscounts = new ArrayList<>();

        preferentialVo.setOrderDiscounts(orderDiscounts);
        preferentialVo.setDiscountAmount(BigDecimal.ZERO);
        preferentialVo.setProductDiscountAmount(orderProductPrice);
        preferentialVo.setOrderProductPrice(orderProductPrice);

        VipUser vipUser = vipUserService.getByCustomerId(customerId);
        //店帮主、会员 折扣
        if(Objects.nonNull(vipUser) && vipUser.getLevel() > VipLevelEnum.VIP1.getLevel()){
            //优惠后金额
            VipDiscountEnum e = VipDiscountEnum.getEnumByCode(vipUser.getLevel());
            BigDecimal discountedPrice = BigDecimalUtil.multiply(orderProductPrice, VipDiscountEnum.getDiscountDouble(e));
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg(e.getDescription())
                    .setDiscountAmount(BigDecimalUtil.subtract(orderProductPrice,discountedPrice))
                    .setType(DiscountTypeEnum.VIP_2.getCode());
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
            List<Long> productIds = couponProductService.getProdIdsByShopProdIdsAndCouponId(shopProdIds, coupon.getId());
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
