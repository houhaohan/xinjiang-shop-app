package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.OrderProductDto;
import com.pinet.rest.mapper.OrderProductMapper;
import com.pinet.rest.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements IOrderProductService {
    @Resource
    private OrderProductMapper orderProductMapper;

    @Resource
    private ICartService cartService;

    @Resource
    private IShopProductService shopProductService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Resource
    private IProductSkuService productSkuService;

    @Resource
    private ICartProductSpecService cartProductSpecService;

    @Override
    public List<OrderProduct> getByOrderId(Long orderId) {
        List<OrderProduct> orderProducts = orderProductMapper.selectByOrderId(orderId);
        orderProducts.forEach(k -> k.setOrderProductSpecStr(k.getOrderProductSpecs().stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(","))));
        return orderProducts;
    }

    @Override
    public List<OrderProduct> getByCartAndShop(Long shopId,Integer orderType) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        List<Cart> cartList = cartService.getByUserIdAndShopId(userId, shopId);
        if (cartList == null ||  cartList.size() == 0){
            throw new PinetException("购物车内没有需要结算的商品");
        }
        cartList.forEach(k -> {
            if (k.getCartStatus() == 2) {
                throw new PinetException("购物车内有失效的商品,请删除后在结算");
            }
            //查询购物车商品样式
            List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(k.getId());
            List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
            QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(k.getShopProdId(), k.getProdNum(), shopProdSpecIds,orderType);
            OrderProduct orderProduct = this.getByQueryOrderProductBo(queryOrderProductBo);
            orderProducts.add(orderProduct);
        });
        return orderProducts;
    }

    @Override
    public OrderProduct getByQueryOrderProductBo(QueryOrderProductBo queryOrderProductBo) {
        OrderProduct orderProduct = new OrderProduct();
        ShopProduct shopProduct = shopProductService.getById(queryOrderProductBo.getShopProdId());

        //判断店铺商品是否下架
        if (shopProduct.getShopProdStatus() == 2) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //判断店铺商品是否删除
        if (shopProduct.getDelFlag() == 1) {
            throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
        }

        //设置打包费   //自提没有打包费
        if (queryOrderProductBo.getOrderType() == 1){
            if ("SINGLE".equals(shopProduct.getDishType())){
                orderProduct.setPackageFee(new BigDecimal("1").multiply(new BigDecimal(queryOrderProductBo.getProdNum())));
            }else if ("COMBO".equals(shopProduct.getDishType())){
                orderProduct.setPackageFee(new BigDecimal("2").multiply(new BigDecimal(queryOrderProductBo.getProdNum())));
            }
        }


        orderProduct.setDishId(shopProduct.getProdId());
        orderProduct.setShopProdId(shopProduct.getId());
        orderProduct.setProdName(shopProduct.getProductName());
        orderProduct.setProdNum(queryOrderProductBo.getProdNum());
        orderProduct.setUnit(shopProduct.getUnit());

        List<OrderProductSpec> orderProductSpecs = new ArrayList<>();

        //单价
        BigDecimal prodUnitPrice = BigDecimal.ZERO;
        for (Long shopProdSpecId : queryOrderProductBo.getShopProdSpecIds()) {
            //查询具体的样式并且校验
            ShopProductSpec shopProductSpec = shopProductSpecService.getById(shopProdSpecId);
            if (shopProductSpec.getStock() < queryOrderProductBo.getProdNum()) {
                throw new PinetException(shopProduct.getProductName() + ":" + shopProductSpec.getSpecName() + "库存不足,剩余库存:" + shopProductSpec.getStock());
            }
            prodUnitPrice = prodUnitPrice.add(shopProductSpec.getPrice());

            ProductSku productSku = productSkuService.getById(shopProductSpec.getSkuId());

            OrderProductSpec orderProductSpec = new OrderProductSpec();
            orderProductSpec.setProdSkuId(shopProductSpec.getSkuId());
            orderProductSpec.setProdSkuName(productSku.getSkuName());
            orderProductSpec.setShopProdSpecId(shopProdSpecId);
            orderProductSpec.setProdSpecName(shopProductSpec.getSpecName());
            orderProductSpecs.add(orderProductSpec);
        }

        orderProduct.setOrderProductSpecs(orderProductSpecs);

        orderProduct.setOrderProductSpecStr(orderProductSpecs.stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(",")));

        orderProduct.setProdUnitPrice(prodUnitPrice);
        //计算总价
        BigDecimal prodPrice = prodUnitPrice.multiply(new BigDecimal(queryOrderProductBo.getProdNum())).setScale(2, RoundingMode.DOWN);
        orderProduct.setProdPrice(prodPrice);

        orderProduct.setProdImg(shopProduct.getProductImg());

        return orderProduct;
    }

    @Override
    public List<OrderProductDto> selectByOrderId(Long orderId) {
        return orderProductMapper.getByOrderId(orderId);
    }

}
