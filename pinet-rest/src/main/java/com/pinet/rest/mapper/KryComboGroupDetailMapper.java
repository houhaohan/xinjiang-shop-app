package com.pinet.rest.mapper;

import com.pinet.rest.entity.KryComboGroupDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.KryComboGroupDetailVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客如云门套餐分组明细表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
public interface KryComboGroupDetailMapper extends BaseMapper<KryComboGroupDetail> {

    /**
     * 根据 商品ID 查询套餐明细
     * @param shopProdId
     * @return
     */
    List<KryComboGroupDetailVo> getByShopProdId(@Param("shopProdId") Long shopProdId);
}
