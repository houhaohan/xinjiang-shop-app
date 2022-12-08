package com.pinet.rest.service;

import com.pinet.rest.entity.ShopProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.vo.HotProductVo;
import com.pinet.rest.entity.vo.RecommendProductVo;
import com.pinet.rest.entity.vo.ShopProductVo;

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
    List<RecommendProductVo> recommendList(String userId);


    /**
     * 商品详情
     * @param id
     * @return
     */
    ShopProductVo getDetailById(Long id);
}
