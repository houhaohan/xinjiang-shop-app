package com.pinet.rest.service.impl;

import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.vo.HotProductVo;
import com.pinet.rest.entity.vo.RecommendProductVo;
import com.pinet.rest.entity.vo.ShopProductVo;
import com.pinet.rest.mapper.ShopProductMapper;
import com.pinet.rest.service.IShopProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

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
    public List<RecommendProductVo> recommendList(String userId) {
        if(StringUtil.isEmpty(userId)){
            //随机查找8条数据
            return baseMapper.selectRecommendList();
        }
        return baseMapper.selectRecommendListByUserId(Long.valueOf(userId));
    }

    @Override
    public ShopProductVo getDetailById(Long id) {
        //记录商品浏览表
        return null;
    }
}
