package com.pinet.rest.handler.cart;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.FilterUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.CartProductSpec;
import com.pinet.rest.entity.ShopProductSpec;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:21
 */
public class SingleDishCartHandler extends DishCartHandler {

    public SingleDishCartHandler(CartContext context){
        this.context = context;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handler() {
        if(StringUtil.isBlank(context.request.getShopProdSpecIds())){
            throw new PinetException(ApiExceptionEnum.SPEC_ID_NOT_BLANK);
        }

        //单品
        List<Long> singleProdSpecIds = Convert.toList(Long.class, context.request.getShopProdSpecIds());
        //判断存不存在
        List<CartProductSpec> cartProductSpecs = context.cartProductSpecService.getByUserIdAndShopProdId(context.request.getCustomerId(), context.request.getShopProdId());
        List<Long> shopProdSpecIdDBs = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
        boolean allMatch = singleProdSpecIds.stream().allMatch(shopProdSpecIdDBs::contains);

        if(allMatch){
            //购物车已存在，新增数量
            context.prodNum += 1;
            Long cartId = cartProductSpecs.get(0).getCartId();
            Cart cart = context.cartMapper.selectById(cartId);
            cart.setProdNum(cart.getProdNum() + context.prodNum);
            context.cartMapper.updateById(cart);

            //查询单品价格
            BigDecimal price = context.shopProductSpecService.getPriceByShopProdId(context.request.getShopProdId());
            context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);
            return;
        }

        //购物车不存在，新增购物车
        context.prodNum += context.request.getProdNum();
        Cart cart = buildCartInfo();
        context.cartMapper.insert(cart);

        List<ShopProductSpec> shopProductSpecs = context.shopProductSpecService.listByIds(singleProdSpecIds);
        for(Long specId : singleProdSpecIds){
            CartProductSpec cartProductSpec = new CartProductSpec();
            cartProductSpec.setCartId(cart.getId());
            cartProductSpec.setShopProdSpecId(specId);
            ShopProductSpec shopProductSpec = FilterUtil.filter(shopProductSpecs, specId, ApiExceptionEnum.SPEC_NOT_EXISTS);
            cartProductSpec.setShopProdSpecName(shopProductSpec.getSpecName());
            context.cartProductSpecService.save(cartProductSpec);
            BigDecimal price = BigDecimalUtil.multiply(shopProductSpec.getPrice(), new BigDecimal(cart.getProdNum()));
            context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshCart(Long cartId, Integer prodNum) {
        if (prodNum > 0) {
            Cart cart = new Cart();
            cart.setId(cartId);
            cart.setProdNum(prodNum);
            context.cartMapper.updateById(cart);
            return;
        }
        context.cartProductSpecService.remove(new LambdaUpdateWrapper<CartProductSpec>().eq(CartProductSpec::getCartId,cartId));
        context.cartMapper.deleteById(cartId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clear(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        LambdaUpdateWrapper<CartProductSpec> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CartProductSpec::getCartId,ids);
        context.cartProductSpecService.remove(wrapper);
    }
}
