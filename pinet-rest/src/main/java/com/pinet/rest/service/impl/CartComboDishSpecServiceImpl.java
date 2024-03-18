package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.rest.entity.CartComboDishSpec;
import com.pinet.rest.entity.vo.CartComboDishSpecVo;
import com.pinet.rest.mapper.CartComboDishSpecMapper;
import com.pinet.rest.service.ICartComboDishSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 购物车套餐菜品样式表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
@Service
public class CartComboDishSpecServiceImpl extends ServiceImpl<CartComboDishSpecMapper, CartComboDishSpec> implements ICartComboDishSpecService {

    @Override
    public List<CartComboDishSpecVo> getByUserIdAndShopProdSpecId(Long userId, List<Long> shopProdSpecIds) {
        return baseMapper.getByUserIdAndShopProdSpecId(userId,shopProdSpecIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCartId(Integer cartId) {
        UpdateWrapper<CartComboDishSpec> wrapper = new UpdateWrapper<>();
        wrapper.eq("cart_id",cartId);
        remove(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByCartIds(List<Long> cartIds) {
        UpdateWrapper<CartComboDishSpec> wrapper = new UpdateWrapper<>();
        wrapper.in("cart_id",cartIds);
        remove(wrapper);
    }

    @Override
    public List<CartComboDishSpec> getByCartId(Long cartId) {
        QueryWrapper<CartComboDishSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cart_id",cartId);
        return list(queryWrapper);
    }
}
