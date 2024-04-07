package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pinet.rest.entity.CartSide;
import com.pinet.rest.entity.vo.CartSideVO;
import com.pinet.rest.mapper.CartSideMapper;
import com.pinet.rest.service.ICartSideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 购物车加料 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-04-07
 */
@Service
public class CartSideServiceImpl extends ServiceImpl<CartSideMapper, CartSide> implements ICartSideService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCartId(Long cartId) {
        UpdateWrapper<CartSide> wrapper = new UpdateWrapper<>();
        wrapper.eq("cart_id",cartId);
        return remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCartIds(List<Long> cartIds) {
        if(CollectionUtils.isEmpty(cartIds)){
            return true;
        }
        UpdateWrapper<CartSide> wrapper = new UpdateWrapper<>();
        wrapper.in("cart_id",cartIds);
        return remove(wrapper);
    }

    @Override
    public List<CartSideVO> getByCartId(Long cartId) {
        return baseMapper.getByCartId(cartId);
    }
}
