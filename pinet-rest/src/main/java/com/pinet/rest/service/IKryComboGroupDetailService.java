package com.pinet.rest.service;

import com.pinet.rest.entity.KryComboGroupDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.ComboSingleProductSpecVo;
import com.pinet.rest.entity.vo.KryComboGroupDetailVo;

import java.util.List;

/**
 * <p>
 * 客如云门套餐分组明细表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
public interface IKryComboGroupDetailService extends IService<KryComboGroupDetail> {

    List<KryComboGroupDetailVo> getByOrderProdId(Long orderProdId,Long shopId);

    /**
     * 根据商品ID 查询套餐明细
     * @param shopProdId
     * @return
     */
    List<KryComboGroupDetail> getByShopProdId(Long shopProdId);

    /**
     * 根据商品ID查询套餐价格
     * @param shopProdId
     * @return
     */
    Long getPriceByShopProdId(Long shopProdId);

    /**
     * 根据商品规格ID 查询规格信息
     * @param shopProdSpecIds
     * @return
     */
    List<ComboSingleProductSpecVo> getSpecByShopProdSpecIds(List<Long> shopProdSpecIds,Long shopId);

}
