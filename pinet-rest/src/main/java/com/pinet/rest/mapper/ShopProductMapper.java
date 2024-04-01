package com.pinet.rest.mapper;

import com.pinet.rest.entity.ShopProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.GetShopIdAndShopProdIdDto;
import com.pinet.rest.entity.param.RecommendProductParam;
import com.pinet.rest.entity.param.ShopProductParam;
import com.pinet.rest.entity.vo.*;
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
     * 随机查找12条推荐商品
     * @return
     */
    List<RecommendProductVo> selectLast12RecommendList(@Param("param") RecommendProductParam param,@Param("prodIds") List<Long> prodIds);

    /**
     * 根据用户ID查找8条推荐商品
     * @param param
     * @return
     */
    List<RecommendProductVo> selectFirst8RecommendList(RecommendProductParam param);

    /**
     * 商品详情
     * @param id
     * @return
     */
    ShopProductVo getDetailById(@Param("id") Long id);

    /**
     * 套餐详情
     * @param shopId
     * @param dishIds
     * @return
     */
    List<ShopProductVo> getComboDetailByShopIdAndDishIds(@Param("shopId") Long shopId,@Param("dishIds") List<String> dishIds);

    /**
     * 根据店铺ID 和 套餐商品ID 查询套餐明细
     * @param shopId
     * @param shopProdId
     * @param dishIds
     * @return
     */
    List<ComboGroup> getComboDetailByShopIdAndShopProdId(@Param("shopId") Long shopId,@Param("shopProdId") Long shopProdId,@Param("dishIds") List<String> dishIds);


    /**
     * 根据店铺ID 查找商品列表
     * @param shopId
     * @return
     */
    List<ProdTypeVo> getProductListByShopId(@Param("shopId") Long shopId);

    /**
     * 根据店铺ID 和商品名称查询商品信息
     * @param param
     * @return
     */
    List<ShopProductVo> search(ShopProductParam param);

    /**
     * 店铺畅销
     * @param shopId
     * @return
     */
    List<String> sellwell(@Param("shopId") Long shopId);

    GetShopProdIdByProdIdVo selectShopIdAndShopProdId(GetShopIdAndShopProdIdDto dto);
}
