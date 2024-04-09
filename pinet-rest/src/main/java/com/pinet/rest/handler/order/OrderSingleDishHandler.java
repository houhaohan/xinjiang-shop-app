package com.pinet.rest.handler.order;

import com.pinet.core.constants.OrderConstant;
import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.FilterUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.SideDishGroupDTO;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.request.CartOrderProductRequest;
import com.pinet.rest.entity.request.DirectOrderRequest;
import com.pinet.rest.entity.request.OrderProductRequest;
import com.pinet.rest.entity.vo.CartSideVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chengshuanghui
 * @description 订单 单品处理器
 * @data 2024-03-21 15:00
 */
public class OrderSingleDishHandler extends OrderDishAbstractHandler {

    public OrderSingleDishHandler(OrderDishContext context) {
        this.context = context;
    }

    @Override
    protected OrderProduct build(OrderProductRequest request, BigDecimal unitPrice,BigDecimal sidePrice) {
        OrderProduct orderProduct = super.build(request, unitPrice,sidePrice);
        if (Objects.equals(request.getOrderType(), OrderTypeEnum.SELF_PICKUP.getCode())) {
            return orderProduct;
        }
        if(Objects.equals(context.dishType, DishType.COMBO)){
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE, orderProduct.getProdNum(), RoundingMode.HALF_UP));
        }else {
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE, orderProduct.getProdNum(), RoundingMode.HALF_UP));
        }
        return orderProduct;
    }

    /**
     * 单品购物车下单
     * 执行订单商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderProduct execute(CartOrderProductRequest request) {
        List<CartProductSpec> cartProductSpecList = context.cartProductSpecService.getByCartId(request.getCartId());
        List<Long> shopProdSpecIds = cartProductSpecList.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        BigDecimal unitPrice = context.shopProductSpecService.getPriceByIds(shopProdSpecIds);
        List<CartSideVO> cartSideList = context.cartSideService.getByCartId(request.getCartId());
        Long sidePrice = cartSideList.stream().map(side-> side.getAddPrice() * side.getQuantity()).reduce(0L, Long::sum);
        OrderProduct orderProduct = build(request, unitPrice,BigDecimalUtil.fenToYuan(sidePrice));
        context.orderProductService.save(orderProduct);

        //新增订单商品样式
        saveOrderProductSpecs(shopProdSpecIds, request.getOrderId(), orderProduct.getId());
        //新增订单小料
        saveOrderSide(request.getCartId(),request.getOrderId(),orderProduct.getId());
        return orderProduct;
    }


    /**
     * 单品直接购买
     * 执行订单商品入库
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderProduct execute(DirectOrderRequest request) {
        BigDecimal unitPrice = context.shopProductSpecService.getPriceByIds(request.getShopProdSpecIds());
        BigDecimal sidePrice = request.getSideDishGroupList().stream().map(side -> BigDecimalUtil.multiply(side.getAddPrice(), new BigDecimal(side.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        OrderProduct orderProduct = build(request, unitPrice, sidePrice);
        context.orderProductService.save(orderProduct);

        saveOrderProductSpecs(request.getShopProdSpecIds(), request.getOrderId(), orderProduct.getId());
        //新增订单小料
        saveOrderSide(request.getSideDishGroupList(),request.getOrderId(),orderProduct.getId());
        return orderProduct;
    }

    /**
     * 新增订单商品样式
     *
     * @param shopProdSpecIds
     * @param orderId
     * @param orderProductId
     */
    private void saveOrderProductSpecs(List<Long> shopProdSpecIds, Long orderId, Long orderProductId) {
        List<ShopProductSpec> shopProductSpecs = context.shopProductSpecService.listByIds(shopProdSpecIds);
        List<OrderProductSpec> orderProductSpecList = shopProdSpecIds.stream().map(specId -> {
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            orderProductSpec.setCreateBy(shopProductSpecs.get(0).getCreateBy());
            orderProductSpec.setOrderId(orderId);
            orderProductSpec.setOrderProdId(orderProductId);
            ShopProductSpec shopProductSpec = FilterUtil.filter(shopProductSpecs, specId, ApiExceptionEnum.SPEC_NOT_EXISTS);
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(shopProductSpec.getSkuName());
            orderProductSpec.setShopProdSpecId(specId);
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            return orderProductSpec;
        }).collect(Collectors.toList());
        context.orderProductSpecService.saveBatch(orderProductSpecList);
    }

    /**
     * 购物车购买添加订单小料
     * @param cartId
     * @param orderId
     * @param orderProductId
     */
    private void saveOrderSide(Long cartId,Long orderId,Long orderProductId){
        List<CartSideVO> list = context.cartSideService.getByCartId(cartId);
        if(CollectionUtils.isEmpty(list)){
            return;
        }

        List<OrderSide> orderSideList = list.stream().map(side -> {
            OrderSide orderSide = new OrderSide();
            orderSide.setOrderId(orderId);
            orderSide.setOrderProdId(orderProductId);
            orderSide.setAddPrice(BigDecimalUtil.fenToYuan(side.getAddPrice()));
            orderSide.setQuantity(side.getQuantity());
            orderSide.setSideDetailId(side.getSideDetailId());
            orderSide.setTotalPrice(BigDecimalUtil.multiply(orderSide.getAddPrice(),new BigDecimal(orderSide.getQuantity())));
            return orderSide;
        }).collect(Collectors.toList());
        context.orderSideService.saveBatch(orderSideList);
    }


    /**
     * 直接购买添加订单小料
     * @param sideDishGroupList 小料明细
     * @param orderId
     * @param orderProductId
     */
    private void saveOrderSide(List<SideDishGroupDTO> sideDishGroupList,Long orderId,Long orderProductId){
        if(CollectionUtils.isEmpty(sideDishGroupList)){
            return;
        }
        List<OrderSide> orderSideList = sideDishGroupList.stream().map(side -> {
            OrderSide orderSide = new OrderSide();
            orderSide.setOrderId(orderId);
            orderSide.setOrderProdId(orderProductId);
            orderSide.setAddPrice(side.getAddPrice());
            orderSide.setQuantity(side.getQuantity());
            orderSide.setSideDetailId(side.getId());
            orderSide.setTotalPrice(BigDecimalUtil.multiply(orderSide.getAddPrice(),new BigDecimal(orderSide.getQuantity())));
            return orderSide;
        }).collect(Collectors.toList());
        context.orderSideService.saveBatch(orderSideList);
    }

}
