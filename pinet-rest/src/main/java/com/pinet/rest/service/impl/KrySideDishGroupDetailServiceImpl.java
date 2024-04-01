package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.KrySideDishGroupDetail;
import com.pinet.rest.mapper.KrySideDishGroupDetailMapper;
import com.pinet.rest.service.IKrySideDishGroupDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品加料组明细 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-04-01
 */
@Service
public class KrySideDishGroupDetailServiceImpl extends ServiceImpl<KrySideDishGroupDetailMapper, KrySideDishGroupDetail> implements IKrySideDishGroupDetailService {

    @Override
    public List<KrySideDishGroupDetail> getByShopProdId(Long shopProdId) {
        QueryWrapper<KrySideDishGroupDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_prod_id",shopProdId);
        return list(queryWrapper);
    }
}
