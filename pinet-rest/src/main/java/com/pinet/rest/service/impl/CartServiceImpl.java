package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.ICartProductSpecService;
import com.pinet.rest.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IShopProductSpecService;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private IShopService shopService;

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
        cartListVos.forEach(k->{
            List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(k.getCartId());
            String prodSpecName = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecName).collect(Collectors.joining(","));
            k.setProdSpecName(prodSpecName);
            BigDecimal price = cartProductSpecs.stream().map(CartProductSpec::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            k.setProdPrice(price);
        });

        return cartListVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addCart(AddCartDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        Cart cart = new Cart();
        BeanUtils.copyProperties(dto, cart);
        cart.setCartStatus(1);
        cart.setCustomerId(customerId);
        cart.setCreateBy(customerId);
        Date now = new Date();
        cart.setCreateTime(now);
        cart.setUpdateBy(customerId);
        cart.setUpdateTime(now);
        cart.setDelFlag(0);
        Boolean res =  save(cart);

        //添加购物车选中的样式
        String[] shopProdSpecIds = dto.getShopProdSpecIds().split(",");
        //如果添加的商品样式在购物车已存在 num+1   如果num=shopProdSpecIds.length说明改商品样式已存在购物车;
        int num = 0;
        for (String shopProdSpecId : shopProdSpecIds) {

            CartProductSpec query = cartProductSpecService.getByUserIdAndSpecId(customerId,Long.valueOf(shopProdSpecId));
            if (query != null){
                num += 1;
            }

            CartProductSpec cartProductSpec = new CartProductSpec();
            cartProductSpec.setCartId(cart.getId());
            cartProductSpec.setShopProdSpecId(Long.valueOf(shopProdSpecId));
            ShopProductSpec shopProductSpec =  shopProductSpecService.getById(Long.valueOf(shopProdSpecId));
            if (shopProductSpec == null){
                throw new PinetException("样式不存在");
            }
            cartProductSpec.setShopProdSpecName(shopProductSpec.getSpecName());
            cartProductSpec.setCreateBy(customerId);
            cartProductSpec.setCreateTime(now);
            cartProductSpec.setUpdateBy(customerId);
            cartProductSpec.setUpdateTime(now);
            cartProductSpec.setDelFlag(0);
        }

        if (num == shopProdSpecIds.length){
            throw new PinetException("购物车已存在改商品");
        }
        return res;
    }

    @Override
    public Boolean editCartProdNum(EditCartProdNumDto dto) {
        Cart cart = getById(dto.getCartId());
        if (cart == null || cart.getDelFlag() == 1) {
            throw new PinetException("购物车不存在");
        }
        //如果数量为0则删除
        if (dto.getProdNum() == 0) {
            return removeById(dto.getCartId());
        }

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();


        cart.setProdNum(dto.getProdNum());
        cart.setUpdateTime(new Date());
        cart.setUpdateBy(customerId);
        return updateById(cart);
    }

    @Override
    public List<Cart> getByUserIdAndShopId(Long userId, Long shopId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BaseEntity::getDelFlag,0).eq(Cart::getCustomerId,userId).eq(Cart::getShopId,shopId);
        return list(lambdaQueryWrapper);
    }

    @Override
    public void delCartByShopId(Long shopId, Long customerId) {
        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getShopId,shopId).eq(Cart::getCustomerId,customerId).eq(BaseEntity::getDelFlag,0);
        remove(lambdaQueryWrapper);
    }

    @Override
    public CartVo getCartByUserIdAndShopId(Long shopId, Long customerId) {
        return cartMapper.getCartByUserIdAndShopId(shopId,customerId);
    }
}
