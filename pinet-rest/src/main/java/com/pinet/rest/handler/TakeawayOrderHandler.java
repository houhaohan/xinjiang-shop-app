package com.pinet.rest.handler;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.*;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.DeliveryPlatformEnum;
import com.pinet.rest.entity.enums.OrderSourceEnum;
import com.pinet.rest.entity.vo.PreferentialVo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * @description: 外卖单
 * @author: chengshuanghui
 * @date: 2024-03-09 10:23
 */
public class TakeawayOrderHandler extends OrderAbstractHandler implements OrderHandler {

    public TakeawayOrderHandler(OrderContext context){
        this.context = context;
    }


    @Override
    public void checkData() {
        Shop shop = context.shop;
        super.checkShop(shop);
        if (Objects.equals(shop.getSupportDelivery(), CommonConstant.NO)) {
            throw new PinetException("该店铺暂不支持外卖订单");
        }
    }


    @Override
    public void amountHandler() {
        BigDecimal orderProdOriginalPrice = orderProdOriginalPrice();
        if (BigDecimalUtil.gt(context.shop.getMinDeliveryPrice(),orderProdOriginalPrice)) {
            throw new PinetException("餐品价格低于" + context.shop.getMinDeliveryPrice() + "元，无法配送");
        }
        Orders order = context.order;
        BigDecimal shippingFeePlat = getShippingFeePlat(orderProdOriginalPrice, context.shop.getDeliveryShopNo(), context.shop.getDeliveryPlatform());
        BigDecimal shippingFee = deliveryFeeHandler();
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
        order.setShippingFee(shippingFee);
        order.setShippingFeePlat(shippingFeePlat);
        order.setPackageFee(packageFee);
        order.setOrderDistance(getDistance().intValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertData() {
        super.insertData();
        //外卖订单插入订单地址表
        OrderAddress orderAddress = context.orderAddressService.createByCustomerAddressId(context.request.getCustomerAddressId());
        orderAddress.setOrderId(context.order.getId());
        Customer customer = context.customerService.getById(context.order.getCustomerId());
        orderAddress.setSex(customer.getSex());
        context.orderAddressService.save(orderAddress);
    }

    @Override
    public void promotionHandler() {

    }


    /**
     * 用户支付的配送费
     * @return
     */
    @Override
    public BigDecimal deliveryFeeHandler() {
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if(Objects.equals(context.shop.getDeliveryPlatform(),DeliveryPlatformEnum.ZPS.getCode())){
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }
        //用户实际支付的配送费
        BigDecimal shippingFee = context.shippingFeeRuleService.getByDistance(getDistance());
        if (shippingFee == null) {
            throw new PinetException("配送费查询失败");
        }

        return shippingFee;
    }

    @Override
    public void couponHandler() {

    }

    @Override
    public void successHandler() {

    }


    /**
     * 获取距离
     */
    private Double getDistance() {
        CustomerAddress customerAddress = context.customerAddressService.getById(context.request.getCustomerAddressId());
        double distance = LatAndLngUtils.getDistance(customerAddress.getLng().doubleValue(), customerAddress.getLat().doubleValue(),
                Double.parseDouble(context.shop.getLng()), Double.parseDouble(context.shop.getLat()));
        if (distance > context.shop.getDeliveryDistance()) {
            throw new PinetException("店铺距离过远,无法配送");
        }
        return distance;
    }


    /**
     * 获取平台配送费
     * @param deliveryPlatform 配送平台( ZPS-自配送，DADA-达达)
     * @return BigDecimal
     */
    private BigDecimal getShippingFeePlat( BigDecimal orderProdPrice, String deliveryShopNo, String deliveryPlatform) {
        //测试环境默认4元吧
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if (StringUtil.isBlank(deliveryShopNo) || deliveryPlatform.equals(DeliveryPlatformEnum.ZPS.getCode())) {
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }
        //查询收货地址
        CustomerAddress customerAddress = context.customerAddressService.getById(context.request.getCustomerAddressId());
        if (Objects.isNull(customerAddress)) {
            throw new PinetException("收货地址异常");
        }

        Snowflake snowflake = IdUtil.getSnowflake();

        AddOrderReq addOrderReq = AddOrderReq.builder()
                .shopNo(deliveryShopNo)
                .originId(snowflake.nextIdStr())
                .cargoPrice(orderProdPrice.doubleValue())
                .prepay(0)
                .receiverName(customerAddress.getName())
                .receiverAddress(customerAddress.getAddress())
                .receiverPhone(customerAddress.getPhone())
                .callback("http://xinjiangapi.ypxlbz.com/house/qingshi/api/dada/deliverFee/callback")
                .cargoWeight(0.5)
                .receiverLat(customerAddress.getLat().doubleValue())
                .receiverLng(customerAddress.getLng().doubleValue())
                .build();
        try {
            AddOrderResp addOrderResp = context.daDaService.queryDeliverFee(addOrderReq);
            return BigDecimal.valueOf(addOrderResp.getDeliverFee());
        } catch (RpcException e) {
            throw new PinetException("查询配送费服务失败");
        }
    }

}
