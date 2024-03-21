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
public class OrderSingleDishHandler {
    private final ICartProductSpecService cartProductSpecService;
    private final IShopProductService shopProductService;
    private final IShopProductSpecService shopProductSpecService;
    private final IOrderProductService orderProductService;
    private final IOrderProductSpecService orderProductSpecService;

    /**
     * 执行订单商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderProduct exectue(OrderProductRequest request){
        List<CartProductSpec> cartProductSpecList = cartProductSpecService.getByCartId(request.getCartId());
        List<Long> shopProdSpecIds = cartProductSpecList.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        BigDecimal unitPrice = shopProductSpecService.getPriceByIds(shopProdSpecIds);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(request.getOrderId());
        orderProduct.setShopProdId(request.getShopProdId());
        orderProduct.setDishId(request.getDishId());
        orderProduct.setProdUnitPrice(unitPrice);
        orderProduct.setProdNum(request.getProdNum());
        orderProduct.setProdPrice(BigDecimalUtil.multiply(unitPrice,orderProduct.getProdNum(), RoundingMode.HALF_UP));

        ShopProduct shopProduct = shopProductService.getById(request.getShopProdId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setUnit(shopProduct.getUnit());
        orderProduct.setProdImg(shopProduct.getProductImg());
        if(request.isCalculate()){
            orderProduct.setCommission(BigDecimalUtil.multiply(orderProduct.getProdPrice(),0.1));
        }
        if(Objects.equals(request.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProductService.save(orderProduct);

        List<OrderProductSpec> orderProductSpecList = cartProductSpecList.stream().map(spec -> {
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            orderProductSpec.setOrderId(request.getOrderId());
            orderProductSpec.setOrderProdId(shopProduct.getId());

            ShopProductSpec shopProductSpec = shopProductSpecService.getById(spec.getShopProdSpecId());
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(shopProductSpec.getSkuName());
            orderProductSpec.setShopProdSpecId(spec.getShopProdSpecId());
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            return orderProductSpec;
        }).collect(Collectors.toList());

        orderProductSpecService.saveBatch(orderProductSpecList);
        return orderProduct;
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderProduct directOrder(DirectOrderRequest request){
        BigDecimal unitPrice = shopProductSpecService.getPriceByIds(request.getShopProdSpecIds());

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(request.getOrderId());
        orderProduct.setShopProdId(request.getShopProdId());
        orderProduct.setDishId(request.getDishId());
        orderProduct.setProdUnitPrice(unitPrice);
        orderProduct.setProdNum(request.getProdNum());
        orderProduct.setProdPrice(BigDecimalUtil.multiply(unitPrice,orderProduct.getProdNum(), RoundingMode.HALF_UP));
        orderProduct.setProdName(request.getProductName());
        orderProduct.setUnit(request.getUnit());
        orderProduct.setProdImg(request.getProductImg());
        if(request.isCalculate()){
            orderProduct.setCommission(BigDecimalUtil.multiply(orderProduct.getProdPrice(),0.1));
        }
        if(Objects.equals(request.getOrderType(),OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProductService.save(orderProduct);


        List<Long> shopProdSpecIds = request.getShopProdSpecIds();
        List<OrderProductSpec> orderProductSpecs = new ArrayList<>(shopProdSpecIds.size());
        for(Long shopProdSpecId : shopProdSpecIds){
            ShopProductSpec shopProductSpec = shopProductSpecService.getById(shopProdSpecId);
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(shopProductSpec.getSkuName());
            orderProductSpec.setShopProdSpecId(shopProdSpecId);
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            orderProductSpec.setOrderId(request.getOrderId());
            orderProductSpec.setOrderProdId(orderProduct.getId());
            orderProductSpecs.add(orderProductSpec);
        }
        orderProductSpecService.saveBatch(orderProductSpecs);
        return orderProduct;
    }


}
