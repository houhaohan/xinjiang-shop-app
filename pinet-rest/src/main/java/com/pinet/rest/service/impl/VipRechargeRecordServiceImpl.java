package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.VipRechargeRecord;
import com.pinet.rest.mapper.VipRechargeRecordMapper;
import com.pinet.rest.service.IVipRechargeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * VIP充值记录 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@DS(DB.MASTER)
public class VipRechargeRecordServiceImpl extends ServiceImpl<VipRechargeRecordMapper, VipRechargeRecord> implements IVipRechargeRecordService {

    @Override
    public VipRechargeRecord getByOutTradeNo(String outTradeNo) {
        QueryWrapper<VipRechargeRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("out_trade_no",outTradeNo);
        return getOne(queryWrapper);
    }
}
