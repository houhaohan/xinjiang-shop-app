package com.pinet.rest.mapper;

import com.pinet.rest.entity.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-11-27
 */
public interface LabelMapper extends BaseMapper<Label> {

    String selectByShopProdId(@Param("shopProdId") Long shopProdId);
}
