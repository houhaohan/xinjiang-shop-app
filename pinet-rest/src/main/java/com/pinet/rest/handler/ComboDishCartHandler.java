package com.pinet.rest.handler;

import cn.hutool.core.convert.Convert;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.vo.CartComboDishSpecVo;
import com.pinet.rest.entity.vo.OrderProductVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:21
 */
public class ComboDishCartHandler extends DishCartHandler{

    public ComboDishCartHandler(CartContext context){
        this.context = context;
    }

    /**
     * 新增购物车
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handler() {
        List<Long> shopProdSpecIds = new ArrayList<>();
        for (AddCartDto singleDish : context.request.getComboDetails()){
            shopProdSpecIds.addAll(Convert.toList(Long.class, singleDish.getShopProdSpecIds()));
        }
        //查询套餐价格
        Long unitPrice = context.kryComboGroupDetailService.getPriceByShopProdId(context.request.getShopProdId());

        List<CartComboDishSpecVo> cartComboDishSpecVos = context.cartComboDishSpecService.getByUserIdAndShopProdSpecId(context.customerId, shopProdSpecIds);
        List<Long> shopProdSpecIdDBs = cartComboDishSpecVos.stream().map(CartComboDishSpecVo::getShopProdSpecId).collect(Collectors.toList());
        boolean allMatch = shopProdSpecIds.stream().allMatch(shopProdSpecIdDBs::contains);
        if(allMatch){
            //增加数量
            context.prodNum ++;
            Long cartId = cartComboDishSpecVos.get(0).getCartId();
            Cart cart = context.cartMapper.selectById(cartId);
            cart.setProdNum(cart.getProdNum() + context.request.getProdNum());
            context.cartMapper.updateById(cart);
            //套餐价格
            BigDecimal price = BigDecimalUtil.multiply(BigDecimalUtil.fenToYuan(unitPrice), new BigDecimal(cart.getProdNum()));
            context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);
            return;
        }


        //新增购物车
        context.prodNum += context.request.getProdNum();
        Cart cart = buildCartInfo();
        context.cartMapper.insert(cart);
        List<AddCartDto> comboDetails = context.request.getComboDetails();
        for(AddCartDto singleDish : comboDetails){
            CartComboDish cartComboDish = new CartComboDish();
            cartComboDish.setCartId(cart.getId());
            cartComboDish.setShopProdId(singleDish.getShopProdId());
            context.cartComboDishService.save(cartComboDish);
            List<Long> singleProdSpecIds = Convert.toList(Long.class, singleDish.getShopProdSpecIds());
            for(Long shopProdSpecId : singleProdSpecIds){

                CartComboDishSpec cartComboDishSpec = new CartComboDishSpec();
                cartComboDishSpec.setCartId(cart.getId());
                cartComboDishSpec.setShopProdId(singleDish.getShopProdId());
                cartComboDishSpec.setShopProdSpecId(shopProdSpecId);
                ShopProductSpec shopProductSpec = context.shopProductSpecService.getById(shopProdSpecId);
                if (shopProductSpec == null) {
                    throw new PinetException("样式不存在");
                }
                cartComboDishSpec.setShopProdSpecName(shopProductSpec.getSpecName());
                context.cartComboDishSpecService.save(cartComboDishSpec);
            }
        }

        BigDecimal price = BigDecimalUtil.multiply(BigDecimalUtil.fenToYuan(unitPrice), new BigDecimal(cart.getProdNum()));
        context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshCart(Cart cart,Integer prodNum) {
        if (prodNum > 0) {
            cart.setProdNum(prodNum);
            context.cartMapper.updateById(cart);
            return;
        }
        context.cartMapper.deleteById(cart.getId());
        context.cartComboDishService.deleteByCartId(cart.getId().intValue());
        context.cartComboDishSpecService.deleteByCartId(cart.getId().intValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clear(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return;
        }
        context.cartComboDishService.deleteByCartIds(ids);
        context.cartComboDishSpecService.deleteByCartIds(ids);
    }

}
