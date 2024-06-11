package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.VipEquity;
import com.pinet.rest.mapper.VipEquityMapper;
import com.pinet.rest.service.IVipEquityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IVipUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIP权益表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-11
 */
@Service
@DS(DB.MASTER)
@RequiredArgsConstructor
public class VipEquityServiceImpl extends ServiceImpl<VipEquityMapper, VipEquity> implements IVipEquityService {
    private final IVipUserService vipUserService;

    @Override
    public List<VipEquity> equityList() {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Integer level = vipUserService.getLevelByCustomerId(userId);

        QueryWrapper<VipEquity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vip_level",level);
        queryWrapper.orderByAsc("id");
        return list(queryWrapper);
    }
}
