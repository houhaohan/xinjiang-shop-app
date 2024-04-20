package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.rest.entity.CartProductSpec;
import com.pinet.rest.mapper.CartProductSpecMapper;
import com.pinet.rest.service.ICartProductSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
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
    public List<CartProductSpec> getComboByCartId(Long cartId) {
        return cartProductSpecMapper.getComboByCartId(cartId);
    }

    @Override
    public CartProductSpec getByUserIdAndSpecId(Long userId, Long shopProdSpecId) {
        CartProductSpec cartProductSpec = cartProductSpecMapper.selectByUserIdAndSpecId(userId,shopProdSpecId);
        return cartProductSpec;
    }

    @Override
    public List<CartProductSpec> getByUserIdAndShopProdId(Long userId, Long shopProdId) {
        return cartProductSpecMapper.getByUserIdAndShopProdId(userId,shopProdId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCartId(Long cartId) {
        UpdateWrapper<CartProductSpec> wrapper = new UpdateWrapper<>();
        wrapper.eq("cart_id",cartId);
        return remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCartIds(List<Long> cartIds) {
        if(CollectionUtils.isEmpty(cartIds)){
            return true;
        }
        UpdateWrapper<CartProductSpec> wrapper = new UpdateWrapper<>();
        wrapper.in("cart_id",cartIds);
        return remove(wrapper);
    }

    @Override
    public boolean saveBatch(Collection<CartProductSpec> entityList) {
        return cartProductSpecMapper.insertBatchSomeColumn(entityList) > 0;
    }
}
