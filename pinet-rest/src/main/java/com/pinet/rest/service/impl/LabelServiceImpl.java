package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.util.StringUtil;
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

    @Override
    public String getByLabelIds(String labelIds) {
        if(StringUtil.isBlank(labelIds)){
            return null;
        }
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("GROUP_CONCAT(label_name,'/',color order by FIND_IN_SET(id,"+labelIds+"))");
        queryWrapper.apply("FIND_IN_SET(id,{0}) > {1}",labelIds,0);
        return this.getObj(queryWrapper, o -> o.toString());
    }
}
