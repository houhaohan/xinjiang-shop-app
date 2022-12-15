package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.common.mq.config.QueueConstants;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.entity.vo.OrderSettlementVo;
import com.pinet.rest.mapper.OrderMapper;
import com.pinet.rest.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private IOrderProductService orderProductService;

    @Resource
    private IOrderAddressService orderAddressService;

    @Resource
    private IShopService shopService;

    @Resource
    private ICartService cartService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Resource
    private JmsUtil jmsUtil;

    @Override
    public List<OrderListVo> orderList(OrderListDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<OrderListVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        IPage<OrderListVo> orderListVos = orderMapper.selectOrderList(page, customerId);
        orderListVos.getRecords().forEach(k -> {
            List<OrderProduct> orderProducts = orderProductService.getByOrderId(k.getOrderId());
            k.setOrderProducts(orderProducts);
            k.setProdNum(orderProducts.size());
            k.setOrderStatusStr(OrderStatusEnum.getEnumByCode(k.getOrderStatus()));
        });
        return orderListVos.getRecords();
    }

    @Override
    public OrderDetailVo orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = orderMapper.selectOrderDetail(orderId);
        if (orderDetailVo == null) {
            throw new PinetException("订单不存在");
        }
        orderDetailVo.setOrderProducts(orderProductService.getByOrderId(orderId));

        OrderAddress orderAddress = orderAddressService.getOrderAddress(orderId);

        orderDetailVo.setAddress(orderAddress.getAddress());
        //脱敏
        orderDetailVo.setName(DesensitizedUtil.chineseName(orderAddress.getName()));
        orderDetailVo.setTel(DesensitizedUtil.mobilePhone(orderAddress.getTel()));
        return orderDetailVo;
    }

    @Override
    public OrderSettlementVo orderSettlement(OrderSettlementDto dto) {
        OrderSettlementVo vo = new OrderSettlementVo();
        //配送费默认4元 自提没有配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType());

        vo.setShippingFee(shippingFee);

        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId());
        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), dto.getShopProdSpecId());
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        vo.setOrderProductBoList(orderProducts);

        //判断店铺是否营业
        if (!shopService.checkShopStatus(dto.getShopId())) {
            throw new PinetException("店铺已经打烊了~");
        }

        vo.setOrderMakeCount(countShopOrderMakeNum(dto.getShopId()));
        //计算总金额
        BigDecimal orderPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(shippingFee, BigDecimal::add);
        vo.setOrderPrice(orderPrice);
        return vo;
    }

    @Override
    public Integer countShopOrderMakeNum(Long shopId) {
        Date date = new Date();
        Date queryDate = DateUtil.offsetHour(date, -8);
        return orderMapper.countShopOrderMakeNum(shopId, queryDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderVo createOrder(CreateOrderDto dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();

        Shop shop = shopService.getById(dto.getShopId());
        //判断店铺是否营业
        if (!shopService.checkShopStatus(shop)) {
            throw new PinetException("店铺已经打烊了~");
        }

        //配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType());

        //外卖订单 校验距离 10公里以内
        double m =  LatAndLngUtils.getDistance(Double.parseDouble(dto.getLng()),Double.parseDouble(dto.getLat()),
                Double.parseDouble(shop.getLng()),Double.parseDouble(shop.getLat()));
        if (m > 10000D && dto.getOrderType() == 1){
            throw new PinetException("店铺距离过远,无法配送");
        }

        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId());

            //删除购物车已购商品
            cartService.delCartByShopId(dto.getShopId(),userId);
        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), dto.getShopProdSpecId());
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        //减少已购商品的库存(第一版暂不加锁 后期考虑加乐观锁或redis锁)
        for (OrderProduct orderProduct : orderProducts) {
            int res = shopProductSpecService.reduceStock(orderProduct.getShopProdSpecId(),orderProduct.getProdNum());
            if (res != 1){
                throw new PinetException("库存更新失败");
            }
        }


       //计算订单总金额  和订单商品金额
        BigDecimal orderPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(shippingFee, BigDecimal::add);
        BigDecimal orderProdPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //创建订单基础信息
        Order order = createOrder(dto,shippingFee,m,orderPrice,orderProdPrice,shop);
        //插入订单
        this.save(order);

        //插入订单商品  并设置订单号
        orderProducts.forEach(k->{
            k.setOrderId(order.getId());
        });
        orderProductService.saveBatch(orderProducts);


        //外卖订单插入订单地址表
        if (dto.getOrderType() == 1){
            OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(dto.getCustomerAddressId());
            orderAddressService.save(orderAddress);
        }

        //将订单放到mq中
        jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME,order.getId().toString(), (long) (15 * 60 * 1000));

        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setOrderId(order.getId());
        createOrderVo.setOrderNo(order.getOrderNo());
        createOrderVo.setOrderPrice(orderPrice);

        return createOrderVo;
    }


    private Order createOrder(CreateOrderDto dto,BigDecimal shippingFee,Double m,BigDecimal orderPrice,BigDecimal orderProdPrice,Shop shop){
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Date now = new Date();
        Date estimateArrivalStartTime = DateUtil.offsetHour(now,1);
        Date estimateArrivalEndTime = DateUtil.offsetMinute(now,90);

        Order order = new Order();
        Snowflake snowflake = IdUtil.getSnowflake();
        order.setOrderNo(snowflake.nextId());
        order.setOrderType(dto.getOrderType());
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setCustomerId(userId);
        order.setShopId(shop.getId());
        order.setShopName(shop.getShopName());
        order.setOrderPrice(orderPrice);
        order.setOrderProdPrice(orderProdPrice);
        order.setShippingFee(shippingFee);
        order.setEstimateArrivalStartTime(estimateArrivalStartTime);
        order.setEstimateArrivalEndTime(estimateArrivalEndTime);
        order.setOrderDistance(m.intValue());
        order.setRemark(dto.getRemark());
        order.setCreateBy(userId);
        order.setCreateTime(now);
        order.setUpdateBy(userId);
        order.setUpdateTime(now);
        order.setDelFlag(0);

        return order;
    }


    /**
     * 获取配送费
     * @param orderType 订单类型( 1外卖  2自提)
     * @return BigDecimal
     */
    private BigDecimal getShippingFee(Integer orderType){
        if (orderType == 1) {
            return new BigDecimal("4");
        } else {
            return   new BigDecimal("0");
        }
    }
}
