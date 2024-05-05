package com.pinet.rest.handler.settle;


import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;

import java.util.Objects;

public abstract class DishSettleAbstractHandler implements DishSettleHandler {

    protected DishSettleContext context;

    /**
     * 单品
     *
     * @param shopProdId
     * @param prodNum
     * @return
     */
    protected OrderProduct build(Long shopProdId, Integer prodNum) {
        ShopProduct shopProduct = context.shopProductService.getById(shopProdId);

        //判断店铺商品是否下架
        if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_LINE.getCode())) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }
        OrderProduct orderProduct = new OrderProduct();
        //设置打包费   //自提没有打包费
        if (Objects.equals(context.request.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())) {
            if(Objects.equals(shopProduct.getDishType(), DishType.COMBO)){
                orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE, prodNum));
            }else {
                orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE, prodNum));
            }
        }
        orderProduct.setDishId(shopProduct.getProdId());
        orderProduct.setShopProdId(shopProduct.getId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setProdNum(prodNum);
        orderProduct.setUnit(shopProduct.getUnit());
        orderProduct.setProdImg(shopProduct.getProductImg());
        return orderProduct;
    }

}
