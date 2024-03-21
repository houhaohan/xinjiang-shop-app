package com.pinet.rest.handler.order;

import cn.hutool.core.date.DateUtil;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.Environment;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.*;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.entity.vo.PreferentialVo;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import com.pinet.rest.strategy.MemberLevelStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CartOrderHandler extends OrderAbstractHandler implements OrderHandler{
    private final OrderPreferentialManager orderPreferentialManager;
    private final IShippingFeeRuleService shippingFeeRuleService;
    private final ICustomerMemberService customerMemberService;
    private final OrdersMapper ordersMapper;
    private final IShopProductService shopProductService;
    private final OrderComboDishHandler orderComboDishHandler;
    private final OrderSingleDishHandler orderSingleDishHandler;
    private final IOrderAddressService orderAddressService;
    private final IOrderDiscountService orderDiscountService;
    private final ICustomerService customerService;
    private final OrderAmountHandler orderAmountHandler;
    private final JmsUtil jmsUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrderContext context) {
        super.context = context;
        Orders orders = buildOrder();
        BigDecimal shippingFeePlat = orderAmountHandler.getShippingFeePlat(orders.getOrderType(),context.shop,context.request.getCustomerAddressId(),orders.getOrderProdPrice());
        orders.setShippingFeePlat(shippingFeePlat);
        orders.setShippingFee(shippingFeeRule());

        //初始化订单金额
        orders.setOrderPrice(BigDecimal.ZERO);
        orders.setPackageFee(BigDecimal.ZERO);
        orders.setOrderProdPrice(BigDecimal.ZERO);
        orders.setCommission(BigDecimal.ZERO);
        ordersMapper.insert(orders);
        boolean condition = orderAmountHandler.commissionCondition(orders.getCustomerId(), orders.getShareId());

        List<OrderProduct> orderProducts = new ArrayList<>();
        for(Cart cart : context.cartList){
            ShopProduct shopProduct = shopProductService.getById(cart.getShopProdId());
            //判断店铺商品是否下架
            if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_SHELF.getCode())) {
                throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
            }

            //判断店铺商品是否删除
            if (shopProduct.getDelFlag() == 1) {
                throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
            }
            //订单类型( 1外卖  2自提)
            OrderProductRequest request = OrderProductRequest.builder()
                    .cartId(cart.getId())
                    .dishId(cart.getDishId())
                    .orderId(orders.getId())
                    .shopProdId(cart.getShopProdId())
                    .prodName(shopProduct.getProductName())
                    .prodNum(cart.getProdNum())
                    .prodImg(shopProduct.getProductImg())
                    .unit(shopProduct.getUnit())
                    .orderType(context.request.getOrderType())
                    .calculate(condition)
                    .build();
            OrderProduct orderProduct = null;
            if(Objects.equals(cart.getDishType(), DishType.SINGLE)){
                orderProduct = orderSingleDishHandler.exectue(request);
            }else {
                orderProduct = orderComboDishHandler.exectue(request);
            }

            orders.setPackageFee(BigDecimalUtil.sum(orders.getPackageFee(),orderProduct.getPackageFee()));
            orders.setOrderProdPrice(BigDecimalUtil.sum(orders.getOrderProdPrice(),orderProduct.getProdPrice()));
            orderProducts.add(orderProduct);
        }

        PreferentialVo preferentialVo = orderPreferentialManager.doPreferential(context.customerId, context.request.getCustomerCouponId(), orders.getOrderProdPrice(), orderProducts);
        orders.setDiscountAmount(preferentialVo.getDiscountAmount());
        BigDecimal orderPrice = BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), orders.getShippingFee(), orders.getPackageFee());
        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (!Objects.equals(orders.getOrderSource(), OrderSourceEnum.SYSTEM.getCode())
                && BigDecimalUtil.ne(orderPrice,context.request.getOrderPrice())) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }
        orders.setCommission(BigDecimalUtil.multiply(orders.getOrderPrice(),0.1));

        //修改订单金额
        Orders entity = new Orders();
        entity.setId(orders.getId());
        entity.setCommission(orders.getCommission());
        entity.setOrderPrice(orderPrice);
        entity.setPackageFee(orders.getPackageFee());
        entity.setOrderProdPrice(orders.getOrderProdPrice());
        Integer level = customerMemberService.getMemberLevel(orders.getCustomerId());
        context.orderUserlevel = level;
        Integer score = new MemberLevelStrategyContext(orders.getOrderPrice()).getScore(level);
        orders.setScore(score);
        ordersMapper.updateById(entity);

        //插入优惠明细表
        List<OrderDiscount> orderDiscounts = preferentialVo.getOrderDiscounts();
        if (!CollectionUtils.isEmpty(orderDiscounts)) {
            orderDiscounts.forEach(item -> item.setOrderId(orders.getId()));
            orderDiscountService.saveBatch(orderDiscounts);
        }


        if(Objects.equals(context.request.getOrderType(),OrderTypeEnum.TAKEAWAY.getCode())){
            //外卖订单插入订单地址表
            OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(context.request.getCustomerAddressId());
            orderAddress.setOrderId(orders.getId());
            Customer customer = customerService.getById(orders.getCustomerId());
            orderAddress.setSex(customer.getSex());
            orderAddressService.save(orderAddress);
        }

        //todo 清除购物车

        //jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME, orders.getId().toString(), OrderConstant.ORDER_AUTO_CANCEL_TIME);
        CreateOrderVo response = new CreateOrderVo();
        response.setOrderId(orders.getId());
        response.setOrderNo(orders.getOrderNo());
        response.setExpireTime(DateUtil.offsetMinute(orders.getCreateTime(), 15).getTime());
        response.setOrderPrice(orders.getOrderPrice());
        context.response = response;

    }

    @Override
    public BigDecimal shippingFeeRule() {
        if(Objects.equals(context.request.getOrderType(),OrderTypeEnum.SELF_PICKUP.getCode())){
            return BigDecimal.ZERO;
        }
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if (context.shop.getDeliveryPlatform().equals(DeliveryPlatformEnum.ZPS.getCode())) {
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }

        BigDecimal shippingFee = shippingFeeRuleService.getByDistance(context.distance);
        if (shippingFee == null) {
            throw new PinetException("配送费查询失败");
        }
        return shippingFee;
    }
}
