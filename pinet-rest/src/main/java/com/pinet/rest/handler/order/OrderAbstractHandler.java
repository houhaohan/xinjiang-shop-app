package com.pinet.rest.handler.order;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.Environment;
import com.pinet.core.util.StringUtil;
import com.pinet.keruyun.openapi.param.CustomerParam;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.*;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.entity.vo.PreferentialVo;
import com.pinet.rest.mq.constants.QueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @description 订单抽象处理器
 * @author chengshuanghui
 * @data 2024-03-21 15:00
 */
@Slf4j
public abstract class OrderAbstractHandler extends ShippingFeeHandler implements OrderHandler{
    protected OrderContext context;

    protected Orders buildOrder(){
        Orders order = new Orders();
        order.setOrderNo(IdUtil.getSnowflake().nextId());
        order.setOrderType(context.request.getOrderType());
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setOrderSource(context.request.getOrderSource());
        order.setCustomerId(context.customerId);
        order.setShopId(context.request.getShopId());
        order.setKryShopId(context.shop.getKryShopId());
        order.setShopName(context.shop.getShopName());

        Date now = new Date();
        order.setEstimateArrivalStartTime(DateUtil.offsetHour(now, 1));
        order.setEstimateArrivalEndTime(DateUtil.offsetMinute(now, 90));
        order.setOrderDistance(context.distance.intValue());

        BigDecimal shippingFee = calculate(context.request.getOrderType(), order.getOrderDistance(), context.shop.getDeliveryPlatform());
        order.setShippingFee(shippingFee);
        order.setRemark(context.request.getRemark());
        order.setShareId(context.request.getShareId());
        order.setCustomerCouponId(context.request.getCustomerCouponId());

        //初始化订单金额
        order.setOrderPrice(BigDecimal.ZERO);
        order.setPackageFee(BigDecimal.ZERO);
        order.setOrderProdPrice(BigDecimal.ZERO);
        order.setCommission(BigDecimal.ZERO);
        context.ordersMapper.insert(order);
        return order;
    }

    /**
     * 达达平台配送费
     * @param orderType
     * @param shop
     * @param customerAddressId
     * @param orderProdPrice
     * @return
     */
    private BigDecimal getShippingFeePlat(Integer orderType, Shop shop, Long customerAddressId, BigDecimal orderProdPrice ) {
        if (Objects.equals(orderType, OrderTypeEnum.SELF_PICKUP.getCode())) {
            return BigDecimal.ZERO;
        }
        //测试环境默认4元吧
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }

        if (StringUtil.isBlank(shop.getDeliveryShopNo())
                || shop.getDeliveryPlatform().equals(DeliveryPlatformEnum.ZPS.getCode())) {
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }
        //查询收货地址
        CustomerAddress customerAddress = context.customerAddressService.getById(customerAddressId);
        if (ObjectUtil.isNull(customerAddress)) {
            throw new PinetException("收货地址异常");
        }

        Snowflake snowflake = IdUtil.getSnowflake();

