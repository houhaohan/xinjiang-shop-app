package com.pinet.rest.handler.cart;

import cn.hutool.core.convert.Convert;
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
 * @description: 购物车单品处理器
 * @author: chengshuanghui
 * @date: 2024-03-18 14:21
 */
public class SingleDishCartHandler extends DishCartHandler {

    public SingleDishCartHandler(CartContext context){
        this.context = context;
    }

    /**
     * 购物车单品处理逻辑
     * 1、前端参数样式 ID 和数据库样式 ID不完全相同，新增购物车。
     * 2.1、如果样式完全相同，判断前端参数是否有小料，如果前端参数没传小料，购物车有小料，新增购物车。
     * 2.2、如果前端参数和购物车都没有小料，修改购物车数量、价格。
     * 3、前端传了小料，购物车没有小料，新增购物车
     * 4、前端传了小料，购物车也有小料，判断小料 ID和数量是否完全相同，如果是，修改购物车数量
     * 5、（前端参数的样式ID 、小料ID、小料数量） 和 （购物车的样式ID、小料ID、小料数量）不完全匹配，新增购物车
     */
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
        if(CollectionUtils.isEmpty(cartProductSpecs)){
            saveCart(singleProdSpecIds);
            return;
        }
        boolean exists = false;
        boolean save = false;
        Map<Long, List<CartProductSpec>> cartProductSpecMap = cartProductSpecs.stream().collect(Collectors.groupingBy(CartProductSpec::getCartId));
        for (Map.Entry<Long, List<CartProductSpec>> entry : cartProductSpecMap.entrySet()){
            Long cartId = entry.getKey();
            List<CartProductSpec> cartProductSpecList = entry.getValue();

            List<Long> shopProdSpecIdDBs = cartProductSpecList.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
            boolean specAllMatch = singleProdSpecIds.stream().allMatch(shopProdSpecIdDBs::contains);

            //样式不完全匹配，新增购物车
            if(!specAllMatch){
                //新增购物车
                saveCart(singleProdSpecIds);
                continue;
            }

            //样式完全匹配
            List<CartSideVO> cartSideList = context.cartSideService.getByCartId(cartId);
            //此次没有传小料
            if(CollectionUtils.isEmpty(context.request.getSideDishGroupList())){
                long count = cartSideList.stream().filter(side -> Objects.equals(context.request.getShopProdId(),side.getShopProdId())).count();
                if(count > 0){
                    //新增购物车(样式相同，购物车此商品有小料)
                    saveCart(singleProdSpecIds);
                }else {
                    //修改数量(样式相同，购物车此商品没有小料)
                    updateCart(cartId,null);
                }
                continue;
            }

            //前端传了小料，并且购物车没有小料
            if(CollectionUtils.isEmpty(cartSideList)){
                //新增购物车
                saveCart(singleProdSpecIds);
                continue;
            }
            //前端有小料，购物车也有小料，看小料ID是否完全匹配，是就修改，否就新增
            List<String> sideDetailIdDBs = cartSideList.stream().map(s->s.getSideDetailId() + "-" + s.getQuantity()).collect(Collectors.toList());
            boolean allMatch = context.request.getSideDishGroupList().stream().map(s->s.getId() + "-" + s.getQuantity()).allMatch(sideDetailIdDBs::contains);
            if(allMatch){
                exists = true;
                updateCart(cartId,cartSideList);
            }else {
                save = true;
            }
        }

        if(!exists && save){
            saveCart(singleProdSpecIds);
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


    /**
     * 新增购物车
     * @param singleProdSpecIds 样式 ID
     * @return
     */
    private void saveCart(List<Long> singleProdSpecIds){
        context.prodNum += context.request.getProdNum();
        Cart cart = buildCartInfo();
        context.cartMapper.insert(cart);

        saveCartSpec(cart,singleProdSpecIds);
        saveCartSide(cart);
    }

    /**
     * 添加规格
     * @param cart
     * @param singleProdSpecIds 样式 ID
     */
    private void saveCartSpec(Cart cart,List<Long> singleProdSpecIds){
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
    }

    /**
     * 添加小料
     * @param cart
     */
    private void saveCartSide(Cart cart){
        List<SideDishGroupDTO> sideDishGroupList = context.request.getSideDishGroupList();
        if(CollectionUtils.isEmpty(sideDishGroupList)){
            return;
        }
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

    /**
     * 修改购物车数量
     * @param cartId
     */
    private void updateCart(Long cartId,List<CartSideVO> sideList){
        context.prodNum += 1;
        Cart cart = context.cartMapper.selectById(cartId);
        cart.setProdNum(cart.getProdNum() + context.prodNum);
        context.cartMapper.updateById(cart);

        //新增小料数量

        BigDecimal addPrice = BigDecimal.ZERO;
        if(!CollectionUtils.isEmpty(sideList)){
            for(CartSideVO side : sideList){
                Optional<Integer> optional = context.request.getSideDishGroupList().stream()
                        .filter(s -> Objects.equals(s.getId(), side.getSideDetailId()))
                        .map(SideDishGroupDTO::getQuantity)
                        .findFirst();
                if(!optional.isPresent()){
                    continue;
                }
                addPrice = BigDecimalUtil.sum(addPrice,BigDecimalUtil.fenToYuan(side.getAddPrice()));
            }
        }
        //查询单品价格
        BigDecimal price = context.shopProductSpecService.getPriceByShopProdId(context.request.getShopProdId());
        context.totalPrice = BigDecimalUtil.sum(context.totalPrice,price,addPrice);
        return;
    }
}
