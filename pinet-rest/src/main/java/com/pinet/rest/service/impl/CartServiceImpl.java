package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private IShopService shopService;

    @Resource
    private IShopProductService shopProductService;

    @Resource
    private ICartProductSpecService cartProductSpecService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Override
    public List<CartListVo> cartList(CartListDto dto) {
        //判断店铺是否存在  店铺状态
        Shop shop = shopService.getById(dto.getShopId());
        if (shop == null || shop.getDelFlag() == 1) {
            throw new PinetException("店铺不存在");
        }
        List<CartListVo> cartListVos = cartMapper.selectCartList(dto);
        cartListVos.forEach(k -> {
            List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(k.getCartId());
            String prodSpecName = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecName).collect(Collectors.joining(","));
            k.setProdSpecName(prodSpecName);
            BigDecimal price = cartProductSpecs.stream().map(CartProductSpec::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            k.setProdPrice(price);

            k.setAllPrice(price.multiply(new BigDecimal(k.getProdNum())).setScale(2, RoundingMode.DOWN));

        });

        return cartListVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddCartVo addCart(AddCartDto dto) {

        //校验店铺商品id是否存在
        ShopProduct shopProduct = shopProductService.getById(dto.getShopProdId());
        if (shopProduct == null){
            throw new PinetException("店铺商品不存在");
        }


        Long customerId = dto.getCustomerId();
        Cart cart = new Cart();
        BeanUtils.copyProperties(dto, cart);
        cart.setCartStatus(1);
        cart.setCustomerId(customerId);

        //添加购物车选中的样式
        String[] shopProdSpecIds = dto.getShopProdSpecIds().split(",");
        //如果添加的商品样式在购物车已存在 num+1   如果num=shopProdSpecIds.length说明改商品样式已存在购物车;
        int num = 0;
        Long cartId = 0L;
        List<CartProductSpec> cartProductSpecs = new ArrayList<>();
        for (String shopProdSpecId : shopProdSpecIds) {
            CartProductSpec query = cartProductSpecService.getByUserIdAndSpecId(customerId, Long.valueOf(shopProdSpecId));
            if (query != null) {
                num += 1;
                cartId = query.getCartId();
            }

            CartProductSpec cartProductSpec = new CartProductSpec();
            cartProductSpec.setShopProdSpecId(Long.valueOf(shopProdSpecId));
            ShopProductSpec shopProductSpec = shopProductSpecService.getById(Long.valueOf(shopProdSpecId));
            if (shopProductSpec == null) {
                throw new PinetException("样式不存在");
            }
            cartProductSpec.setShopProdSpecName(shopProductSpec.getSpecName());
            cartProductSpecs.add(cartProductSpec);
        }

        List<CartProductSpec> resCartProdSpec;
        AddCartVo addCartVo = new AddCartVo();
        //如果商品已存在  商品数量相加  不存在则添加
        if (num == shopProdSpecIds.length) {
            Cart cartQuery = getById(cartId);
            cartQuery.setProdNum(cartQuery.getProdNum() + dto.getProdNum());
            updateById(cartQuery);
            addCartVo.setProdNum(cartQuery.getProdNum());

            resCartProdSpec = cartProductSpecService.getByCartId(cartId);
        } else {
            addCartVo.setProdNum(cart.getProdNum());
            save(cart);
            cartProductSpecs.forEach(k->k.setCartId(cart.getId()));
            cartProductSpecService.saveBatch(cartProductSpecs);
            resCartProdSpec = cartProductSpecService.getByCartId(cart.getId());
        }
        BigDecimal price = resCartProdSpec.stream().map(CartProductSpec::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        addCartVo.setPrice(price.multiply(new BigDecimal(addCartVo.getProdNum())).setScale(2, RoundingMode.DOWN));
        return addCartVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editCartProdNum(EditCartProdNumDto dto) {
        Cart cart = getById(dto.getCartId());
        if (cart == null || cart.getDelFlag() == 1) {
            throw new PinetException("购物车不存在");
        }
        //如果数量为0则删除
        if (dto.getProdNum() == 0) {
            cartProductSpecService.remove(new LambdaQueryWrapper<CartProductSpec>().eq(CartProductSpec::getCartId,dto.getCartId()));
            return removeById(dto.getCartId());
        }

        cart.setProdNum(dto.getProdNum());
        return updateById(cart);
    }

    @Override
    public List<Cart> getByUserIdAndShopId(Long userId, Long shopId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BaseEntity::getDelFlag, 0).eq(Cart::getCustomerId, userId).eq(Cart::getShopId, shopId);
        return list(lambdaQueryWrapper);
    }

    @Override
    public void delCartByShopId(Long shopId, Long customerId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getShopId, shopId).eq(Cart::getCustomerId, customerId).eq(BaseEntity::getDelFlag, 0);

        List<Cart> cartList = list(lambdaQueryWrapper);

        //删除购物车
        remove(lambdaQueryWrapper);

        //删除cart_product_spec表数据
        List<Long> cartIds = cartList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        LambdaQueryWrapper<CartProductSpec> removeWrapper = new LambdaQueryWrapper<>();
        removeWrapper.in(CartProductSpec::getCartId,cartIds).eq(BaseEntity::getDelFlag,0);
        cartProductSpecService.remove(removeWrapper);
    }

    @Override
    public CartVo getCartByUserIdAndShopId(Long shopId, Long customerId) {
        return cartMapper.getCartByUserIdAndShopId(shopId,customerId);
    }

}
