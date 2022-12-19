package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.ShopListDto;
import com.pinet.rest.entity.vo.ShopVo;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.IOrdersService;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private IOrdersService ordersService;

    @Override
    public Shop getMinDistanceShop(BigDecimal lat, BigDecimal lng) {
        return shopMapper.getMinDistanceShop(lat, lng);
    }

    @Override
    public List<ShopVo> shopList(ShopListDto dto) {
        if (dto.getLat() == null || dto.getLng() == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        List<ShopVo> shopList = shopMapper.shopList();
        //查询店铺未完成订单数量限8小时内订单
        Date date = new Date();
        Date queryDate = DateUtil.offsetHour(date, -8);
        List<Orders> orderList = ordersService.list(Wrappers.lambdaQuery(new Orders())
                .in(Orders::getOrderStatus, 20, 30)
                .ge(Orders::getCreateTime, queryDate)
                .le(Orders::getCreateTime, date)
        );
        Map<Long, List<Orders>> collect = orderList.stream().collect(Collectors.groupingBy(Orders::getShopId));
        //计算距离,单位Km，加订单量
        if (ObjectUtil.isNotEmpty(shopList)) {
            for (ShopVo shopVo : shopList) {
                double distance = getDistance(dto.getLng().doubleValue(), dto.getLat().doubleValue(), Double.parseDouble(shopVo.getLng()), Double.parseDouble(shopVo.getLat()), 2);
                shopVo.setDistance(distance);
                for (Long shopId : collect.keySet()) {
                    if (shopVo.getId().equals(shopId)) {
                        shopVo.setOrderNum(collect.get(shopId).size());
                    }
                }
            }
            //根据距离排序
            shopList.stream().sorted(Comparator.comparing(ShopVo::getDistance).reversed()).collect(Collectors.toList());
        }
        return shopList;
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

        Date now = DateUtil.parse(DateUtil.now(), "HH:mm:ss");
        return com.pinet.core.util.DateUtil.isEffectiveDate(now, shop.getWorkTime(), shop.getFinishTime());
    }
}
