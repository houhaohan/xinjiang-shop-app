package com.pinet.rest.handler;

import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.CartProductSpec;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.service.ICartProductSpecService;
import com.pinet.rest.service.IOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:21
 */
public class ComboDishCartHandler extends DishCartHandler {

    @Autowired
    private ICartProductSpecService cartProductSpecService;

    @Autowired
    private IOrderProductService orderProductService;

    @Override
    public OrderProduct getOrderProductByCartId(Long cartId, Long shopProdId, Integer prodNum, Integer orderType) {
        //套餐
        List<CartProductSpec> cartProductSpecs = cartProductSpecService.getComboByCartId(cartId);
        List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(shopProdId, prodNum, shopProdSpecIds,orderType);
        return orderProductService.getByQueryOrderProductBo(queryOrderProductBo);
     }
}
