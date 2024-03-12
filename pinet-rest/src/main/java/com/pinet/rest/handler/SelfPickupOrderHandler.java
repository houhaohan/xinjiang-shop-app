package com.pinet.rest.handler;

import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.enums.OrderSourceEnum;
import com.pinet.rest.entity.vo.PreferentialVo;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * @description: 自提单
 * @author: chengshuanghui
 * @date: 2024-03-09 10:23
 */
public class SelfPickupOrderHandler extends OrderAbstractHandler implements OrderHandler{

    public SelfPickupOrderHandler(OrderContext context){
        this.context = context;
    }


    @Override
    public void checkData() {
        super.checkShop(context.shop);
    }


    @Override
    public void amountHandler() {
        Orders order = context.order;
        BigDecimal packageFee = packageFee();
        PreferentialVo preferentialVo = context.orderPreferentialManager.doPreferential(context.userId, context.request.getCustomerCouponId(), orderProdOriginalPrice(), context.orderProducts);
        BigDecimal orderPrice = BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), deliveryFeeHandler(), packageFee);
        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (!Objects.equals(context.request.getOrderSource(), OrderSourceEnum.SYSTEM.getCode())
                && BigDecimalUtil.ne(orderPrice,context.request.getOrderPrice())) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }
        order.setOrderPrice(orderPrice);
        order.setOrderProdPrice(preferentialVo.getProductDiscountAmount());
        order.setDiscountAmount(preferentialVo.getDiscountAmount());
        order.setShippingFee(BigDecimal.ZERO);
        order.setShippingFeePlat(BigDecimal.ZERO);
        order.setPackageFee(packageFee);
        order.setOrderDistance(0);
    }

    @Override
    public BigDecimal deliveryFeeHandler() {
        return BigDecimal.ZERO;
    }

    @Override
    public void promotionHandler() {

    }

    @Override
    public void couponHandler() {

    }

    @Override
    public void successHandler() {

    }


}
