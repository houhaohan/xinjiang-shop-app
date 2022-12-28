package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.rest.entity.CartProductSpec;
import com.pinet.rest.mapper.CartProductSpecMapper;
import com.pinet.rest.service.ICartProductSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 购物车商品样式表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-28
 */
@Service
public class CartProductSpecServiceImpl extends ServiceImpl<CartProductSpecMapper, CartProductSpec> implements ICartProductSpecService {
    @Resource
    private CartProductSpecMapper cartProductSpecMapper;

    @Override
    public List<CartProductSpec> getByCartId(Long cartId) {
        return cartProductSpecMapper.selectByCartId(cartId);
    }

    @Override
    public CartProductSpec getByUserIdAndSpecId(Long userId, Long shopProdSpecId) {
        CartProductSpec cartProductSpec = cartProductSpecMapper.selectByUserIdAndSpecId(userId,shopProdSpecId);
        return cartProductSpec;
    }


}
