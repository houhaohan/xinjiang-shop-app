package com.pinet.rest.handler.order;


import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单套餐菜处理器
 */
@Component
@RequiredArgsConstructor
public class OrderComboDishHandler extends OrderDishAbstractHandler{
    private final IOrderComboDishService orderComboDishService;
    private final IOrderComboDishSpecService orderComboDishSpecService;
    private final ICartComboDishService cartComboDishService;
    private final ICartComboDishSpecService cartComboDishSpecService;
    private final IOrderProductService orderProductService;
    private final IKryComboGroupDetailService kryComboGroupDetailService;
    private final IShopProductService shopProductService;
    private final IShopProductSpecService shopProductSpecService;


    /**
     * 购物车购买
     * 执行订单套餐商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderProduct exectue(CartOrderProductRequest request){
        Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(request.getShopProdId());
        OrderProduct orderProduct = buildOrderProduct(request, BigDecimalUtil.fenToYuan(unitPrice));

        if(Objects.equals(request.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProductService.save(orderProduct);

        List<CartComboDish> cartComboDishList = cartComboDishService.getByCartId(request.getCartId());
        for(CartComboDish cartComboDish : cartComboDishList){
            OrderComboDish orderComboDish = new OrderComboDish();
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDish.setShopProdId(cartComboDish.getShopProdId());
            ShopProduct singleProduct = shopProductService.getById(cartComboDish.getShopProdId());
            orderComboDish.setDishId(singleProduct.getProdId());
            orderComboDish.setShopProdId(request.getShopProdId());
            orderComboDish.setSingleProdId(singleProduct.getId());
            orderComboDish.setProdName(singleProduct.getProductName());
            orderComboDish.setUnit(singleProduct.getUnit());
            orderComboDish.setUnitId(singleProduct.getUnitId());
            orderComboDish.setQuantity(request.getProdNum());
            orderComboDish.setImageUrl(singleProduct.getProductImg());
            orderComboDishService.save(orderComboDish);

            List<CartComboDishSpec> cartComboDishSpecList = cartComboDishSpecService.getByCartIdAndProdId(request.getCartId(), singleProduct.getId());
            List<OrderComboDishSpec> orderComboDishSpecList = cartComboDishSpecList.stream().map(spec -> {
                OrderComboDishSpec orderComboDishSpec = new OrderComboDishSpec();
                ShopProductSpec shopProductSpec = shopProductSpecService.getById(spec.getShopProdSpecId());
                orderComboDishSpec.setAddPrice(shopProductSpec.getPrice());
                orderComboDishSpec.setOrderComboDishId(orderComboDish.getId());
                orderComboDishSpec.setShopProdSpecId(spec.getShopProdSpecId());
                orderComboDishSpec.setShopProdSpecName(spec.getShopProdSpecName());
                return orderComboDishSpec;
            }).collect(Collectors.toList());
            orderComboDishSpecService.saveBatch(orderComboDishSpecList);

        }
        return orderProduct;
    }


    /**
     * 直接购买
     * 执行订单套餐商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderProduct directOrder(DirectOrderRequest request){
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(request.getOrderId());
        orderProduct.setShopProdId(request.getShopProdId());
        orderProduct.setDishId(request.getDishId());
        Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(request.getShopProdId());
        orderProduct.setProdUnitPrice(BigDecimalUtil.fenToYuan(unitPrice));
        orderProduct.setProdNum(request.getProdNum());
        orderProduct.setProdPrice(BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(),orderProduct.getProdNum(), RoundingMode.HALF_UP));
        orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE,request.getProdNum(),RoundingMode.HALF_UP));

        orderProduct.setProdName(request.getProdName());
        orderProduct.setUnit(request.getUnit());
        orderProduct.setProdImg(request.getProdImg());
        if(request.isCalculate()){
            orderProduct.setCommission(BigDecimalUtil.multiply(orderProduct.getProdPrice(),0.1));
        }
        buildOrderProduct(request,BigDecimalUtil.fenToYuan(unitPrice));

        if(Objects.equals(request.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        orderProductService.save(orderProduct);

        List<OrderComboDishDto> comboDishDtoList = request.getComboDishDtoList();
        for(OrderComboDishDto comboDishDto : comboDishDtoList){
            OrderComboDish orderComboDish = new OrderComboDish();
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDish.setShopProdId(comboDishDto.getShopProdId());
            ShopProduct singleProduct = shopProductService.getById(comboDishDto.getShopProdId());
            orderComboDish.setDishId(singleProduct.getProdId());
            orderComboDish.setProdName(singleProduct.getProductName());
            orderComboDish.setUnit(singleProduct.getUnit());
            orderComboDish.setUnitId(singleProduct.getUnitId());
            orderComboDish.setQuantity(request.getProdNum());
            orderComboDish.setImageUrl(singleProduct.getProductImg());
            orderComboDish.setSingleProdId(singleProduct.getId());
            orderComboDish.setShopProdId(request.getShopProdId());
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDishService.save(orderComboDish);

            List<OrderComboDishSpec> orderComboDishSpecList = comboDishDto.getOrderComboDishSpecList().stream().map(spec -> {
                OrderComboDishSpec orderComboDishSpec = new OrderComboDishSpec();
                ShopProductSpec shopProductSpec = shopProductSpecService.getById(spec.getShopProdSpecId());
                orderComboDishSpec.setOrderComboDishId(orderComboDish.getId());
                orderComboDishSpec.setAddPrice(shopProductSpec.getPrice());
                orderComboDishSpec.setShopProdSpecId(spec.getShopProdSpecId());
                orderComboDishSpec.setShopProdSpecName(spec.getShopProdSpecName());
                return orderComboDishSpec;
            }).collect(Collectors.toList());
            orderComboDishSpecService.saveBatch(orderComboDishSpecList);
        }

        return orderProduct;
    }

}
