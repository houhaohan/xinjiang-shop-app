package com.pinet.rest.service;

import com.pinet.rest.entity.KryComboGroupDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.KryComboGroupDetailVo;

import java.util.List;

/**
 * <p>
 * 客如云门套餐分组明细表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
public interface IKryComboGroupDetailService extends IService<KryComboGroupDetail> {

    List<KryComboGroupDetailVo> getByOrderProdId(Long orderProdId,Long shopId);

}
