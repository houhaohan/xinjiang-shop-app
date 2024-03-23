package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderProductDto;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.mapper.OrderProductMapper;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements IOrderProductService {

    private final OrderProductMapper orderProductMapper;


    @Override
    public List<OrderProduct> getByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(orderId);
        orderProducts.forEach(k -> k.setOrderProductSpecStr(k.getOrderProductSpecs().stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(","))));
        return orderProducts;
    }

    @Override
    public List<OrderProduct> getComboByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.getComboByOrderId(orderId);
        for(OrderProduct orderProduct : orderProducts){
            String orderProductSpecStr = orderProduct.getComboDishDetails().stream().map(ComboDishSpecVo::getSingleDishName).collect(Collectors.joining(","));
            orderProduct.setOrderProductSpecStr(orderProductSpecStr);
        }
        return orderProducts;
    }


    @Override
    public List<OrderProductDto> selectByOrderId(Long orderId) {
        return orderProductMapper.getByOrderId(orderId);
    }

}
