package com.pinet.rest.service.impl;

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
import java.time.LocalDateTime;
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
public class BCapitalFlowServiceImpl extends ServiceImpl<BCapitalFlowMapper, BCapitalFlow> implements IBCapitalFlowService {
    @Resource
    private IBUserBalanceService balanceService;

    @Override
    public void add(BigDecimal amount, Long orderId, Date orderTime, CapitalFlowWayEnum capitalFlowWayEnum, CapitalFlowStatusEnum capitalFlowStatusEnum, Long shopId) {
        BCapitalFlow bCapitalFlow = new BCapitalFlow();
        bCapitalFlow.setAmount(amount);
        bCapitalFlow.setOrderId(orderId);
        bCapitalFlow.setOrderTime(orderTime);
        bCapitalFlow.setPaymentWay(capitalFlowWayEnum.getCode());
        bCapitalFlow.setStatus(capitalFlowStatusEnum.getCode());
        BUserBalance bUserBalance = balanceService.getByShopId(shopId);
        bCapitalFlow.setBalance(bUserBalance.getAmount().add(amount));
        bCapitalFlow.setShopId(shopId);
        bCapitalFlow.setCreateTime(new Date());

        save(bCapitalFlow);
    }
}
