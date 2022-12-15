package com.pinet.rest.service.impl;

import com.pinet.rest.entity.ShopProductSpec;
import com.pinet.rest.mapper.ShopProductSpecMapper;
import com.pinet.rest.service.IShopProductSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
public class ShopProductSpecServiceImpl extends ServiceImpl<ShopProductSpecMapper, ShopProductSpec> implements IShopProductSpecService {
    @Resource
    private ShopProductSpecMapper shopProductSpecMapper;

    @Override
    public int reduceStock(Long shopProductSpecId, Integer num) {
        return shopProductSpecMapper.updateStock(shopProductSpecId,num);
    }
}
