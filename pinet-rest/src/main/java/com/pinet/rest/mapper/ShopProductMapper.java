package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.ShopProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.HotProductVo;
import com.pinet.rest.entity.vo.RecommendProductVo;
import com.pinet.rest.entity.vo.ShopProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 店铺商品表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface ShopProductMapper extends BaseMapper<ShopProduct> {


    /**
     * 首页热卖排行版
     * @param shopId
     * @return
     */
    public List<HotProductVo> getProductByShopId(@Param("shopId") Long shopId);

    /**
     * 随机查找8条推荐商品
     * @return
     */
    Page<RecommendProductVo> selectRecommendList(Page<RecommendProductVo> page);

    /**
     * 根据用户ID查找8条推荐商品
     * @param userId
     * @return
     */
    Page<RecommendProductVo> selectRecommendListByUserId(Page<RecommendProductVo> page,@Param("userId") Long userId);

    /**
     * 商品详情
     * @param id
     * @return
     */
    ShopProductVo getDetailById(@Param("id") Long id);

}
