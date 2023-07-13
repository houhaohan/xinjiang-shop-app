package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.rest.entity.BUserBalance;
import com.pinet.rest.mapper.BUserBalanceMapper;
import com.pinet.rest.service.IBUserBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 商家资金余额表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-07-12
 */
@Service
public class BUserBalanceServiceImpl extends ServiceImpl<BUserBalanceMapper, BUserBalance> implements IBUserBalanceService {

    @Override
    public BUserBalance getByShopId(Long shopId) {
        LambdaQueryWrapper<BUserBalance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BUserBalance::getShopId,shopId).eq(BaseEntity::getDelFlag,0);
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean addAmount(Long shopId, BigDecimal amount) {
        BUserBalance bUserBalance = getByShopId(shopId);
        if (ObjectUtil.isNull(bUserBalance)){
            throw new PinetException("商家账户出现异常");
        }
        bUserBalance.setAmount(bUserBalance.getAmount().add(amount));
        return updateById(bUserBalance);
    }
}
