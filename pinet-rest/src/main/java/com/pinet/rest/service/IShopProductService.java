package com.pinet.rest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.ShopProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.param.RecommendProductParam;
import com.pinet.rest.entity.param.ShopProductParam;
import com.pinet.rest.entity.vo.*;

import java.util.List;

/**
 * <p>
 * 店铺商品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface IShopProductService extends IService<ShopProduct> {
    /**
     * 首页热卖排行版
     * @param param
     * @return
     */
    List<HotProductVo> hotSellList(HomeProductParam param);

    /**
     * 首页推荐商品
     * @return
     */
    Page<RecommendProductVo> recommendList(RecommendProductParam param);

    /**
     * 商品详情
     * @param id
     * @return
     */
    ShopProductVo getDetailById(Long id);

    /**
     * 店铺商品列表
     */
    ShopProductListVo productListByShopId(Long shopId);

    /**
     * 店铺商品搜索
     * @param
     * @param param
     * @return
     */
    List<ShopProductVo> search(ShopProductParam param);

    /**
     * 畅销商品
     * @param shopId
     * @return
     */
    List<String> sellwell(Long shopId);

}
