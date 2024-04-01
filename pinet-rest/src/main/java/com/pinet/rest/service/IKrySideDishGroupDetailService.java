package com.pinet.rest.service;

import com.pinet.rest.entity.KrySideDishGroupDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品加料组明细 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-04-01
 */
public interface IKrySideDishGroupDetailService extends IService<KrySideDishGroupDetail> {

    /**
     * 根据商品 ID查询加料明细
     * @param shopProdId
     * @return
     */
    List<KrySideDishGroupDetail> getByShopProdId(Long shopProdId);
}
