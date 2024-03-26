package com.pinet.rest.handler.order;


import com.pinet.core.constants.OrderConstant;
import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.FilterUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.dto.OrderComboDishSpecDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.request.CartOrderProductRequest;
import com.pinet.rest.entity.request.DirectOrderRequest;
import com.pinet.rest.entity.request.OrderProductRequest;
import com.pinet.rest.entity.vo.ComboSingleProductSpecVo;
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

        List<Long> shopProdIds = cartComboDishList.stream().map(CartComboDish::getShopProdId).collect(Collectors.toList());
        List<ShopProduct> shopProducts = context.shopProductService.listByIds(shopProdIds);

        for(CartComboDish cartComboDish : cartComboDishList){
            OrderComboDish orderComboDish = new OrderComboDish();
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDish.setShopProdId(cartComboDish.getShopProdId());
            ShopProduct singleProduct = FilterUtil.filter(shopProducts, cartComboDish.getShopProdId(),ApiExceptionEnum.PROD_NOT_EXISTS);
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
            List<Long> shopProdSpecIds = cartComboDishSpecList.stream().map(CartComboDishSpec::getShopProdSpecId).collect(Collectors.toList());
            List<ComboSingleProductSpecVo> shopProductSpecs = context.kryComboGroupDetailService.getSpecByShopProdSpecIds(shopProdSpecIds, singleProduct.getShopId());

            List<OrderComboDishSpec> orderComboDishSpecList = shopProdSpecIds.stream().map(specId -> {
                OrderComboDishSpec orderComboDishSpec = new OrderComboDishSpec();
                ComboSingleProductSpecVo spec =  shopProductSpecs.stream()
                        .filter(o->Objects.equals(o.getShopProdSpecId(),specId))
                        .findFirst()
                        .orElseThrow(()->new PinetException(ApiExceptionEnum.SPEC_NOT_EXISTS));
                orderComboDishSpec.setAddPrice(BigDecimalUtil.fenToYuan(spec.getAddPrice()));
                orderComboDishSpec.setOrderComboDishId(orderComboDish.getId());
                orderComboDishSpec.setShopProdSpecId(specId);
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

        List<Long> singleProdIds = request.getComboDishDtoList().stream().map(OrderComboDishDto::getSingleProdId).collect(Collectors.toList());
        List<ShopProduct> shopProducts = context.shopProductService.listByIds(singleProdIds);

        List<OrderComboDishDto> comboDishDtoList = request.getComboDishDtoList();
        for(OrderComboDishDto comboDishDto : comboDishDtoList){
            OrderComboDish orderComboDish = new OrderComboDish();
            orderComboDish.setOrderId(request.getOrderId());
            orderComboDish.setShopProdId(comboDishDto.getShopProdId());
            ShopProduct singleProduct = FilterUtil.filter(shopProducts, comboDishDto.getSingleProdId(),ApiExceptionEnum.PROD_NOT_EXISTS);
            orderComboDish.setDishId(singleProduct.getProdId());
            orderComboDish.setProdName(singleProduct.getProductName());
            orderComboDish.setUnit(singleProduct.getUnit());
            orderComboDish.setUnitId(singleProduct.getUnitId());
            orderComboDish.setQuantity(request.getProdNum());
            orderComboDish.setImageUrl(singleProduct.getProductImg());
            orderComboDish.setSingleProdId(singleProduct.getId());
            context.orderComboDishService.save(orderComboDish);

            List<Long> shopProdSpecIds = comboDishDto.getOrderComboDishSpecList().stream().map(OrderComboDishSpecDto::getShopProdSpecId).collect(Collectors.toList());
            List<ComboSingleProductSpecVo> shopProductSpecs = context.kryComboGroupDetailService.getSpecByShopProdSpecIds(shopProdSpecIds, singleProduct.getShopId());
            List<OrderComboDishSpec> orderComboDishSpecList = shopProdSpecIds.stream().map(specId -> {
                OrderComboDishSpec orderComboDishSpec = new OrderComboDishSpec();
                ComboSingleProductSpecVo spec = shopProductSpecs.stream()
                        .filter(o -> Objects.equals(o.getShopProdSpecId(), specId))
                        .findFirst()
                        .orElseThrow(() -> new PinetException(ApiExceptionEnum.SPEC_NOT_EXISTS));
                orderComboDishSpec.setOrderComboDishId(orderComboDish.getId());
                orderComboDishSpec.setAddPrice(BigDecimalUtil.fenToYuan(spec.getAddPrice()));
                orderComboDishSpec.setShopProdSpecId(spec.getShopProdSpecId());
                orderComboDishSpec.setShopProdSpecName(spec.getShopProdSpecName());
                return orderComboDishSpec;
            }).collect(Collectors.toList());
            context.orderComboDishSpecService.saveBatch(orderComboDishSpecList);
        }

        return orderProduct;
    }


}
