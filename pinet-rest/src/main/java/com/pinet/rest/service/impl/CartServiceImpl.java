package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.ClearCartDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.handler.cart.CartContext;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
@DS(DB.MASTER)
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final CartMapper cartMapper;
    private final IShopService shopService;
    private final IShopProductService shopProductService;
    private final ICartProductSpecService cartProductSpecService;
    private final IKryComboGroupDetailService kryComboGroupDetailService;
    private final ICartComboDishService cartComboDishService;

    @Override
    public List<CartListVo> cartList(CartListDto dto) {
        //判断店铺是否存在  店铺状态
        Shop shop = shopService.getById(dto.getShopId());
        if (shop == null || shop.getDelFlag() == 1) {
            throw new PinetException("店铺不存在");
        }
        List<CartListVo> cartListVos = cartMapper.selectCartList(dto);
        cartListVos.forEach(k -> {
            if(DishType.COMBO.equalsIgnoreCase(k.getDishType())){
                List<CartComboDishVo> cartComboDishVos = cartComboDishService.getComboDishByCartId(k.getCartId(),k.getShopProdId());
                k.setCartComboDishVos(cartComboDishVos);
                Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(k.getShopProdId());

                //规格加价
                List<Long> shopProdSpecIds = cartComboDishVos.stream().map(CartComboDishVo::getComboDishSpecs)
                        .flatMap(list ->
                                list.stream().map(CartComboDishSpecVo::getShopProdSpecId)
                        ).collect(Collectors.toList());
                List<ComboSingleProductSpecVo> comboSingleProductSpecVos = kryComboGroupDetailService.getSpecByShopProdSpecIds(shopProdSpecIds, k.getShopProdId());
                Long addPrice = comboSingleProductSpecVos.stream().map(ComboSingleProductSpecVo::getAddPrice).reduce(0L, Long::sum);
                k.setProdPrice(BigDecimalUtil.fenToYuan(unitPrice + addPrice));
                k.setAllPrice(BigDecimalUtil.multiply(k.getProdPrice(),new BigDecimal(k.getProdNum())));
            }else {
                List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(k.getCartId());
                String prodSpecName = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecName).collect(Collectors.joining(","));
                k.setProdSpecName(prodSpecName);
                BigDecimal price = cartProductSpecs.stream().map(CartProductSpec::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                k.setProdPrice(price);
                k.setAllPrice(BigDecimalUtil.multiply(price,new BigDecimal(k.getProdNum())));
            }

        });

        return cartListVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddCartVo addCart(AddCartDto dto) {
        ShopProduct shopProduct = shopProductService.getById(dto.getShopProdId());
        //校验店铺商品id是否存在
        if (shopProduct == null){
            throw new PinetException("店铺商品不存在");
        }
        dto.setShopId(shopProduct.getShopId());
        CartContext context = new CartContext(shopProduct.getDishType());
        context.setRequest(dto);
        context.setShopProduct(shopProduct);
        context.handler();
        return context.response();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editCartProdNum(EditCartProdNumDto dto) {
        Cart cart = getById(dto.getCartId());
        if (cart == null) {
            throw new PinetException("购物车不存在");
        }
        CartContext context = new CartContext(cart.getDishType());
        context.refreshCart(cart.getId(),dto.getProdNum());
        return true;
    }

    @Override
    public List<Cart> getByUserIdAndShopId(Long userId, Long shopId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getCustomerId, userId).eq(Cart::getShopId, shopId);
        return list(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCartByShopId(Long shopId, Long customerId) {
        List<Cart> cartList = getByUserIdAndShopId(customerId, shopId);
        removeBatchByIds(cartList);

        //删除cart_product_spec表数据
        List<Long> singleCartIds = cartList.stream().filter(c -> Objects.equals(c.getDishType(), DishType.SINGLE)).map(Cart::getId).collect(Collectors.toList());
        List<Long> comboCartIds = cartList.stream().filter(c -> Objects.equals(c.getDishType(), DishType.COMBO)).map(Cart::getId).collect(Collectors.toList());

        //删除购物车单品
        CartContext singleContext = new CartContext(DishType.SINGLE);
        singleContext.clear(singleCartIds);

        //删除购物车套餐
        CartContext comboContext = new CartContext(DishType.COMBO);
        comboContext.clear(comboCartIds);

    }

    @Override
    public CartVo getCartByUserIdAndShopId(Long shopId, Long customerId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",shopId);
        queryWrapper.eq("customer_id",customerId);
        List<Cart> cartList = list(queryWrapper);
        CartVo cartVo = new CartVo();
        Integer prodNum = 0;
        BigDecimal price = BigDecimal.ZERO;

        for(Cart cart : cartList){
            if(DishType.SINGLE.equals(cart.getDishType())){
                //单品单价
                BigDecimal unitPrice = cartMapper.getSingleByCartId(cart.getId());
                price = BigDecimalUtil.sum(price,BigDecimalUtil.multiply(unitPrice,new BigDecimal(cart.getProdNum())));
            }else {
                //套餐单价
                Long unitPrice = cartMapper.getComboByCartId(cart.getId());
                price = BigDecimalUtil.sum(price,BigDecimalUtil.multiply(BigDecimalUtil.fenToYuan(unitPrice),new BigDecimal(cart.getProdNum())));
            }
            prodNum = prodNum + cart.getProdNum();
        }
        cartVo.setPrice(price);
        cartVo.setProdNum(prodNum);
        return cartVo;
//        return cartMapper.getCartByUserIdAndShopId(shopId,customerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCart(ClearCartDto dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        delCartByShopId(dto.getShopId(),userId);
        return true;
    }

}
