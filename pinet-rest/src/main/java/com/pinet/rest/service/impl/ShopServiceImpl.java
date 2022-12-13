package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pinet.rest.entity.Order;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.ShopListDto;
import com.pinet.rest.entity.vo.ShopVo;
import com.pinet.rest.mapper.OrderMapper;
import com.pinet.rest.mapper.ShopMapper;
import com.pinet.rest.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private OrderMapper orderMapper;

    @Override
    public Shop getMinDistanceShop(BigDecimal lat, BigDecimal lng) {
        return shopMapper.getMinDistanceShop(lat,lng);
    }

    @Override
    public List<ShopVo> shopList(ShopListDto dto) {
        if(dto.getLat() == null || dto.getLng() == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        List<ShopVo> shopList = shopMapper.shopList();
        //查询店铺未完成订单数量
        List<Order> orderList = orderMapper.selectList(Wrappers.lambdaQuery(new Order())
                .in(Order::getOrderStatus,20,30)
        );
        Map<Long, List<Order>> collect = orderList.stream().collect(Collectors.groupingBy(Order::getShopId));
        //计算距离，加订单量
        if (ObjectUtil.isNotEmpty(shopList)){
            for (ShopVo shopVo : shopList) {
                double distance = getDistance(dto.getLng().doubleValue(), dto.getLat().doubleValue(), Double.parseDouble(shopVo.getLng()), Double.parseDouble(shopVo.getLat()), 2);
                shopVo.setDistance(distance);
                for (long i = 0; i < collect.keySet().size(); i++) {
                    if (shopVo.getId()==i){
                        shopVo.setOrderNum(collect.get(i).size());
                    }
                }
            }
            //根据距离排序
            shopList.stream().sorted(Comparator.comparing(ShopVo::getDistance).reversed()).collect(Collectors.toList());
        }
        return shopList;
    }
}
