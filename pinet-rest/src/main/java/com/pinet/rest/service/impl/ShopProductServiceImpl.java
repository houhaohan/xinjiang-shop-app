package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.param.RecommendProductParam;
import com.pinet.rest.entity.param.ShopProductParam;
import com.pinet.rest.entity.vo.HotProductVo;
import com.pinet.rest.entity.vo.RecommendProductVo;
import com.pinet.rest.entity.vo.ShopProductListVo;
import com.pinet.rest.entity.vo.ShopProductVo;
import com.pinet.rest.mapper.ShopProductMapper;
import com.pinet.rest.service.IProductGlanceOverService;
import com.pinet.rest.service.IShopProductService;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        if(param.getLat() == null || param.getLng() == null ){
            throw new IllegalArgumentException("获取经纬度失败，请检查定位是否开启");
        }
        //根据经纬度获取最近的店铺ID
        if(param.getLat() != null && param.getLng() != null && param.getShopId() == null){
            Long shopId = shopService.getMinDistanceShop(param.getLat(), param.getLng());
            if(shopId == null){
                return Collections.emptyList();
            }
            param.setShopId(shopId);
        }
        return baseMapper.getProductByShopId(param.getShopId());
    }

    @Override
    public Page<RecommendProductVo> recommendList(RecommendProductParam param) {
        if(param.getLat() == null || param.getLng() == null ){
            throw new IllegalArgumentException("获取经纬度失败，请检查定位是否开启");
        }
        Page<RecommendProductVo> page = new Page<>(1,20);
        //根据经纬度获取最近的店铺ID
        if(param.getLat() != null && param.getLng() != null && param.getShopId() == null){
            Long shopId = shopService.getMinDistanceShop(param.getLat(), param.getLng());
            if(shopId == null){
                return page;
            }
            param.setShopId(shopId);
        }

        //前4个为用户浏览最多的8个，后面12个随机推荐
        //前8条数据
        List<RecommendProductVo> first4List = baseMapper.selectFirst8RecommendList(param);
        List<Long> prodIds = first4List.stream().map(RecommendProductVo::getProdId).collect(Collectors.toList());

        //后12条数据
        List<RecommendProductVo> last12List = baseMapper.selectLast12RecommendList(param,prodIds);

        List<RecommendProductVo> result = new ArrayList<>(first4List.size()+last12List.size());
        result.addAll(first4List);
        result.addAll(last12List);
        page.setRecords(result);
        page.setTotal(result.size());
        return page;
    }

    @Override
    public ShopProductVo getDetailById(Long id) {

        ShopProductVo shopProductVo = baseMapper.getDetailById(id);
        if(shopProductVo == null){
            throw new PinetException("商品不存在");
        }

        //更新商品浏览次数
        productGlanceOverService.updateGlanceOverTimes(id);
        return shopProductVo;
    }

    @Override
    public ShopProductListVo productListByShopId(Long shopId) {
        return baseMapper.getProductListByShopId(shopId);
    }

    @Override
    public List<ShopProductVo> search(ShopProductParam param) {
        if(param.getShopId() == null || StringUtil.isEmpty(param.getProductName())){
            return Collections.EMPTY_LIST;
        }
        List<ShopProductVo> list = baseMapper.search(param);
//        pageList.getRecords().forEach(item->{
//            //距离
//            double distance = LatAndLngUtils.getDistance(param.getLng().doubleValue(), param.getLat().doubleValue(), Double.valueOf(item.getLng()), Double.valueOf(item.getLat()));
//            item.setDistance(BigDecimal.valueOf(distance));
//        });
        return list;
    }

    @Override
    public List<String> sellwell(Long shopId) {
        if(shopId == null) return Collections.EMPTY_LIST;
        return baseMapper.sellwell(shopId);
    }
}
