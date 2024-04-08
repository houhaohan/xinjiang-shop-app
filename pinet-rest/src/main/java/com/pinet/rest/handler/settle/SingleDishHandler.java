package com.pinet.rest.handler.settle;

import cn.hutool.core.convert.Convert;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.SideDishGroupDTO;
import com.pinet.rest.entity.vo.CartSideVO;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 单品处理器
 * @author: chengshuanghui
 * @date: 2024-03-22 16:48
 */
public class SingleDishHandler extends DishSettleAbstractHandler{

    public SingleDishHandler(DishSettleContext context){
        this.context = context;
    }

    /**
     * 单品购物车结算
     * @param cart
     * @return
     */
    @Override
    public void handler(Cart cart){
        List<CartProductSpec> cartProductSpecs = context.cartProductSpecService.getByCartId(cart.getId());
        List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());

        OrderProduct orderProduct = build(cart.getShopProdId(), cart.getProdNum());
        buildSpec(shopProdSpecIds,orderProduct);
        buildSide(cart.getId(),orderProduct);
        context.response = orderProduct;
    }


    /**
     * 单品直接结算
     */
    @Override
    public void handler() {
        OrderProduct orderProduct = build(context.request.getShopProdId(), context.request.getProdNum());

        List<Long> shopProdSpecIds = Convert.toList(Long.class, context.request.getShopProdSpecIds());
        buildSpec(shopProdSpecIds,orderProduct);
        buildSide(orderProduct);
        context.response = orderProduct;
    }


    /**
     * 设置商品规格
     * @param shopProdSpecIds
     * @param orderProduct
     */
    private void buildSpec(List<Long> shopProdSpecIds,OrderProduct orderProduct){
        if(CollectionUtils.isEmpty(shopProdSpecIds)){
           return ;
        }
        BigDecimal unitPrice = BigDecimal.ZERO;
        List<OrderProductSpec> orderProductSpecs = new ArrayList<>(shopProdSpecIds.size());
        StringBuffer sb = new StringBuffer(shopProdSpecIds.size());
        for (Long shopProdSpecId : shopProdSpecIds) {
            //查询具体的样式并且校验
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            ShopProductSpec shopProductSpec = context.shopProductSpecService.getById(shopProdSpecId);
            unitPrice = BigDecimalUtil.sum(unitPrice, shopProductSpec.getPrice());
            ProductSku productSku = context.productSkuService.getById(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(productSku.getSkuName());
            orderProductSpec.setShopProdSpecId(shopProdSpecId);
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            orderProductSpecs.add(orderProductSpec);
            sb.append(orderProductSpec.getProdSpecName()).append(",");
        }
        orderProduct.setProdUnitPrice(unitPrice);
        orderProduct.setOrderProductSpecs(orderProductSpecs);
        orderProduct.setOrderProductSpecStr(sb.substring(0,sb.length()-1));
        BigDecimal prodPrice = BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), orderProduct.getProdNum(), RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);
    }

    /**
     * 购物车结算设置小料
     * @param cartId
     * @param orderProduct
     */
    private void buildSide(Long cartId,OrderProduct orderProduct){
        List<CartSideVO> cartSideList = context.cartSideService.getByCartId(cartId);
        if(CollectionUtils.isEmpty(cartSideList)){
            return;
        }
        Long addPrice = 0L;
        StringBuffer sb = new StringBuffer(orderProduct.getOrderProductSpecStr());
        for(CartSideVO side : cartSideList){
            Long totalSidePrice = side.getAddPrice() * side.getQuantity();
            addPrice += totalSidePrice;
            sb.append(",")
                    .append(side.getSideDishName())
                    .append("x")
                    .append(side.getQuantity())
                    .append("(+")
                    .append(BigDecimalUtil.stripTrailingZeros(BigDecimalUtil.fenToYuan(addPrice)))
                    .append("元)");
        }
        orderProduct.setOrderProductSpecStr(sb.toString());
        orderProduct.setProdUnitPrice(BigDecimalUtil.sum(orderProduct.getProdUnitPrice(),BigDecimalUtil.fenToYuan(addPrice)));
        BigDecimal prodPrice = BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), orderProduct.getProdNum(), RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);
    }

    /**
     * 直接结束设置小料
     * @param orderProduct
     */
    private void buildSide(OrderProduct orderProduct){
        List<SideDishGroupDTO> sideDishGroupList = context.request.getSideDishGroupList();
        if(CollectionUtils.isEmpty(sideDishGroupList)){
            return;
        }
        BigDecimal addPrice = BigDecimal.ZERO;
        StringBuffer sb = new StringBuffer(orderProduct.getOrderProductSpecStr());
        for(SideDishGroupDTO side : sideDishGroupList){
            BigDecimal totalSidePrice = BigDecimalUtil.multiply(side.getAddPrice(),new BigDecimal(side.getQuantity()));
            addPrice =  BigDecimalUtil.sum(addPrice,totalSidePrice);
            sb.append(",")
                    .append(side.getSideDishName())
                    .append("x")
                    .append(side.getQuantity())
                    .append("(+")
                    .append(BigDecimalUtil.stripTrailingZeros(addPrice))
                    .append("元)");
        }
        orderProduct.setOrderProductSpecStr(sb.toString());
        orderProduct.setProdUnitPrice(BigDecimalUtil.sum(orderProduct.getProdUnitPrice(),addPrice));
        BigDecimal prodPrice = BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), orderProduct.getProdNum(), RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);
    }

}
