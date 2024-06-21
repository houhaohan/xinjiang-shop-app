package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.BCapitalFlow;
import com.pinet.rest.entity.BUserBalance;
import com.pinet.rest.entity.enums.CapitalFlowStatusEnum;
import com.pinet.rest.entity.enums.CapitalFlowWayEnum;
import com.pinet.rest.mapper.BCapitalFlowMapper;
import com.pinet.rest.service.IBCapitalFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IBUserBalanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商家资金流水表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-07-12
 */
@Service
@DS(DB.MASTER)
public class BCapitalFlowServiceImpl extends ServiceImpl<BCapitalFlowMapper, BCapitalFlow> implements IBCapitalFlowService {
    @Resource
    private IBUserBalanceService balanceService;

    @Override
    public void add(BigDecimal orderAmount, Long orderId, Date orderTime, CapitalFlowWayEnum capitalFlowWayEnum, CapitalFlowStatusEnum capitalFlowStatusEnum, Long shopId) {
        BCapitalFlow entity = new BCapitalFlow();
        entity.setOrderId(orderId);
        entity.setOrderTime(orderTime);
        entity.setPaymentWay(capitalFlowWayEnum.getCode());
        entity.setStatus(capitalFlowStatusEnum.getCode());
        BUserBalance bUserBalance = balanceService.getByShopId(shopId);
        entity.setShopId(shopId);
        entity.setActualAmount(orderAmount);
        entity.setRate(OrderConstant.WITHDRAW_RATE);
        entity.setExtraAmount(BigDecimalUtil.multiply(orderAmount,OrderConstant.WITHDRAW_RATE * 0.01));
        entity.setAmount(BigDecimalUtil.subtract(orderAmount,entity.getExtraAmount()));
        entity.setBalance(BigDecimalUtil.sum(bUserBalance.getAmount(),entity.getAmount()));
        save(entity);
    }

}
