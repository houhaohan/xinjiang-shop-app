package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.rest.entity.KryComboGroup;
import com.pinet.rest.mapper.KryComboGroupMapper;
import com.pinet.rest.service.IKryComboGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客如云门套餐分组表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
@Service
public class KryComboGroupServiceImpl extends ServiceImpl<KryComboGroupMapper, KryComboGroup> implements IKryComboGroupService {

    @Override
    public List<KryComboGroup> getByShopProdId(Long shopProdId) {
        LambdaQueryWrapper<KryComboGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KryComboGroup::getShopProdId,shopProdId);
        return list(queryWrapper);
    }
}
