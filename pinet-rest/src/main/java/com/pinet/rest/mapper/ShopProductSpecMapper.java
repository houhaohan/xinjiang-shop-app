package com.pinet.rest.mapper;

import com.pinet.rest.entity.ShopProductSpec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface ShopProductSpecMapper extends BaseMapper<ShopProductSpec> {

    /**
     * 减少库存
     *
     * @param shopProductSpecId id
     * @param num               数量
     * @return 更新成功返回1  扣减库存条数 > 剩余库存 返回0
     */
    int updateStock(@Param("shopProductSpecId") Long shopProductSpecId, @Param("num") Integer num);
}
