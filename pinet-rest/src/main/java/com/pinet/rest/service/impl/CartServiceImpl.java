package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.mapper.CartMapper;
import com.pinet.rest.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<CartListVo> cartList(CartListDto dto) {
        //判断店铺是否存在  店铺状态
        Shop shop = shopService.getById(dto.getShopId());
        if (shop == null || shop.getDelFlag() == 1) {
            throw new PinetException("店铺不存在");
        }
        List<CartListVo> cartListVos = cartMapper.selectCartList(dto);
        return cartListVos;
    }

    @Override
    public Boolean addCart(AddCartDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        LambdaQueryWrapper<Cart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Cart::getCustomerId, customerId).eq(Cart::getShopId, dto.getShopId())
                .eq(Cart::getShopProdId, dto.getShopProdId()).eq(BaseEntity::getDelFlag, 0).eq(Cart::getShopProdSpecId, dto.getShopProdSpecId());

        Cart query = getOne(lambdaQueryWrapper);
        if (query != null) {
            throw new PinetException("购物车商品已存在");
        }

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
        return save(cart);
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
}
