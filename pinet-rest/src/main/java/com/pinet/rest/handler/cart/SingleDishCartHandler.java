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
import com.pinet.rest.entity.CartSide;
import com.pinet.rest.entity.ShopProductSpec;

import com.pinet.rest.entity.dto.SideDishGroupDTO;
import com.pinet.rest.entity.vo.CartSideVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        boolean specAllMatch = singleProdSpecIds.stream().allMatch(shopProdSpecIdDBs::contains);
        List<SideDishGroupDTO> sideDishGroupList = context.request.getSideDishGroupList();
        if(CollectionUtils.isEmpty(sideDishGroupList)){
            //只判断样式
        }else {
            //判断样式和小料
        }
        List<CartSideVO> cartSideList = context.cartSideService.getByUserIdAndShopId(context.request.getCustomerId(), context.request.getShopId());
        Map<Long, List<CartSideVO>> cartSideMap = cartSideList.stream().collect(Collectors.groupingBy(CartSideVO::getCartId));
        //样式完全匹配，小料完全匹配
        if(specAllMatch){
            Long cartId = cartProductSpecs.get(0).getCartId();
            List<CartSideVO> sideList = cartSideMap.get(cartId);
            List<Long> sideDetailIdDBs = sideList.stream().map(CartSideVO::getSideDetailId).collect(Collectors.toList());
            boolean allMatch = sideDishGroupList.stream().map(SideDishGroupDTO::getId).allMatch(sideDetailIdDBs::contains);
            if(allMatch){
                //购物车已存在，新增数量
                //新增购物车数量
                context.prodNum += 1;
                Cart cart = context.cartMapper.selectById(cartId);
                cart.setProdNum(cart.getProdNum() + context.prodNum);
                context.cartMapper.updateById(cart);

                //新增小料数量
                BigDecimal sideAddPrice = BigDecimal.ZERO;
                for(CartSideVO side : sideList){
                    CartSide cartSide = new CartSide();
                    cartSide.setId(side.getCartSideId());
                    Optional<Integer> optional = context.request.getSideDishGroupList().stream()
                            .filter(s -> Objects.equals(s.getId(), side.getSideDetailId()))
                            .map(SideDishGroupDTO::getQuantity)
                            .findFirst();
                    if(!optional.isPresent()){
                        continue;
                    }
                    sideAddPrice = BigDecimalUtil.sum(sideAddPrice,BigDecimalUtil.fenToYuan(side.getAddPrice()));
                }
                //查询单品价格
                BigDecimal price = context.shopProductSpecService.getPriceByShopProdId(context.request.getShopProdId());
                context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);
                return;
            }
        }

        //购物车不存在，新增购物车
        context.prodNum += context.request.getProdNum();
        Cart cart = buildCartInfo();
        context.cartMapper.insert(cart);

        List<ShopProductSpec> shopProductSpecs = context.shopProductSpecService.listByIds(singleProdSpecIds);
        List<CartProductSpec> cartProductSpecList = singleProdSpecIds.stream().map(specId -> {
            CartProductSpec cartProductSpec = new CartProductSpec();
            cartProductSpec.setCreateBy(cart.getCreateBy());
            cartProductSpec.setCreateTime(cart.getCreateTime());
            cartProductSpec.setCartId(cart.getId());
            cartProductSpec.setShopProdSpecId(specId);
            ShopProductSpec shopProductSpec = FilterUtil.filter(shopProductSpecs, specId, ApiExceptionEnum.SPEC_NOT_EXISTS);
            cartProductSpec.setShopProdSpecName(shopProductSpec.getSpecName());
            BigDecimal price = BigDecimalUtil.multiply(shopProductSpec.getPrice(), new BigDecimal(cart.getProdNum()));
            context.totalPrice = BigDecimalUtil.sum(context.totalPrice, price);
            return cartProductSpec;
        }).collect(Collectors.toList());
        context.cartProductSpecService.saveBatch(cartProductSpecList);

        //新增小料
        if(!CollectionUtils.isEmpty(sideDishGroupList)){
            List<CartSide> cartSideData = sideDishGroupList.stream().map(side -> {
                CartSide cartSide = new CartSide();
                cartSide.setCartId(cart.getId());
                cartSide.setQuantity(side.getQuantity());
                cartSide.setShopProdId(cart.getShopProdId());
                cartSide.setSideDetailId(side.getId());
                return cartSide;
            }).collect(Collectors.toList());
            context.cartSideService.saveBatch(cartSideData);
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
        context.cartProductSpecService.deleteByCartId(cartId);
        context.cartSideService.deleteByCartId(cartId);
        context.cartMapper.deleteById(cartId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clear(List<Long> ids) {
        context.cartProductSpecService.deleteByCartIds(ids);
        context.cartSideService.deleteByCartIds(ids);
    }
}
