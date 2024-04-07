package com.pinet.rest.service.impl;

import com.pinet.rest.entity.KrySideDishGroupDetail;
import com.pinet.rest.entity.vo.KrySideDishGroupVo;
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
    public List<KrySideDishGroupVo> getByShopProdId(Long shopProdId) {
        return baseMapper.getByShopProdId(shopProdId);
    }
}
