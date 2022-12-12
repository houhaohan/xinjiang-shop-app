package com.pinet.rest.service.impl;

import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.bo.OrderProductBo;
import com.pinet.rest.mapper.OrderProductMapper;
import com.pinet.rest.service.IOrderProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements IOrderProductService {
    @Resource
    private OrderProductMapper orderProductMapper;

    @Override
    public List<OrderProductBo> getOrderProduct(Long orderId) {
        List<OrderProductBo> orderProductBoList = orderProductMapper.selectOrderProduct(orderId);
        return orderProductBoList;
    }
}
