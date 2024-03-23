package com.pinet.rest.handler.order;

import cn.hutool.core.convert.Convert;
import com.pinet.core.exception.PinetException;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;
import com.pinet.rest.entity.request.DirectOrderRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description 直接购买订单处理器
 * @author chengshaunghui
 * @data 2024-03-21 15:00
 */
public class DirectBuyOrderHandler extends OrderAbstractHandler {

    public DirectBuyOrderHandler(OrderContext context){
        this.context = context;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create() {
        Orders orders = buildOrder();
        ShopProduct shopProduct = context.shopProductService.getById(context.request.getShopProdId());
        //判断店铺商品是否下架
        if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_SHELF.getCode())) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        boolean condition = commissionCondition(orders.getCustomerId(), orders.getShareId());

        DirectOrderRequest request = new DirectOrderRequest();
        request.setOrderId(orders.getId());
        request.setShopProdId(shopProduct.getId());
        request.setProdName(shopProduct.getProductName());
        request.setUnit(shopProduct.getUnit());
        request.setProdImg(shopProduct.getProductImg());
        request.setDishId(shopProduct.getProdId());
        request.setProdNum(context.request.getProdNum());
        request.setShopProdSpecIds(Convert.toList(Long.class,context.request.getShopProdSpecIds()));
        request.setComboDishDtoList(context.request.getOrderComboDishList());
        request.setCalculate(condition);

        List<OrderProduct> orderProducts = new ArrayList<>();
        OrderProduct orderProduct = context.orderDishContext
                .handler(shopProduct.getDishType())
                .execute(request);
        orderProducts.add(orderProduct);
        orders.setPackageFee(orderProduct.getPackageFee());
        orders.setOrderProdPrice(orderProduct.getProdPrice());

        afterHandler(orders,orderProducts);
    }


}
