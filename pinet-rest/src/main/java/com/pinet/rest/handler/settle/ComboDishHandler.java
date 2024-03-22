package com.pinet.rest.handler.settle;

import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderProductSpecVo;
import com.pinet.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 套餐处理器
 * @author: chengshuanghui
 * @date: 2024-03-22 16:48
 */
public class ComboDishHandler implements DishSettleHandler{
    @Autowired
    private IShopProductService shopProductService;
    @Autowired
    private IShopProductSpecService shopProductSpecService;
    @Autowired
    private IKryComboGroupDetailService kryComboGroupDetailService;
    @Autowired
    private ICartComboDishService cartComboDishService;
    @Autowired
    private ICartComboDishSpecService cartComboDishSpecService;


    public OrderProduct handler(Cart cart,Integer orderType){
        ShopProduct shopProduct = shopProductService.getById(cart.getShopProdId());
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setShopProdId(cart.getShopProdId());
        orderProduct.setDishId(cart.getDishId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setUnit(shopProduct.getUnit());
        Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(shopProduct.getId());
        orderProduct.setProdUnitPrice(BigDecimalUtil.fenToYuan(unitPrice));
        orderProduct.setProdNum(cart.getProdNum());
        orderProduct.setProdPrice(BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), new BigDecimal(orderProduct.getProdNum())));
        orderProduct.setProdImg(shopProduct.getProductImg());
        if (orderType.equals(OrderTypeEnum.TAKEAWAY.getCode())) {
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.COMBO_PACKAGE_FEE, cart.getProdNum(), RoundingMode.HALF_UP));
        }

        StringBuffer sb = new StringBuffer();
        List<ComboDishSpecVo> list = new ArrayList<>();
        List<CartComboDish> cartComboDishList = cartComboDishService.getByCartId(cart.getId());
        for (CartComboDish cartComboDish : cartComboDishList) {
            ComboDishSpecVo comboDishSpecVo = new ComboDishSpecVo();
            comboDishSpecVo.setShopProdId(shopProduct.getId());
            comboDishSpecVo.setSingleDishId(cartComboDish.getShopProdId());
            ShopProduct singleShopProduct = shopProductService.getById(cartComboDish.getShopProdId());
            comboDishSpecVo.setSingleDishName(singleShopProduct.getProductName());
            List<CartComboDishSpec> cartComboDishSpecs = cartComboDishSpecService.getByCartIdAndProdId(cart.getId(), cartComboDish.getShopProdId());

            List<OrderProductSpecVo> orderProductSpecVoList = new ArrayList<>();
            for (CartComboDishSpec cartComboDishSpec : cartComboDishSpecs) {
                OrderProductSpecVo orderProductSpecVo = new OrderProductSpecVo();
                ShopProductSpec shopProductSpec = shopProductSpecService.getById(cartComboDishSpec.getShopProdSpecId());
                orderProductSpecVo.setProdSkuId(shopProductSpec.getSkuId());
                orderProductSpecVo.setProdSkuName(shopProductSpec.getSkuName());
                orderProductSpecVo.setProdSpecName(cartComboDishSpec.getShopProdSpecName());
                orderProductSpecVo.setShopProdSpecId(cartComboDishSpec.getShopProdSpecId());
                orderProductSpecVoList.add(orderProductSpecVo);
            }
            comboDishSpecVo.setOrderProductSpecs(orderProductSpecVoList);
            list.add(comboDishSpecVo);
            sb.append(singleShopProduct.getProductName()).append(",");
        }
        orderProduct.setOrderProductSpecStr(sb.substring(0,sb.length()-1));
        orderProduct.setComboDishDetails(list);
        return orderProduct;
    }

    @Override
    public void handler() {

    }
}
