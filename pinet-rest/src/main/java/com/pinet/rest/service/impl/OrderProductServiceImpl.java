package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderProductDto;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderSideVo;
import com.pinet.rest.mapper.OrderProductMapper;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
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
    private final IOrderSideService orderSideService;


    @Override
    public List<OrderProduct> getByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(orderId);
        orderProducts.forEach(k -> k.setOrderProductSpecStr(k.getOrderProductSpecs().stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(","))));
        List<OrderSideVo> orderSideList = orderSideService.getByOrderId(orderId);
        if(!CollectionUtils.isEmpty(orderSideList)){
            orderProducts.forEach(orderProduct -> {
                StringBuffer sb = new StringBuffer();
                String str = orderSideList.stream()
                        .filter(o-> Objects.equals(o.getOrderProdId(),orderProduct.getId()))
                        .map(side -> {
                            String sideStr = sb.append(",")
                                    .append(side.getSideDishName())
                                    .append("x")
                                    .append(side.getQuantity())
                                    .append("(+")
                                    .append(BigDecimalUtil.stripTrailingZeros(side.getAddPrice()))
                                    .append("元)").toString();
                            sb.setLength(0);
                            return sideStr;
                        }).collect(Collectors.joining(","));
                orderProduct.setOrderProductSpecStr(orderProduct.getOrderProductSpecStr() + str);
            });
        }
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
