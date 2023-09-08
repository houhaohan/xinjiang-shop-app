package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.core.util.OkHttpUtil;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.ShopListDto;
import com.pinet.rest.entity.vo.ShopVo;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.pinet.core.util.LatAndLngUtils.getDistance;

/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
@DS(DB.MASTER)
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Long getMinDistanceShop(BigDecimal lat, BigDecimal lng) {
        return shopMapper.getMinDistanceShop(lat,lng);
    }

    @Override
    public List<ShopVo>  shopList(ShopListDto dto) {
        if (dto.getLat() == null || dto.getLng() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        //根据IP获取城市
        String url = "https://restapi.amap.com/v3/ip?output=json&key=d6f83cc59d2e545ce0f6daf28e80d85f";
        String str = OkHttpUtil.get(url, null);
        JSONObject object = JSON.parseObject(str);
        String city = object.getString("city");

        //当前定位的城市店铺
        List<ShopVo> shopList = shopMapper.shopList(city);
        if (CollectionUtils.isEmpty(shopList)) {
            return Collections.emptyList();
        }
        //计算距离,单位Km
        for (ShopVo shopVo : shopList) {
            double distance = getDistance(dto.getLng().doubleValue(), dto.getLat().doubleValue(), Double.parseDouble(shopVo.getLng()), Double.parseDouble(shopVo.getLat()), 2);
            shopVo.setDistance(distance);
        }
        //根据距离排序
        return shopList.stream().sorted(Comparator.comparing(ShopVo::getDistance)).collect(Collectors.toList());
    }

    @Override
    public Boolean checkShopStatus(Long shopId) {
        Shop shop = getById(shopId);
        return checkShopStatus(shop);
    }

    @Override
    public Boolean checkShopStatus(Shop shop) {
        if (shop.getShopStatus() == 2) {
            return false;
        }
        Date now = new Date();
        Date startTime = DateUtil.parseTimeToday(DateUtil.format(shop.getWorkTime(),"HH:mm:ss"));
        Date endTime = DateUtil.parseTimeToday(DateUtil.format(shop.getFinishTime(),"HH:mm:ss"));
        return com.pinet.core.util.DateUtil.isEffectiveDate(now, startTime, endTime);
    }

}
