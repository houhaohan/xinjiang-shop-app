package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.BUserBalance;
import com.pinet.rest.mapper.BUserBalanceMapper;
import com.pinet.rest.service.IBUserBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

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
        lambdaQueryWrapper.eq(BUserBalance::getShopId,shopId);
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean addAmount(Long shopId, BigDecimal amount) {
        BUserBalance bUserBalance = getByShopId(shopId);
        if (Objects.isNull(bUserBalance)){
            throw new PinetException("商家账户出现异常");
        }
        //手续费
        BigDecimal extraAmount = BigDecimalUtil.multiply(amount, OrderConstant.WITHDRAW_RATE * 0.01);
        BigDecimal actualAmount = BigDecimalUtil.subtract(amount, extraAmount);
        bUserBalance.setAmount(BigDecimalUtil.sum(bUserBalance.getAmount(),actualAmount));
        return updateById(bUserBalance);
    }
}
