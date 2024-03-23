package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.rest.entity.CartComboDish;
import com.pinet.rest.entity.vo.CartComboDishVo;
import com.pinet.rest.mapper.CartComboDishMapper;
import com.pinet.rest.service.ICartComboDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 购物车套餐菜品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
@Service
public class CartComboDishServiceImpl extends ServiceImpl<CartComboDishMapper, CartComboDish> implements ICartComboDishService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCartId(Long cartId) {
        UpdateWrapper<CartComboDish> wrapper = new UpdateWrapper<>();
        wrapper.eq("cart_id",cartId);
        remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByCartIds(List<Long> cartIds) {
        UpdateWrapper<CartComboDish> wrapper = new UpdateWrapper<>();
        wrapper.in("cart_id",cartIds);
        remove(wrapper);
    }

    @Override
    public List<CartComboDish> getByCartId(Long cartId) {
        QueryWrapper<CartComboDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cart_id",cartId);
        return list(queryWrapper);
    }

    @Override
    public List<CartComboDishVo> getComboDishByCartId(Long cartId,Long shopProdId) {
        return baseMapper.getComboDishByCartId(cartId,shopProdId);
    }
}
