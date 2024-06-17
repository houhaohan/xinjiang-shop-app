package com.pinet.rest.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.VipRechargeTemplate;
import com.pinet.rest.entity.vo.VipRechargeTemplateVO;
import com.pinet.rest.mapper.VipRechargeTemplateMapper;
import com.pinet.rest.service.IVipRechargeTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * VIP充值模板 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@DS(DB.MASTER)
public class VipRechargeTemplateServiceImpl extends ServiceImpl<VipRechargeTemplateMapper, VipRechargeTemplate> implements IVipRechargeTemplateService {

    @Override
    public List<VipRechargeTemplateVO> templateList(Long shopId) {
        List<VipRechargeTemplate> list = getByShopId(shopId);
        if(CollectionUtil.isEmpty(list)){
            //默认模板
            list = getByShopId(0L);
        }
        return convert(list);
    }

    @Override
    public List<VipRechargeTemplate> getByShopId(Long shopId) {
        QueryWrapper<VipRechargeTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id",shopId);
        return list(queryWrapper);
    }

    private List<VipRechargeTemplateVO> convert(List<VipRechargeTemplate> list){
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(item->{
            VipRechargeTemplateVO vipRechargeTemplateVO = new VipRechargeTemplateVO();
            vipRechargeTemplateVO.setShopId(item.getShopId());
            vipRechargeTemplateVO.setAmount(item.getAmount());
            vipRechargeTemplateVO.setGiftAmount(item.getGiftAmount());
            vipRechargeTemplateVO.setDescription(item.getDescription());
            vipRechargeTemplateVO.setTotalAmount(BigDecimalUtil.sum(item.getAmount(),item.getGiftAmount()));
            return vipRechargeTemplateVO;
        }).collect(Collectors.toList());
    }

}
