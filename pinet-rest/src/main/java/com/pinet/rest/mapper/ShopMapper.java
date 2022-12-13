package com.pinet.rest.mapper;

import com.pinet.rest.entity.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.ShopVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 店铺表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Repository
public interface ShopMapper extends BaseMapper<Shop> {

    /**
     * 根据经纬度查询最近的店铺
     * @param lat
     * @param lng
     * @return
     */
    public Shop getMinDistanceShop(@Param("lat") BigDecimal lat,@Param("lng") BigDecimal lng) ;

    /**
     * 查找店铺列表
     * @return
     */
    List<ShopVo> shopList();
}
