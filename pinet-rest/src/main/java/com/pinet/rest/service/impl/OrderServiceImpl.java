package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Order;
import com.pinet.rest.entity.OrderAddress;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.enums.OrderStatusEnum;
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
        Page<OrderListVo> page = new Page<>(dto.getPageNum(),dto.getPageSize());

        IPage<OrderListVo> orderListVos = orderMapper.selectOrderList(page,customerId);
        orderListVos.getRecords().forEach(k->{
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
        if (orderDetailVo == null){
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
        BigDecimal shippingFee;
        if (dto.getOrderType() == 1){
            shippingFee = new BigDecimal("4");
        }else {
            shippingFee = new BigDecimal("0");
        }
        vo.setShippingFee(shippingFee);

        List<OrderProduct> orderProducts = orderProductService.getByCartAndShop(dto.getShopId());
        vo.setOrderProductBoList(orderProducts);

        //判断店铺是否营业
        if (!shopService.checkShopStatus(dto.getShopId())){
            throw new PinetException("店铺已经打烊了~");
        }

        vo.setOrderMakeCount(countShopOrderMakeNum(dto.getShopId()));
        //计算总金额
        BigDecimal orderPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(shippingFee,BigDecimal::add);
        vo.setOrderPrice(orderPrice);
        return vo;
    }

    @Override
    public Integer countShopOrderMakeNum(Long shopId) {
        Date date = new Date();
        Date queryDate = DateUtil.offset(date, DateField.DAY_OF_MONTH,-8);
        return orderMapper.countShopOrderMakeNum(shopId,queryDate);
    }
}
