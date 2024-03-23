package com.pinet.rest.handler.order;


import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.request.CartOrderProductRequest;
import com.pinet.rest.entity.request.DirectOrderRequest;
import com.pinet.rest.entity.request.OrderProductRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description 订单套餐菜处理器
 * @author chengshuanghui
 * @data 2024-03-21 15:00
 */
public class OrderComboDishHandler extends OrderDishAbstractHandler{

    public OrderComboDishHandler(OrderDishContext context){
        this.context = context;
    }


    @Override
    protected OrderProduct build(OrderProductRequest request, BigDecimal unitPrice) {
        OrderProduct orderProduct = super.build(request, unitPrice);
        if(Objects.equals(request.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE,orderProduct.getProdNum(),RoundingMode.HALF_UP));
        }
        context.orderProductService.save(orderProduct);
        return orderProduct;
    }

    /**
     * 购物车购买
     * 执行订单套餐商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderProduct execute(CartOrderProductRequest request){
        Long unitPrice = context.kryComboGroupDetailService.getPriceByShopProdId(request.getShopProdId());
        OrderProduct orderProduct = build(request, BigDecimalUtil.fenToYuan(unitPrice));

        List<CartComboDish> cartComboDishList = context.cartComboDishService.getByCartId(request.getCartId());
        for(CartComboDish cartComboDish : cartComboDishList){
            OrderComboDish orderComboDish = new OrderComboDish();
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDish.setShopProdId(cartComboDish.getShopProdId());
            ShopProduct singleProduct = context.shopProductService.getById(cartComboDish.getShopProdId());
            orderComboDish.setDishId(singleProduct.getProdId());
            orderComboDish.setShopProdId(request.getShopProdId());
            orderComboDish.setSingleProdId(singleProduct.getId());
            orderComboDish.setProdName(singleProduct.getProductName());
            orderComboDish.setUnit(singleProduct.getUnit());
            orderComboDish.setUnitId(singleProduct.getUnitId());
            orderComboDish.setQuantity(request.getProdNum());
            orderComboDish.setImageUrl(singleProduct.getProductImg());
            context.orderComboDishService.save(orderComboDish);

            List<CartComboDishSpec> cartComboDishSpecList = context.cartComboDishSpecService.getByCartIdAndProdId(request.getCartId(), singleProduct.getId());
            List<OrderComboDishSpec> orderComboDishSpecList = cartComboDishSpecList.stream().map(spec -> {
                OrderComboDishSpec orderComboDishSpec = new OrderComboDishSpec();
                ShopProductSpec shopProductSpec = context.shopProductSpecService.getById(spec.getShopProdSpecId());
                orderComboDishSpec.setAddPrice(shopProductSpec.getPrice());
                orderComboDishSpec.setOrderComboDishId(orderComboDish.getId());
                orderComboDishSpec.setShopProdSpecId(spec.getShopProdSpecId());
                orderComboDishSpec.setShopProdSpecName(spec.getShopProdSpecName());
                return orderComboDishSpec;
            }).collect(Collectors.toList());
            context.orderComboDishSpecService.saveBatch(orderComboDishSpecList);

        }
        return orderProduct;
    }


    /**
     * 直接购买
     * 执行订单套餐商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderProduct execute(DirectOrderRequest request){
        Long unitPrice = context.kryComboGroupDetailService.getPriceByShopProdId(request.getShopProdId());
        OrderProduct orderProduct = build(request, BigDecimalUtil.fenToYuan(unitPrice));

        List<OrderComboDishDto> comboDishDtoList = request.getComboDishDtoList();
        for(OrderComboDishDto comboDishDto : comboDishDtoList){
            OrderComboDish orderComboDish = new OrderComboDish();
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDish.setShopProdId(comboDishDto.getShopProdId());
            ShopProduct singleProduct = context.shopProductService.getById(comboDishDto.getSingleProdId());
            orderComboDish.setDishId(singleProduct.getProdId());
            orderComboDish.setProdName(singleProduct.getProductName());
            orderComboDish.setUnit(singleProduct.getUnit());
            orderComboDish.setUnitId(singleProduct.getUnitId());
            orderComboDish.setQuantity(request.getProdNum());
            orderComboDish.setImageUrl(singleProduct.getProductImg());
            orderComboDish.setSingleProdId(singleProduct.getId());
            orderComboDish.setShopProdId(request.getShopProdId());
            orderComboDish.setOrderId(request.getOrderId());
            context.orderComboDishService.save(orderComboDish);

            List<OrderComboDishSpec> orderComboDishSpecList = comboDishDto.getOrderComboDishSpecList().stream().map(spec -> {
                OrderComboDishSpec orderComboDishSpec = new OrderComboDishSpec();
                ShopProductSpec shopProductSpec = context.shopProductSpecService.getById(spec.getShopProdSpecId());
                orderComboDishSpec.setOrderComboDishId(orderComboDish.getId());
                orderComboDishSpec.setAddPrice(shopProductSpec.getPrice());
                orderComboDishSpec.setShopProdSpecId(spec.getShopProdSpecId());
                orderComboDishSpec.setShopProdSpecName(spec.getShopProdSpecName());
                return orderComboDishSpec;
            }).collect(Collectors.toList());
            context.orderComboDishSpecService.saveBatch(orderComboDishSpecList);
        }

        return orderProduct;
    }

}
