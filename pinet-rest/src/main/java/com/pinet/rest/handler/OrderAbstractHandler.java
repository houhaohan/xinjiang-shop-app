package com.pinet.rest.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.vo.OrderProductVo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 订单抽象处理器
 * @author: chengshuanghui
 * @date: 2024-03-09 11:13
 */
public abstract class OrderAbstractHandler implements OrderHandler {
    protected OrderContext context;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(){
        //校验数据
        checkData();
        //构建基本数据
        buildOrder();
//        //限时活动处理
//        promotionHandler();
//        //优惠券处理
//        couponHandler();
        //订单金额处理
        amountHandler();
        //设置佣金
        setOrdersCommission();
        //数据入库
        insertData();

        System.err.println(1/0);
    }


    /**
     * 校验店铺
     */
    protected void checkShop(Shop shop) {
        //判断店铺是否营业
        if (!context.shopService.checkShopStatus(shop)) {
            throw new PinetException("店铺已经打烊了~");
        }
    }

    /**
     * 构造订单数据
     */
    protected void buildOrder() {
        List<OrderProductVo> orderProducts = new ArrayList<>();
        CreateOrderDto dto = context.request;
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = context.orderProductService.getByCartAndShop(dto.getShopId(), dto.getOrderType());

            //删除购物车已购商品
            context.cartService.delCartByShopId(dto.getShopId(), context.userId);
        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds, dto.getOrderType());
            OrderProductVo orderProduct = context.orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }
        context.orderProducts = orderProducts;

        Date now = new Date();
        Date estimateArrivalStartTime = DateUtil.offsetHour(now, 1);
        Date estimateArrivalEndTime = DateUtil.offsetMinute(now, 90);
        Orders order = new Orders();
        Snowflake snowflake = IdUtil.getSnowflake();
        order.setOrderNo(snowflake.nextId());
        order.setOrderType(context.request.getOrderType());
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setCustomerId(context.userId);
        order.setShopId(context.shop.getId());
        order.setShopName(context.shop.getShopName());
        order.setEstimateArrivalStartTime(estimateArrivalStartTime);
        order.setEstimateArrivalEndTime(estimateArrivalEndTime);
        order.setRemark(context.request.getRemark());
        order.setShareId(context.request.getShareId());
        order.setKryShopId(context.shop.getKryShopId());
        order.setOrderSource(context.request.getOrderSource());
        order.setCustomerCouponId(context.request.getCustomerCouponId());
        context.order = order;
    }

    /**
     * 订单打包费
     * @return
     */
    protected BigDecimal packageFee() {
//        List<OrderProduct> orderProducts = context.orderProducts;
//        return orderProducts.stream().map(OrderProduct::getPackageFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        return null;
    }

    /**
     * 订单商品原价
     * @return
     */
    protected BigDecimal orderProdOriginalPrice() {
        List<OrderProductVo> orderProducts = context.orderProducts;
        return orderProducts.stream().map(OrderProductVo::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertData(){
        //插入订单
//        Orders order = context.order;
//        context.ordersService.save(order);
//        IOrderProductService orderProductService = context.orderProductService;
//        IOrderProductSpecService orderProductSpecService = context.orderProductSpecService;
//        //插入订单商品
//        List<OrderProductVo> orderProducts = context.orderProducts;
//        orderProducts.forEach(item -> {
//            item.setOrderId(order.getId());
//            //保存订单商品表
//            orderProductService.save(item);
//
//            //保存订单商品样式表
//            item.getOrderProductSpecs().forEach(spec -> {
//                spec.setOrderProdId(item.getId());
//                spec.setOrderId(order.getId());
//            });
//            orderProductSpecService.saveBatch(item.getOrderProductSpecs());
//        });
//        //插入优惠明细表
//        List<OrderDiscount> orderDiscounts = context.orderDiscounts;
//        if (!CollectionUtils.isEmpty(orderDiscounts)) {
//            orderDiscounts.forEach(item -> item.setOrderId(order.getId()));
//            context.orderDiscountService.saveBatch(orderDiscounts);
//        }
//
//        //将订单放到mq中
//        context.jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME, context.order.getId().toString(), OrderConstant.ORDER_AUTO_CANCEL_TIME);
//        CreateOrderVo result = new CreateOrderVo();
//        result.setOrderId(context.order.getId());
//        result.setOrderNo(context.order.getOrderNo());
//        result.setOrderPrice(context.order.getOrderPrice());
//        //返回订单过期时间
//        Date expireTime = DateUtil.offsetMinute(context.order.getCreateTime(), 15);
//        result.setExpireTime(expireTime.getTime());
//        context.result = result;
    };



    /**
     * 分割商品样式id
     */
    private List<Long> splitShopProdSpecIds(String shopProdSpecIds) {
        String[] idArray = shopProdSpecIds.split(",");

        return Arrays.stream(idArray)
                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
    }

    /**
     * 设置佣金
     */
    protected void setOrdersCommission() {
//        Orders orders = context.order;
//        List<OrderProduct> orderProducts = context.orderProducts;
//        if (Objects.isNull(orders.getShareId()) || orders.getShareId() <= 0) {
//            return;
//        }
//
//        //判断下单人和分享人是否是同一个人
//        if (orders.getCustomerId().equals(orders.getShareId())) {
//            return;
//        }
//
//        //下单人会员等级
//        Integer customerMemberLevel = context.customerMemberService.getMemberLevel(orders.getCustomerId());
//        //分享人会员等级
//        Integer shareMemberLevel = context.customerMemberService.getMemberLevel(orders.getShareId());
//
//        //邀请人必须是店帮主  被邀人不能是店帮主
//        if (shareMemberLevel.equals(MemberLevelEnum._20.getCode()) && !customerMemberLevel.equals(MemberLevelEnum._20.getCode())) {
//            //佣金=商品总金额 * 0.1
//            BigDecimal commission = BigDecimalUtil.multiply(orders.getOrderProdPrice(),0.1);
//            orders.setCommission(commission);
//            //设置单个商品的佣金
//            orderProducts.forEach(item -> {
//                item.setCommission(BigDecimalUtil.multiply(item.getProdPrice(),0.1));
//            });
//
//        }
    }


}
