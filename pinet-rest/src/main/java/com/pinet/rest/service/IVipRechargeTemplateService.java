package com.pinet.rest.service;

import com.pinet.rest.entity.VipRechargeTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.VipRechargeTemplateVO;

import java.util.List;

/**
 * <p>
 * VIP充值模板 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
public interface IVipRechargeTemplateService extends IService<VipRechargeTemplate> {

    /**
     * 充值模板列表
     * @param shopId
     * @return
     */
    List<VipRechargeTemplateVO> templateList(Long shopId);
}
