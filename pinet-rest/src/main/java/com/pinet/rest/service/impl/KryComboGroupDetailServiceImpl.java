package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.KryComboGroupDetail;
import com.pinet.rest.entity.vo.KryComboGroupDetailVo;
import com.pinet.rest.mapper.KryComboGroupDetailMapper;
import com.pinet.rest.service.IKryComboGroupDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客如云门套餐分组明细表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
@Service
public class KryComboGroupDetailServiceImpl extends ServiceImpl<KryComboGroupDetailMapper, KryComboGroupDetail> implements IKryComboGroupDetailService {
    @Resource
    private KryComboGroupDetailMapper kryComboGroupDetailMapper;

    @Override
    public List<KryComboGroupDetailVo> getByShopProdId(Long shopProdId) {
        return kryComboGroupDetailMapper.getByShopProdId(shopProdId);
    }
}
