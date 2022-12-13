package com.pinet.rest.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import com.github.pagehelper.PageHelper;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.Order;
import com.pinet.rest.entity.OrderAddress;
import com.pinet.rest.entity.bo.OrderProductBo;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.mapper.OrderMapper;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.service.IOrderAddressService;
import com.pinet.rest.service.IOrderProductService;
import com.pinet.rest.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public List<OrderListVo> orderList(OrderListDto dto) {
        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        List<OrderListVo> orderListVos = orderMapper.selectOrderList(customerId);
        orderListVos.forEach(k->{
            List<OrderProductBo> orderProductBoList = orderProductService.getOrderProduct(k.getOrderId());
            k.setOrderProductBoList(orderProductBoList);
            k.setProdNum(orderProductBoList.size());
            k.setOrderStatusStr(OrderStatusEnum.getEnumByCode(k.getOrderStatus()));
        });
        return orderListVos;
    }

    @Override
    public OrderDetailVo orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = orderMapper.selectOrderDetail(orderId);
        if (orderDetailVo == null){
            throw new PinetException("订单不存在");
        }
        orderDetailVo.setOrderProductBoList(orderProductService.getOrderProduct(orderId));

        OrderAddress orderAddress = orderAddressService.getOrderAddress(orderId);

        orderDetailVo.setAddress(orderAddress.getAddress());
        //脱敏
        orderDetailVo.setName(DesensitizedUtil.chineseName(orderAddress.getName()));
        orderDetailVo.setTel(DesensitizedUtil.mobilePhone(orderAddress.getTel()));
        return orderDetailVo;
    }
}
