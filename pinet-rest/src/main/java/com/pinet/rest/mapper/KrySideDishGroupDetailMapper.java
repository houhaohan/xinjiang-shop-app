package com.pinet.rest.mapper;

import com.pinet.rest.entity.KrySideDishGroupDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.KrySideDishGroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜品加料组明细 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-04-01
 */
public interface KrySideDishGroupDetailMapper extends BaseMapper<KrySideDishGroupDetail> {

    /**
     * 查询商品加料信息
     * @param shopProdId
     * @return
     */
    List<KrySideDishGroupVo> getByShopProdId(@Param("shopProdId") Long shopProdId);
}
