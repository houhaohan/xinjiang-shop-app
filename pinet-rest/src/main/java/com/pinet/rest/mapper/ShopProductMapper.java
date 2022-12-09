package com.pinet.rest.mapper;

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


    public List<HotProductVo> getProductByShopId(@Param("shopId") Long shopId);

    /**
     * 随机查找8条推荐商品
     * @return
     */
    List<RecommendProductVo> selectRecommendList();

    /**
     * 根据用户ID查找8条推荐商品
     * @param userId
     * @return
     */
    List<RecommendProductVo> selectRecommendListByUserId(@Param("userId") Long userId);

    /**
     * 商品详情
     * @param id
     * @return
     */
    ShopProductVo getDetailById(@Param("id") Long id);

}
