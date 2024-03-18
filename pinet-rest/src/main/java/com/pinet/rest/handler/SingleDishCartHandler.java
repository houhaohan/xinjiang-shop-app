package com.pinet.rest.handler;

import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.CartProductSpec;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.service.ICartProductSpecService;
import com.pinet.rest.service.IOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:21
 */
@Component
public class SingleDishCartHandler extends DishCartHandler {

    private ICartProductSpecService cartProductSpecService;
    private IOrderProductService orderProductService;

    public SingleDishCartHandler(ICartProductSpecService cartProductSpecService,
                                  IOrderProductService orderProductService){
        this.cartProductSpecService = cartProductSpecService;
        this.orderProductService = orderProductService;
    }

    @Override
    public OrderProduct getOrderProductByCartId(Long cartId, Long shopProdId, Integer prodNum, Integer orderType) {
        List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(cartId);
        List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(shopProdId, prodNum, shopProdSpecIds, orderType);
        return orderProductService.getByQueryOrderProductBo(queryOrderProductBo);
    }
}
