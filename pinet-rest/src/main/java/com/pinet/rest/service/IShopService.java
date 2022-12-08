package com.pinet.rest.service;

import com.pinet.rest.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 店铺表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface IShopService extends IService<Shop> {

    /**
     * 根据经纬度查询最近的店铺
     * @param lat
     * @param lng
     * @return
     */
    public Shop getMinDistanceShop(BigDecimal lat, BigDecimal lng);

}
