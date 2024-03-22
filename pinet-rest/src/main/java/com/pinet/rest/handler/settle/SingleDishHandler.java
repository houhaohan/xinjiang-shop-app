package com.pinet.rest.handler.settle;

import com.pinet.core.constants.OrderConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.OrderComboDishDto;
import com.pinet.rest.entity.dto.OrderComboDishSpecDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;
import com.pinet.rest.entity.vo.ComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderProductSpecVo;
import com.pinet.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 单品处理器
 * @author: chengshuanghui
 * @date: 2024-03-22 16:48
 */
public class SingleDishHandler implements DishSettleHandler{
    @Autowired
    private ICartProductSpecService cartProductSpecService;
    @Autowired
    private IShopProductService shopProductService;
    @Autowired
    private IShopProductSpecService shopProductSpecService;
    @Autowired
    private IProductSkuService productSkuService;

    public OrderProduct handler(Cart cart,Integer orderType){
        List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(cart.getId());
        List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(cart.getShopProdId(), cart.getProdNum(), shopProdSpecIds, orderType,null);

        OrderProduct orderProduct = new OrderProduct();
        ShopProduct shopProduct = shopProductService.getById(queryOrderProductBo.getShopProdId());

        //判断店铺商品是否下架
        if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_SHELF.getCode())) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //设置打包费   //自提没有打包费
        if (queryOrderProductBo.getOrderType().equals(OrderTypeEnum.TAKEAWAY.getCode())) {
            orderProduct.setPackageFee(BigDecimalUtil.multiply(OrderConstant.SINGLE_PACKAGE_FEE, queryOrderProductBo.getProdNum(), RoundingMode.HALF_UP));
        }
        orderProduct.setDishId(shopProduct.getProdId());
        orderProduct.setShopProdId(shopProduct.getId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setProdNum(queryOrderProductBo.getProdNum());
        orderProduct.setUnit(shopProduct.getUnit());
        //orderProduct.setDishType(shopProduct.getDishType());
        //单价
        BigDecimal unitPrice = BigDecimal.ZERO;
        List<OrderProductSpec> orderProductSpecs = new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        for (Long shopProdSpecId : queryOrderProductBo.getShopProdSpecIds()) {
            //查询具体的样式并且校验
            OrderProductSpec orderProductSpec = new OrderProductSpec();
            ShopProductSpec shopProductSpec = shopProductSpecService.getById(shopProdSpecId);
            unitPrice = BigDecimalUtil.sum(unitPrice, shopProductSpec.getPrice());
            ProductSku productSku = productSkuService.getById(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(productSku.getSkuName());
            orderProductSpec.setShopProdSpecId(shopProdSpecId);
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            orderProductSpecs.add(orderProductSpec);
            sb.append(orderProductSpec.getProdSpecName()).append(",");
        }
        orderProduct.setOrderProductSpecs(orderProductSpecs);
        orderProduct.setOrderProductSpecStr(sb.substring(0,sb.length()-1));
        orderProduct.setProdUnitPrice(unitPrice);
        //计算总价
        BigDecimal prodPrice = BigDecimalUtil.multiply(unitPrice, queryOrderProductBo.getProdNum(), RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);
        orderProduct.setProdImg(shopProduct.getProductImg());
        return orderProduct;
    }

    @Override
    public void handler() {

    }
}
