package com.pinet.rest.service.impl;


import com.pinet.core.exception.PinetException;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.mapper.ShopProductMapper;
import com.pinet.rest.service.IProductGlanceOverService;
import com.pinet.rest.service.IShopProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
public class ShopProductServiceImpl extends ServiceImpl<ShopProductMapper, ShopProduct> implements IShopProductService {

    @Autowired
    private IShopService shopService;
    @Autowired
    private IProductGlanceOverService productGlanceOverService;


    @Override
    public List<HotProductVo> hotSellList(HomeProductParam param) {
        if(param.getLat() == null && param.getLng() == null && param.getShopId() == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //根据经纬度获取最近的店铺ID
        if(param.getLat() != null && param.getLng() != null){
            Shop shop = shopService.getMinDistanceShop(param.getLat(), param.getLng());
            if(shop == null){
                return Collections.emptyList();
            }
            param.setShopId(shop.getId());
        }
        return baseMapper.getProductByShopId(param.getShopId());
    }

    @Override
    public List<RecommendProductVo> recommendList(Long userId) {
        if(userId == null){
            //随机查找8条数据
            return baseMapper.selectRecommendList();
        }
        return baseMapper.selectRecommendListByUserId(userId);
    }

    @Override
    public ShopProductVo getDetailById(Long id) {

        ShopProductVo shopProductVo = baseMapper.getDetailById(id);
        if(shopProductVo == null){
            throw new PinetException("商品不存在");
        }

        //更新商品浏览次数
//        productGlanceOverService.updateGlanceOverTimes(id);
        return shopProductVo;
    }
}
