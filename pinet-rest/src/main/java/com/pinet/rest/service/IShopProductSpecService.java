package com.pinet.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.ShopProductSpec;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface IShopProductSpecService extends IService<ShopProductSpec> {
    /**
     * 减少库存
     * @param shopProductSpecId 店铺商品样式表id
     * @param num 扣减的数量
     * @return 更新的记录条数
     */
    int reduceStock(Long shopProductSpecId, Integer num);

    /**
     * 添加库存
     * @param shopProductSpecId 店铺商品样式表id
     * @param num 增加的数量
     * @return 更新的记录条数
     */
    int addStock(Long shopProductSpecId, Integer num);


    /**
     * 查询商品价格(这个方法有问题)
     * @param shopProdId
     * @return
     */
    BigDecimal getPriceByShopProdId(Long shopProdId);

    /**
     * 根据样式ID查询商品价格
     * @param ids
     * @return
     */
    BigDecimal getPriceByIds(List<Long> ids);

}
