package com.pinet.rest.handler.order;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.Environment;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.DeliveryPlatformEnum;
import com.pinet.rest.entity.enums.OrderSourceEnum;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.entity.vo.PreferentialVo;
import com.pinet.rest.mapper.OrdersMapper;
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

/**
 * 直接购买订单处理器
 */
@Component
@RequiredArgsConstructor
public class DirectBuyOrderHandler extends OrderAbstractHandler implements OrderHandler{
    private final ICustomerMemberService customerMemberService;
    private final OrdersMapper ordersMapper;
    private final IShopProductService shopProductService;
    private final IShippingFeeRuleService shippingFeeRuleService;
    private final OrderSingleDishHandler orderSingleDishHandler;
    private final OrderComboDishHandler orderComboDishHandler;
    private final OrderAmountHandler orderAmountHandler;
    private final OrderPreferentialManager orderPreferentialManager;
    private final IOrderDiscountService orderDiscountService;
    private final IOrderAddressService orderAddressService;
    private final ICustomerService customerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrderContext context) {
        super.context = context;
        List<Long> shopProdSpecIds = Convert.toList(Long.class,context.request.getShopProdSpecIds());
        Orders orders = buildOrder();
        BigDecimal shippingFeePlat = orderAmountHandler.getShippingFeePlat(orders.getOrderType(),context.shop,context.request.getCustomerAddressId(),orders.getOrderProdPrice());
        orders.setShippingFeePlat(shippingFeePlat);

        //初始化订单金额
        orders.setOrderPrice(BigDecimal.ZERO);
        orders.setPackageFee(BigDecimal.ZERO);
        orders.setOrderProdPrice(BigDecimal.ZERO);
        orders.setCommission(BigDecimal.ZERO);
        ordersMapper.insert(orders);
        boolean condition = orderAmountHandler.commissionCondition(orders.getCustomerId(), orders.getShareId());

        ShopProduct shopProduct = shopProductService.getById(context.request.getShopProdId());
        //判断店铺商品是否下架
        if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_SHELF.getCode())) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        DirectOrderRequest request = DirectOrderRequest.builder()
                .orderId(orders.getId())
                .shopProdId(shopProduct.getId())
                .productName(shopProduct.getProductName())
                .unit(shopProduct.getUnit())
                .productImg(shopProduct.getProductImg())
                .dishId(shopProduct.getProdId())
                .prodNum(context.request.getProdNum())
                .shopProdSpecIds(shopProdSpecIds)
                .calculate(condition)
                .build();
        OrderProduct orderProduct = null;
        List<OrderProduct> orderProducts = new ArrayList<>();
        if(Objects.equals(shopProduct.getDishType(), DishType.SINGLE)){
            orderProduct = orderSingleDishHandler.directOrder(request);
        }else {
            request.setComboDishDtoList(context.request.getOrderComboDishList());
            orderProduct = orderComboDishHandler.directOrder(request);
        }
        orderProducts.add(orderProduct);
        orders.setPackageFee(orderProduct.getPackageFee());
        orders.setOrderProdPrice(orderProduct.getProdPrice());

        PreferentialVo preferentialVo = orderPreferentialManager.doPreferential(context.customerId, context.request.getCustomerCouponId(), orders.getOrderProdPrice(), orderProducts);
        orders.setDiscountAmount(preferentialVo.getDiscountAmount());
        BigDecimal orderPrice = BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), orders.getShippingFee(), orders.getPackageFee());
        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (!Objects.equals(orders.getOrderSource(), OrderSourceEnum.SYSTEM.getCode())
                && BigDecimalUtil.ne(orderPrice,context.getRequest().getOrderPrice())) {
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
        if(Objects.equals(context.request.getOrderType(), OrderTypeEnum.SELF_PICKUP.getCode())){
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
