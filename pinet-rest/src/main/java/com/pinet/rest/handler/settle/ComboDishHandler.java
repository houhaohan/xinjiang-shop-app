package com.pinet.rest.handler.settle;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.dto.OrderComboDishSpecDto;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderProductSpecVo;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 套餐处理器
 * @author: chengshuanghui
 * @date: 2024-03-22 16:48
 */
public class ComboDishHandler extends DishSettleAbstractHandler{


    public ComboDishHandler(DishSettleContext context){
        this.context =  context;
    }


    /**
     * 构建订单商品
     * @param shopProdId
     * @param prodNum
     * @return
     */
    @Override
    protected OrderProduct build(Long shopProdId,Integer prodNum){
        OrderProduct orderProduct = super.build(shopProdId, prodNum);
        Long unitPrice = context.kryComboGroupDetailService.getPriceByShopProdId(shopProdId);
        orderProduct.setProdUnitPrice(BigDecimalUtil.fenToYuan(unitPrice));
        orderProduct.setProdPrice(BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), new BigDecimal(orderProduct.getProdNum())));
        return orderProduct;
    }

    /**
     * 套餐购物车结算
     * @param cart
     * @return
     */
    @Override
    public void handler(Cart cart){
        OrderProduct orderProduct = build(cart.getShopProdId(), cart.getProdNum());
        List<CartComboDish> cartComboDishList = context.cartComboDishService.getByCartId(cart.getId());
        if(CollectionUtils.isEmpty(cartComboDishList)){
            return;
        }
        List<ComboDishSpecVo> list = new ArrayList<>();
        for (CartComboDish cartComboDish : cartComboDishList) {
            ComboDishSpecVo comboDishSpecVo = new ComboDishSpecVo();
            comboDishSpecVo.setShopProdId(cart.getShopProdId());
            comboDishSpecVo.setSingleDishId(cartComboDish.getShopProdId());
            ShopProduct singleShopProduct = context.shopProductService.getById(cartComboDish.getShopProdId());
            comboDishSpecVo.setSingleDishName(singleShopProduct.getProductName());
            List<CartComboDishSpec> cartComboDishSpecs = context.cartComboDishSpecService.getByCartIdAndProdId(cart.getId(), cartComboDish.getShopProdId());

            List<OrderProductSpecVo> orderProductSpecVoList = new ArrayList<>();
            for (CartComboDishSpec cartComboDishSpec : cartComboDishSpecs) {
                OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();
                ShopProductSpec shopProductSpec = context.shopProductSpecService.getById(cartComboDishSpec.getShopProdSpecId());
                orderProductSpecVo.setProdSkuId(shopProductSpec.getSkuId());
                orderProductSpecVo.setProdSkuName(shopProductSpec.getSkuName());
                orderProductSpecVo.setProdSpecName(cartComboDishSpec.getShopProdSpecName());
                orderProductSpecVo.setShopProdSpecId(cartComboDishSpec.getShopProdSpecId());
                orderProductSpecVoList.add(orderProductSpecVo);
            }
            comboDishSpecVo.setOrderProductSpecs(orderProductSpecVoList);
            list.add(comboDishSpecVo);
        }
        orderProduct.setComboDishDetails(list);
        context.response = orderProduct;
    }


    /**
     * 套餐直接结算
     */
    @Override
    public void handler() {
        OrderProduct orderProduct = build(context.request.getShopProdId(), context.request.getProdNum());

        List<OrderComboDishDto> orderComboDishDtoList = context.request.getOrderComboDishList();
        if(CollectionUtils.isEmpty(orderComboDishDtoList)){
            return;
        }
        List<ComboDishSpecVo> orderComboDishList = new ArrayList<>(orderComboDishDtoList.size());
        for(OrderComboDishDto orderComboDishDto : orderComboDishDtoList){
            ComboDishSpecVo comboDishSpecVo = new ComboDishSpecVo();
            comboDishSpecVo.setShopProdId(orderComboDishDto.getShopProdId());
            comboDishSpecVo.setSingleDishId(orderComboDishDto.getSingleProdId());
            ShopProduct singleProduct = context.shopProductService.getById(orderComboDishDto.getSingleProdId());
            comboDishSpecVo.setSingleDishName(singleProduct.getProductName());

            List<OrderProductSpecVo> orderProductSpecs = new ArrayList<>();
            for(OrderComboDishSpecDto orderComboDishSpecDto : orderComboDishDto.getOrderComboDishSpecList()) {
                OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();
                ShopProductSpec singleSpec = context.shopProductSpecService.getById(orderComboDishSpecDto.getShopProdSpecId());
                orderProductSpecVo.setShopProdSpecId(orderComboDishSpecDto.getShopProdSpecId());
                orderProductSpecVo.setProdSpecName(orderComboDishSpecDto.getShopProdSpecName());
                orderProductSpecVo.setProdSkuId(singleSpec.getSkuId());
                orderProductSpecVo.setProdSkuName(singleSpec.getSkuName());
                orderProductSpecs.add(orderProductSpecVo);
            }
            comboDishSpecVo.setOrderProductSpecs(orderProductSpecs);
            orderComboDishList.add(comboDishSpecVo);
        }

        orderProduct.setComboDishDetails(orderComboDishList);
        context.response = orderProduct;
    }





}
