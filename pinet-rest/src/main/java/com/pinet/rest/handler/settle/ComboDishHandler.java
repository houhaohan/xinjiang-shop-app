package com.pinet.rest.handler.settle;

import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.FilterUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.dto.OrderComboDishSpecDto;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderProductSpecVo;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

            List<Long> shopProdSpecIds = cartComboDishSpecs.stream().map(CartComboDishSpec::getShopProdSpecId).collect(Collectors.toList());
            List<ShopProductSpec> shopProductSpecs = context.shopProductSpecService.listByIds(shopProdSpecIds);

            List<OrderProductSpecVo> orderProductSpecVoList  = cartComboDishSpecs.stream().map(s->{
                OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();

                ShopProductSpec shopProductSpec = FilterUtil.filter(shopProductSpecs, s.getShopProdSpecId(), ApiExceptionEnum.SPEC_NOT_EXISTS);
                orderProductSpecVo.setProdSkuId(shopProductSpec.getSkuId());
                orderProductSpecVo.setProdSkuName(shopProductSpec.getSkuName());
                orderProductSpecVo.setProdSpecName(s.getShopProdSpecName());
                orderProductSpecVo.setShopProdSpecId(s.getShopProdSpecId());
                return orderProductSpecVo;
            }).collect(Collectors.toList());
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

            List<Long> shopProdSpecIds = orderComboDishDto.getOrderComboDishSpecList().stream().map(OrderComboDishSpecDto::getShopProdSpecId).collect(Collectors.toList());
            List<ShopProductSpec> shopProductSpecs = context.shopProductSpecService.listByIds(shopProdSpecIds);

            List<OrderProductSpecVo> orderProductSpecs = orderComboDishDto.getOrderComboDishSpecList().stream().map(s->{
                OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();
                ShopProductSpec singleSpec = FilterUtil.filter(shopProductSpecs, s.getShopProdSpecId(), ApiExceptionEnum.SPEC_NOT_EXISTS);
                orderProductSpecVo.setShopProdSpecId(s.getShopProdSpecId());
                orderProductSpecVo.setProdSpecName(s.getShopProdSpecName());
                orderProductSpecVo.setProdSkuId(singleSpec.getSkuId());
                orderProductSpecVo.setProdSkuName(singleSpec.getSkuName());
                return orderProductSpecVo;
            }).collect(Collectors.toList());
            comboDishSpecVo.setOrderProductSpecs(orderProductSpecs);
            orderComboDishList.add(comboDishSpecVo);
        }

        orderProduct.setComboDishDetails(orderComboDishList);
        context.response = orderProduct;
    }


}
