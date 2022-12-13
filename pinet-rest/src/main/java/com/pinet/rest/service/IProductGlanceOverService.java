package com.pinet.rest.service;

import com.pinet.rest.entity.ProductGlanceOver;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品浏览表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface IProductGlanceOverService extends IService<ProductGlanceOver> {

    /**
     * 根据商品Id修改浏览记录
     * @param prodId
     * @return
     */
    void updateGlanceOverTimes(Long prodId);

}
