package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.OkHttpUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.ShopListDto;
import com.pinet.rest.entity.vo.ShopVo;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.IShopService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Autowired
    private ShopMapper shopMapper;

    private static final String key = "d6f83cc59d2e545ce0f6daf28e80d85f";

    @Override
    public Long getMinDistanceShop(BigDecimal lat, BigDecimal lng) {
        return shopMapper.getMinDistanceShop(lat, lng);
    }

    @Override
    public List<ShopVo> shopList(ShopListDto dto) {
        //log.info("店铺列表参数=======>{}",JSON.toJSONString(dto));
        if (dto.getLat() == null || dto.getLng() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        //根据经纬度获取城市
        String city = getCityInfo(dto.getLng(), dto.getLat());
        //当前定位的城市店铺

        //程双辉的用户ID
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        if(userId == 12014){
            city = null;
        }
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
        Date startTime = DateUtil.parseTimeToday(DateUtil.format(shop.getWorkTime(), "HH:mm:ss"));
        Date endTime = DateUtil.parseTimeToday(DateUtil.format(shop.getFinishTime(), "HH:mm:ss"));
        return com.pinet.core.util.DateUtil.isEffectiveDate(now, startTime, endTime);
    }

    private String getCityInfo(BigDecimal lng,BigDecimal lat){
//        String city = getCityByIp();
//        if(StringUtil.isNotBlank(city) && !"[]".equals(city)){
//            return city;
//        }
        return getCityByLocation(lng,lat);
    }

    private String getCityByIp(){
        String url = String.format("https://restapi.amap.com/v3/ip?ip=%s&output=json&key=%s", IPUtils.getIpAddr(),key);
        log.info("根据IP获取城市URL=======>{}",url);
        String str = OkHttpUtil.get(url, null);
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject.getString("city");
    }

    private String getCityByLocation(BigDecimal lng,BigDecimal lat){
        String url = String.format("https://restapi.amap.com/v3/geocode/regeo?output=json&location=%s,%s&key=%s&extensions=base", lng,lat,key);
        log.info("根据经纬度获取城市URL=======>{}",url);
        String str = OkHttpUtil.get(url, null);
        JSONObject object = JSON.parseObject(str);
        JSONObject regeocode = object.getJSONObject("regeocode");
        JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
        String city = addressComponent.getString("city");
        String province = addressComponent.getString("province");
        if("北京市".equals(province)
                || "重庆市".equals(province)
                || "上海市".equals(province)
                || "天津市".equals(province)){
            city = province;
        }
        return city;
    }


}
