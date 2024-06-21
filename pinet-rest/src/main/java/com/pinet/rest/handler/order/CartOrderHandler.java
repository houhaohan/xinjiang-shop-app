package com.pinet.rest.handler.order;

import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.*;
import com.pinet.rest.entity.request.CartOrderProductRequest;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @description 购物车订单处理器
 * @author chengshuanghui
 * @data 2024-03-21 15:00
 */
public class CartOrderHandler extends OrderAbstractHandler {
    public CartOrderHandler(OrderContext context) {
        this.context = context;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create() {
        super.checkVipUser();

        Orders orders = buildOrder();
        boolean condition = commissionCondition(orders.getCustomerId(),orders.getShareId());
        List<Cart> cartList = context.cartService.getByUserIdAndShopId(context.customerId, context.request.getShopId());
        List<OrderProduct> orderProducts = new ArrayList<>(cartList.size());
        for(Cart cart : cartList){
            ShopProduct shopProduct = context.shopProductService.getById(cart.getShopProdId());
            //判断店铺商品是否下架
            if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_LINE.getCode())) {
                throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
            }

            //判断店铺商品是否删除
            if (shopProduct.getDelFlag() == 1) {
                throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
            }
            //订单类型( 1外卖  2自提)
            CartOrderProductRequest request = new CartOrderProductRequest();
            request.setCartId(cart.getId());
            request.setDishId(cart.getDishId());
            request.setOrderId(orders.getId());
            request.setShopProdId(cart.getShopProdId());
            request.setProdName(shopProduct.getProductName());
            request.setProdNum(cart.getProdNum());
            request.setProdImg(shopProduct.getProductImg());
            request.setUnit(shopProduct.getUnit());
            request.setOrderType(context.request.getOrderType());
            request.setCalculate(condition);
            request.setCustomerId(context.customerId);

            OrderProduct orderProduct = context.orderDishContext
                    .handler(shopProduct.getDishType())
                    .execute(request);
            orders.setPackageFee(BigDecimalUtil.sum(orders.getPackageFee(),orderProduct.getPackageFee()));
            orders.setOrderProdPrice(BigDecimalUtil.sum(orders.getOrderProdPrice(),orderProduct.getProdPrice()));
            orderProducts.add(orderProduct);
        }

        // 清除购物车
        context.cartService.delCartByShopId(orders.getShopId(),orders.getCustomerId());

        afterHandler(orders,orderProducts);

    }


}
