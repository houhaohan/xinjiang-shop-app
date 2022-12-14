package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Order;
import com.pinet.rest.entity.OrderAddress;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.Shop;
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
import com.pinet.rest.service.IOrderAddressService;
import com.pinet.rest.service.IOrderProductService;
import com.pinet.rest.service.IOrderService;
import com.pinet.rest.service.IShopService;
import org.springframework.stereotype.Service;

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
        double km =  LatAndLngUtils.getDistance(Double.parseDouble(dto.getLng()),Double.parseDouble(dto.getLat()),
                Double.parseDouble(shop.getLng()),Double.parseDouble(shop.getLat()),2);
        if (km > 10D){
            throw new PinetException("店铺距离过远,无法配送");
        }



        return null;
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