        AddOrderReq addOrderReq = AddOrderReq.builder()
                .shopNo(shop.getDeliveryShopNo())
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
            log.error("查询配送费服务失败====>{}",e.getMessage());
            throw new PinetException("查询配送费服务失败");
        }
    }


    /**
     * 是否满足计算佣金的条件
     *
     * @param customerId 下单人 ID
     * @param shareId 分享人 ID
     */
    protected boolean commissionCondition(Long customerId,Long shareId) {

        //todo 店帮主逻辑暂时没有了，等后面版本再继续更新店帮主逻辑
        return false;
//        if (Objects.isNull(shareId) || shareId <= 0) {
//            return false;
//        }
//
//        //判断下单人和分享人是否是同一个人
//        if (Objects.equals(customerId,shareId)) {
//            return false;
//        }
//        //分享人会员等级
//        Integer shareMemberLevel = context.customerMemberService.getMemberLevel(shareId);
//        Integer orderMemberLevel = context.customerMemberService.getMemberLevel(customerId);
//
//        //邀请人必须是店帮主  被邀人不能是店帮主
//        return shareMemberLevel.equals(MemberLevelEnum._20.getCode()) && !orderMemberLevel.equals(MemberLevelEnum._20.getCode());
    }


    protected void beforeHandler(String phone){
        //校验下单用户是否是客如云会员
        CustomerParam param = new CustomerParam();
        param.setMobile(phone);
        context.kryApiService.queryByMobile(12698040L,"ae5f960130e9d2ed01b406be6988b576",null);
    }


    /**
     * 订单后置方法
     * @param orders
     * @param orderProducts
     */
    @Transactional(rollbackFor = Exception.class)
    public void afterHandler(Orders orders,List<OrderProduct> orderProducts) {
        PreferentialVo preferentialVo = context.orderPreferentialManager.doPreferential(orders.getCustomerId(), context.request.getCustomerCouponId(), orders.getOrderProdPrice(), orderProducts);
        orders.setDiscountAmount(preferentialVo.getDiscountAmount());
        BigDecimal orderPrice = BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), orders.getShippingFee(), orders.getPackageFee());
        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (!Objects.equals(orders.getOrderSource(), OrderSourceEnum.SYSTEM.getCode())
                && BigDecimalUtil.ne(orderPrice,context.request.getOrderPrice())) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }
        //修改订单金额
        Orders entity = new Orders();
        entity.setId(orders.getId());
        entity.setCommission(orders.getCommission());
        entity.setOrderPrice(orderPrice);
        entity.setPackageFee(orders.getPackageFee());
        entity.setOrderProdPrice(orders.getOrderProdPrice());
        BigDecimal commission = orderProducts.stream().map(OrderProduct::getCommission).reduce(BigDecimal.ZERO, BigDecimal::add);
        entity.setCommission(commission);
        entity.setDiscountAmount(preferentialVo.getDiscountAmount());
//        Integer level = context.customerMemberService.getMemberLevel(orders.getCustomerId());
        Integer level = context.vipUserService.getLevelByCustomerId(orders.getCustomerId());
        VipLevelEnum e = VipLevelEnum.getEnumByCode(level);

//        Integer score = new MemberLevelStrategyContext(orders.getOrderPrice()).getScore(level);
//        entity.setScore(score);
        entity.setShippingFeePlat(getShippingFeePlat(orders.getOrderType(),context.shop,context.request.getCustomerAddressId(),orders.getOrderProdPrice()));
        context.ordersMapper.updateById(entity);

        List<OrderDiscount> orderDiscounts = preferentialVo.getOrderDiscounts();
        if (!CollectionUtils.isEmpty(orderDiscounts)) {
            orderDiscounts.forEach(item->{
                item.setOrderId(orders.getId());
                item.setCreateBy(orders.getCreateBy());
            });
            context.orderDiscountService.saveBatch(orderDiscounts);
        }

        if(Objects.equals(context.request.getOrderType(),OrderTypeEnum.TAKEAWAY.getCode())){
            //外卖订单插入订单地址表
            OrderAddress orderAddress = context.orderAddressService.createByCustomerAddressId(context.request.getCustomerAddressId());
            orderAddress.setOrderId(orders.getId());
            Customer customer = context.customerService.getById(orders.getCustomerId());
            orderAddress.setSex(customer.getSex());
            context.orderAddressService.save(orderAddress);
        }

        context.jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME, orders.getId().toString(), OrderConstant.ORDER_AUTO_CANCEL_TIME);


        CreateOrderVo response = new CreateOrderVo();
        response.setOrderId(orders.getId());
        response.setOrderNo(orders.getOrderNo());
        response.setExpireTime(DateUtil.offsetMinute(orders.getCreateTime(), 15).getTime());
        response.setOrderPrice(orderPrice);
        context.response = response;
    }



}
