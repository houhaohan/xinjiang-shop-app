package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.constants.DB;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.VipLevel;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.mapper.OrderPayMapper;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mapper.VipLevelMapper;
import com.pinet.rest.service.IVipLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * VIP等级表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Service
@RequiredArgsConstructor
@DS(DB.MASTER)
public class VipLevelServiceImpl extends ServiceImpl<VipLevelMapper, VipLevel> implements IVipLevelService {
    private final OrdersMapper ordersMapper;

    @Override
    public BigDecimal nextLevelDiffAmount(Long customerId,Integer level) {
        BigDecimal paidAmount = ordersMapper.getPaidAmount(customerId);

        VipLevel vipLevel = getByLevel(VipLevelEnum.next(level).getLevel());
        return BigDecimalUtil.subtract(vipLevel.getMinAmount(),paidAmount);
    }

    @Override
    public VipLevel getByLevel(Integer level) {
        QueryWrapper<VipLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`level`",level);
        return getOne(queryWrapper);
    }
}
