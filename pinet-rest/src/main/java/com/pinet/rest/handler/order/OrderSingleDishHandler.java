package com.pinet.rest.handler.order;

import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单 单品处理器
 */
@Component
@RequiredArgsConstructor
public class OrderSingleDishHandler extends  OrderDishAbstractHandler{
    private final ICartProductSpecService cartProductSpecService;
    private final IShopProductSpecService shopProductSpecService;
    private final IOrderProductService orderProductService;
    private final IOrderProductSpecService orderProductSpecService;

    /**
     * 单品购物车下单
     * 执行订单商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderProduct exectue(CartOrderProductRequest request){
        List<CartProductSpec> cartProductSpecList = cartProductSpecService.getByCartId(request.getCartId());
        List<Long> shopProdSpecIds = cartProductSpecList.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        BigDecimal unitPrice = shopProductSpecService.getPriceByIds(shopProdSpecIds);

        OrderProduct orderProduct = buildOrderProduct(request, unitPrice);
        if(Objects.equals(request.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProductService.save(orderProduct);

        //新增订单商品样式
        saveOrderProductSpecs(shopProdSpecIds,request.getOrderId(),orderProduct.getId());
        return orderProduct;
    }


    /**
     * 单品直接购买
     * 执行订单商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderProduct directOrder(DirectOrderRequest request){
        BigDecimal unitPrice = shopProductSpecService.getPriceByIds(request.getShopProdSpecIds());
        OrderProduct orderProduct = buildOrderProduct(request, unitPrice);
        if(Objects.equals(request.getOrderType(),OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProductService.save(orderProduct);

        saveOrderProductSpecs(request.getShopProdSpecIds(),request.getOrderId(),orderProduct.getId());
        return orderProduct;
    }

    /**
     * 新增订单商品样式
     * @param shopProdSpecIds
     * @param orderId
     * @param orderProductId
     */
    private void saveOrderProductSpecs(List<Long> shopProdSpecIds,Long orderId,Long orderProductId){
        List<OrderProductSpec> orderProductSpecList = shopProdSpecIds.stream().map(specId -> {
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            orderProductSpec.setOrderId(orderId);
            orderProductSpec.setOrderProdId(orderProductId);
            ShopProductSpec shopProductSpec = shopProductSpecService.getById(specId);
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(shopProductSpec.getSkuName());
            orderProductSpec.setShopProdSpecId(specId);
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            return orderProductSpec;
        }).collect(Collectors.toList());
        orderProductSpecService.saveBatch(orderProductSpecList);
    }


}
