package com.pinet.rest.service.impl;

import java.time.LocalDateTime;

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
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.dto.OrderPayDto;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.service.*;
import com.pinet.rest.service.common.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class OrderServicesImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Resource
    private OrdersMapper ordersMapper;

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

    @Resource
    private IOrderPayService orderPayService;

    @Resource
    private CommonService commonService;

    @Resource
    private IOrderProductSpecService orderProductSpecService;

    @Override
    public List<OrderListVo> orderList(OrderListDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<OrderListVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        IPage<OrderListVo> orderListVos = ordersMapper.selectOrderList(page, customerId);
        orderListVos.getRecords().forEach(k -> {
            k.setProdNum(k.getOrderProducts().size());
            k.setOrderStatusStr(OrderStatusEnum.getEnumByCode(k.getOrderStatus()));
            List<OrderProduct> orderProducts = orderProductService.getByOrderId(k.getOrderId());
            k.setOrderProducts(orderProducts);
        });
        return orderListVos.getRecords();
    }

    @Override
    public OrderDetailVo orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = ordersMapper.selectOrderDetail(orderId);
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
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds);
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
        return ordersMapper.countShopOrderMakeNum(shopId, queryDate);
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
        double m = LatAndLngUtils.getDistance(Double.parseDouble(dto.getLng()), Double.parseDouble(dto.getLat()),
                Double.parseDouble(shop.getLng()), Double.parseDouble(shop.getLat()));
        if (m > 10000D && dto.getOrderType() == 1) {
            throw new PinetException("店铺距离过远,无法配送");
        }

        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId());

            //删除购物车已购商品
            cartService.delCartByShopId(dto.getShopId(), userId);
        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds);
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        //减少已购商品的库存(第一版暂不加锁 后期考虑加乐观锁或redis锁)
        for (OrderProduct orderProduct : orderProducts) {
            for (OrderProductSpec orderProductSpec : orderProduct.getOrderProductSpecs()) {
                int res = shopProductSpecService.reduceStock(orderProductSpec.getShopProdSpecId(), orderProduct.getProdNum());
                if (res != 1) {
                    throw new PinetException("库存更新失败");
                }
            }
        }


        //计算订单总金额  和订单商品金额
        BigDecimal orderPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(shippingFee, BigDecimal::add);
        BigDecimal orderProdPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (orderPrice.compareTo(dto.getOrderPrice()) != 0) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }

        //创建订单基础信息
        Orders order = createOrder(dto, shippingFee, m, orderPrice, orderProdPrice, shop);
        //插入订单
        this.save(order);

        //插入订单商品  并设置订单号
        orderProducts.forEach(k -> {
            k.setOrderId(order.getId());
            //保存订单商品表
            orderProductService.save(k);

            //保存订单商品样式表
            k.getOrderProductSpecs().forEach(k1 -> k1.setOrderProdId(k.getId()));
            orderProductSpecService.saveBatch(k.getOrderProductSpecs());
        });


        //外卖订单插入订单地址表
        if (dto.getOrderType() == 1) {
            OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(dto.getCustomerAddressId());
            orderAddressService.save(orderAddress);
        }

        //将订单放到mq中
        jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME, order.getId().toString(), (long) (15 * 60 * 1000));

        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setOrderId(order.getId());
        createOrderVo.setOrderNo(order.getOrderNo());
        createOrderVo.setOrderPrice(orderPrice);

        return createOrderVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object orderPay(OrderPayDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        Orders orders = getById(dto.getOrderId());
        if (orders == null) {
            throw new PinetException("订单不存在");
        }

        if (orders.getOrderPrice().compareTo(dto.getOrderPrice()) != 0) {
            throw new PinetException("支付金额异常,请重新支付");
        }

        //根据不同支付渠道获取调用不同支付方法
        IPayService payService = SpringContextUtils.getBean(dto.getChannelId() + "_" + "service", IPayService.class);
        //封装PayParam
        PayParam param = new PayParam();
        param.setOpenId(dto.getOpenId());
        param.setOrderNo(orders.getOrderNo().toString());
        param.setPayPrice(dto.getOrderPrice());
        Object res = payService.pay(param);


        //构造orderPay
        OrderPay orderPay = new OrderPay();
        orderPay.setOrderId(orders.getId());
        orderPay.setOrderNo(orders.getOrderNo());
        orderPay.setCustomerId(customerId);
        orderPay.setPayStatus(1);
        orderPay.setOrderPrice(orders.getOrderPrice());
        orderPay.setPayPrice(dto.getOrderPrice());
        orderPay.setOpenId(dto.getOpenId());
        orderPay.setChannelId(dto.getChannelId());
        orderPay.setPayName(payService.getPayName());
        orderPay.setIp(IPUtils.getIpAddr());
        commonService.setDefInsert(orderPay);

        orderPayService.save(orderPay);

        return res;
    }


    private Orders createOrder(CreateOrderDto dto, BigDecimal shippingFee, Double m, BigDecimal orderPrice, BigDecimal orderProdPrice, Shop shop) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Date now = new Date();
        Date estimateArrivalStartTime = DateUtil.offsetHour(now, 1);
        Date estimateArrivalEndTime = DateUtil.offsetMinute(now, 90);

        Orders order = new Orders();
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
        commonService.setDefInsert(order);

        return order;
    }


    /**
     * 获取配送费
     *
     * @param orderType 订单类型( 1外卖  2自提)
     * @return BigDecimal
     */
    private BigDecimal getShippingFee(Integer orderType) {
        if (orderType == 1) {
            return new BigDecimal("4");
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * 分割商品样式id
     */
    private List<Long> splitShopProdSpecIds(String shopProdSpecIds) {
        String[] idArray = shopProdSpecIds.split(",");

        return Arrays.stream(idArray)
                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
    }
}
