package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.GetShopIdAndShopProdIdDto;
import com.pinet.rest.entity.param.HomeProductParam;
import com.pinet.rest.entity.param.RecommendProductParam;
import com.pinet.rest.entity.param.ShopProductParam;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.mapper.ShopProductMapper;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
@Slf4j
@RequiredArgsConstructor
public class ShopProductServiceImpl extends ServiceImpl<ShopProductMapper, ShopProduct> implements IShopProductService {

    private final IShopService shopService;
    private final ICustomerAddressService customerAddressService;
    private final IKryComboGroupDetailService kryComboGroupDetailService;
    private final ILabelService labelService;
    private final IKrySideDishGroupDetailService krySideDishGroupDetailService;

    @Override
    public List<HotProductVo> hotSellList(HomeProductParam param) {
        //根据经纬度获取最近的店铺ID
        if (param.getShopId() == null) {
            Long shopId = shopService.getMinDistanceShop(param.getLat(), param.getLng());
            if (shopId == null) {
                return Collections.emptyList();
            }
            param.setShopId(shopId);
        }
        List<HotProductVo> hotProductVos = baseMapper.getProductByShopId(param.getShopId());
        return hotProductVos;
    }

    @Override
    public Page<RecommendProductVo> recommendList(RecommendProductParam param) {
        Page<RecommendProductVo> page = new Page<>(1, 20);
        //根据经纬度获取最近的店铺ID
        if (param.getShopId() == null) {
            Long shopId = shopService.getMinDistanceShop(param.getLat(), param.getLng());
            if (shopId == null) {
                return page;
            }
            param.setShopId(shopId);
        }

        //前4个为用户浏览最多的8个，后面12个随机推荐
        //前8条数据
        List<RecommendProductVo> first8List = baseMapper.selectFirst8RecommendList(param);
        List<Long> prodIds = first8List.stream().map(RecommendProductVo::getProdId).collect(Collectors.toList());

        //后12条数据
        List<RecommendProductVo> last12List = baseMapper.selectLast12RecommendList(param, prodIds);

        List<RecommendProductVo> result = new ArrayList<>(first8List.size() + last12List.size());
        result.addAll(first8List);
        result.addAll(last12List);
        page.setRecords(result);
        page.setTotal(result.size());
        return page;
    }

    @Override
    public <T extends ProductDetailVo> T getDetailById(Long id) {
        ShopProduct shopProduct = getById(id);
        if (shopProduct == null) {
            throw new PinetException("商品不存在");
        }
        if(DishType.COMBO.equalsIgnoreCase(shopProduct.getDishType())){
            ComboDishVo comboDishVo = new ComboDishVo();
            comboDishVo.setId(id);
            comboDishVo.setDishType(shopProduct.getDishType());
            comboDishVo.setProdId(shopProduct.getProdId());
            comboDishVo.setProductName(shopProduct.getProductName());
            comboDishVo.setProductImg(shopProduct.getProductImg());
            comboDishVo.setProductDesc(shopProduct.getProductDesc());
            comboDishVo.setProductTypeId(shopProduct.getProductTypeId());
            comboDishVo.setProductType(shopProduct.getProductType());
            comboDishVo.setShopId(shopProduct.getShopId());
            comboDishVo.setSaleCount(shopProduct.getSaleCount());
            String labels = labelService.getByLabelIds(shopProduct.getLableId());
            comboDishVo.setLabels(labels);
            //套餐明细
            List<KryComboGroupDetail> kryComboGroupDetails = kryComboGroupDetailService.getByShopProdId(id);
            KryComboGroupDetail comboGroupDetail = kryComboGroupDetails.stream().filter(item -> StringUtil.isBlank(item.getSingleDishId())).findFirst().get();

            List<ComboGroup> singleDishList = baseMapper.getComboDetailByShopIdAndShopProdId(shopProduct.getShopId(), id);
            comboDishVo.setPrice(BigDecimalUtil.fenToYuan(comboGroupDetail.getPrice()));
            comboDishVo.setMarketPrice(BigDecimalUtil.fenToYuan(comboGroupDetail.getSellPrice()));
            comboDishVo.setGroups(singleDishList);
            return (T)comboDishVo;
        }
        ShopProductVo shopProductVo = baseMapper.getDetailById(id);
        List<KrySideDishGroupVo> krySideDishGroupDetails = krySideDishGroupDetailService.getByShopProdId(id);
        shopProductVo.setSideDishGroupList(krySideDishGroupDetails);
        return (T)shopProductVo;
    }


    @Override
    public ShopProductListVo productListByShopId(Long shopId,BigDecimal lat,BigDecimal lng) {
        ShopProductListVo result = new ShopProductListVo();
        Shop shop = shopService.getById(shopId);
        if(lat != null && lng != null){
            double distance = LatAndLngUtils.getDistance(lng.doubleValue(), lat.doubleValue(), Double.valueOf(shop.getLng()), Double.valueOf(shop.getLat()));
            shop.setDistance(distance);
            result.setDistance(BigDecimal.valueOf(distance));
        }

        result.setShopInfo(shop);
        result.setShopId(shop.getId());
        result.setShopName(shop.getShopName());
        result.setLat(shop.getLat());
        result.setLng(shop.getLng());
        String address = new StringBuilder()
                .append(shop.getProvince())
                .append(shop.getCity())
                .append(shop.getDistrict())
                .append(shop.getAddress())
                .toString();
        result.setAddress(address);
        List<ProdTypeVo> typeList = baseMapper.getProductListByShopId(shopId);
        result.setTypeList(typeList);

        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        CustomerAddress defaultAddress = customerAddressService.getDefaultAddress(userId);
        result.setDefaultAddress(defaultAddress);
        return result;
    }

    @Override
    public List<ShopProductVo> search(ShopProductParam param) {
        if (param.getShopId() == null || StringUtil.isEmpty(param.getProductName())) {
            return Collections.EMPTY_LIST;
        }
        return baseMapper.search(param);
    }

    @Override
    public List<String> sellwell(Long shopId) {
        if (shopId == null) return Collections.EMPTY_LIST;
        return baseMapper.sellwell(shopId);
    }

    @Override
    public GetShopProdIdByProdIdVo getShopIdAndShopProdId(GetShopIdAndShopProdIdDto dto) {
        ShopProduct shopProduct = getById(dto.getShopProdId());
        if (ObjectUtil.isNull(shopProduct)){
            throw new PinetException("分享商品不存在");
        }
        dto.setShopProdName(shopProduct.getProductName().trim());
        GetShopProdIdByProdIdVo getShopProdIdByProdIdVo = baseMapper.selectShopIdAndShopProdId(dto);
        if (ObjectUtil.isNull(getShopProdIdByProdIdVo)) {
            throw new PinetException("附近店铺没有这个商品");
        }
        return getShopProdIdByProdIdVo;
    }

}
