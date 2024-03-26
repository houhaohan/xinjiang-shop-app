package com.pinet.rest.service.impl;

import com.pinet.rest.entity.KryComboGroupDetail;
import com.pinet.rest.entity.vo.ComboSingleProductSpecVo;
import com.pinet.rest.entity.vo.KryComboGroupDetailVo;
import com.pinet.rest.mapper.KryComboGroupDetailMapper;
import com.pinet.rest.service.IKryComboGroupDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public List<KryComboGroupDetailVo> getByOrderProdId(Long orderProdId,Long shopId) {
        return kryComboGroupDetailMapper.getByOrderProdId(orderProdId,shopId);
    }

    @Override
    public List<KryComboGroupDetail> getByShopProdId(Long shopProdId) {
        return kryComboGroupDetailMapper.getByShopProdId(shopProdId);
    }

    @Override
    public Long getPriceByShopProdId(Long shopProdId) {
        return kryComboGroupDetailMapper.getPriceByShopProdId(shopProdId);
    }

    @Override
    public List<ComboSingleProductSpecVo> getSpecByShopProdSpecIds(List<Long> shopProdSpecIds,Long shopId) {
        return kryComboGroupDetailMapper.getSpecByShopProdSpecIds(shopProdSpecIds,shopId);
    }
}
