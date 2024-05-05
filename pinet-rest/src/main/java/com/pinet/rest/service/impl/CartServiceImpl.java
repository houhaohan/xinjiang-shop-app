package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.AddCartDTO;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.ClearCartDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.enums.CartStatusEnum;
import com.pinet.rest.entity.enums.ShopProdStatusEnum;
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
    private final ICartSideService cartSideService;

    @Override
    public List<CartListVo> cartList(CartListDto dto) {
        //判断店铺是否存在  店铺状态
        Shop shop = shopService.getById(dto.getShopId());
        if (shop == null || shop.getDelFlag() == 1) {
            throw new PinetException("店铺不存在");
        }
        List<CartListVo> cartListVos = cartMapper.selectCartList(dto);
        cartListVos.forEach(cart -> {
            if (DishType.COMBO.equalsIgnoreCase(cart.getDishType())) {
                List<CartComboDishVo> cartComboDishVos = cartComboDishService.getComboDishByCartId(cart.getCartId(), cart.getShopProdId());
                long count = cartComboDishVos.stream().filter(item -> Objects.equals(item.getCartStatus(), CartStatusEnum.EXPIRE.getCode())).count();
                if(count > 0){
                    cart.setCartStatus(CartStatusEnum.EXPIRE.getCode());
                }
                cart.setCartComboDishVos(cartComboDishVos);
                Long unitPrice = kryComboGroupDetailService.getPriceByShopProdId(cart.getShopProdId());

                //规格加价
                List<Long> shopProdSpecIds = cartComboDishVos.stream().map(CartComboDishVo::getComboDishSpecs)
                        .flatMap(list ->
                                list.stream().map(CartComboDishSpecVo::getShopProdSpecId)
                        ).collect(Collectors.toList());

                List<ComboSingleProductSpecVo> comboSingleProductSpecVos = kryComboGroupDetailService.getSpecByShopProdSpecIds(shopProdSpecIds, cart.getShopProdId());
                Long specAddPrice = comboSingleProductSpecVos.stream().map(ComboSingleProductSpecVo::getAddPrice).reduce(0L, Long::sum);
                cart.setProdPrice(BigDecimalUtil.fenToYuan(unitPrice + specAddPrice));
                cart.setAllPrice(BigDecimalUtil.multiply(cart.getProdPrice(), new BigDecimal(cart.getProdNum())));
            } else {
                List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(cart.getCartId());
                for (CartProductSpec cartProductSpec : cartProductSpecs) {
                    if (cartProductSpec.getShopProdDelFlag() == 1) {
                        cart.setCartStatus(CartStatusEnum.EXPIRE.getCode());
                    }
                }
                String prodSpecName = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecName).collect(Collectors.joining(","));
                BigDecimal price = cartProductSpecs.stream().map(CartProductSpec::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                List<CartSideVO> cartSideList = cartSideService.getByCartId(cart.getCartId());
                StringBuilder specName = new StringBuilder(prodSpecName);
                for (CartSideVO side : cartSideList) {
                    BigDecimal addPrice = BigDecimalUtil.fenToYuan(side.getAddPrice() * side.getQuantity());
                    price = BigDecimalUtil.sum(price, addPrice);
                    specName.append(",")
                            .append(side.getSideDishName())
                            .append("x")
                            .append(side.getQuantity())
                            .append("(+").append(BigDecimalUtil.stripTrailingZeros(addPrice))
                            .append("元)");
                }
                cart.setProdSpecName(specName.toString());
                cart.setProdPrice(price);
                cart.setAllPrice(BigDecimalUtil.multiply(price, new BigDecimal(cart.getProdNum())));
            }
        });

        return cartListVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddCartVo addCart(AddCartDTO dto) {
        ShopProduct shopProduct = shopProductService.getById(dto.getShopProdId());
        //校验店铺商品id是否存在
        if (shopProduct == null) {
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
        context.refreshCart(cart.getId(), dto.getProdNum());
        return true;
    }

    @Override
    public List<Cart> getByUserIdAndShopId(Long userId, Long shopId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getCustomerId, userId).eq(Cart::getShopId, shopId);
        lambdaQueryWrapper.orderByDesc(Cart::getId);
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
        return cartMapper.getCartByUserIdAndShopId(shopId, customerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCart(ClearCartDto dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        delCartByShopId(dto.getShopId(), userId);
        return true;
    }

}
