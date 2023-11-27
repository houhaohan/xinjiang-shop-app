package com.pinet.rest.service.impl;

import com.pinet.rest.entity.Label;
import com.pinet.rest.mapper.LabelMapper;
import com.pinet.rest.service.ILabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-11-27
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements ILabelService {

    @Override
    public String getByShopProdId(Long shopProdId) {
        return baseMapper.selectByShopProdId(shopProdId);
    }
}
