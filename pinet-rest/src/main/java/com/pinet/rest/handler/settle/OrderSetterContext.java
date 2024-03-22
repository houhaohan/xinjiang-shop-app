package com.pinet.rest.handler.settle;

import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @description: 订单结算上下文
 * @author: chengshuanghui
 * @date: 2024-03-22 17:55
 */
@RequiredArgsConstructor
@Component
public class OrderSetterContext {
    protected final IShopProductService shopProductService;
    protected final ICartService cartService;
    protected final IShopProductSpecService shopProductSpecService;
    protected final IKryComboGroupDetailService kryComboGroupDetailService;
    protected final ICartComboDishService cartComboDishService;
    protected final ICartComboDishSpecService cartComboDishSpecService;
    protected final ICartProductSpecService cartProductSpecService;
    protected final IProductSkuService productSkuService;
}
