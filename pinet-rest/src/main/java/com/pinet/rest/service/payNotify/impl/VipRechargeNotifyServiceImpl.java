package com.pinet.rest.service.payNotify.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.OrderConstant;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.DateUtils;
import com.pinet.keruyun.openapi.dto.DirectChargeDTO;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.vo.customer.DirectChargeVO;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.enums.CapitalFlowStatusEnum;
import com.pinet.rest.entity.enums.CapitalFlowWayEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.service.*;
import com.pinet.rest.service.payNotify.IPayNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * VIP充值回调
 * @author chengshuanghui
 */
@Service("vip_recharge_notify_service")
@RequiredArgsConstructor
@Slf4j
public class VipRechargeNotifyServiceImpl implements IPayNotifyService {
    private final IOrderPayService orderPayService;
    private final IVipRechargeRecordService vipRechargeRecordService;
    private final IVipUserService vipUserService;
    private final IVipShopBalanceService vipShopBalanceService;
    private final ICustomerBalanceRecordService customerBalanceRecordService;
    private final IKryApiService kryApiService;
    private final IShopService shopService;
    private final IBUserBalanceService userBalanceService;
    private final IBCapitalFlowService capitalFlowService;
    @Value("${kry.brandId}")
    private Long brandId;
    @Value("${kry.brandToken}")
    private String brandToken;

    @Override
    @DSTransactional
    public boolean payNotify(OrderPayNotifyParam param) {
        OrderPay orderPay = orderPayService.getByOrderNo(param.getOrderNo());
        if (orderPay == null || Objects.equals(orderPay.getPayStatus(),OrderConstant.PAID)) {
            return false;
        }
        orderPay.setPayStatus(OrderConstant.PAID);
        orderPay.setPayTime(param.getPayTime());
        orderPay.setOutTradeNo(param.getOutTradeNo());
        orderPayService.updateById(orderPay);

        VipRechargeRecord vipRechargeRecord = vipRechargeRecordService.getByOrderNo(param.getOrderNo().toString());
        if(Objects.equals(CommonConstant.SUCCESS,vipRechargeRecord.getStatus())){
            return false;
        }
        VipUser user = vipUserService.getByCustomerId(orderPay.getCustomerId());

        Shop shop = shopService.getById(vipRechargeRecord.getShopId());
        DirectChargeDTO directChargeDTO = new DirectChargeDTO();
        directChargeDTO.setShopId(shop.getKryShopId().toString());
        directChargeDTO.setBizDate(DateUtils.getTime());
        directChargeDTO.setOperatorName("管理员");
        BigDecimal totalAmount = BigDecimalUtil.sum(vipRechargeRecord.getRealAmount(), vipRechargeRecord.getGiftAmount());
        directChargeDTO.setRealValue(BigDecimalUtil.yuan2Fen(totalAmount));
        directChargeDTO.setCustomerId(user.getKryCustomerId());
        directChargeDTO.setOperatorId(user.getKryCustomerId());
        directChargeDTO.setBizId(IdUtil.randomUUID());
        DirectChargeVO directChargeVO = kryApiService.directCharge(brandId, brandToken, directChargeDTO);
        if(directChargeVO == null){
            log.error("VIP充值异常");
            return false;
        }
        //更新用户余额
        vipShopBalanceService.updateAmount(orderPay.getCustomerId(),shop.getId(),totalAmount);

        //更新充值记录状态
        vipRechargeRecord.setStatus(CommonConstant.SUCCESS);
        vipRechargeRecord.setOutTradeNo(param.getOutTradeNo());
        vipRechargeRecordService.updateById(vipRechargeRecord);

        //用户资金流水
        CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
        customerBalanceRecord.setCustomerId(user.getCustomerId());
        customerBalanceRecord.setType(BalanceRecordTypeEnum._5.getCode());
        customerBalanceRecord.setTypeStr(BalanceRecordTypeEnum._5.getMsg());
        customerBalanceRecord.setMoney(totalAmount);
        customerBalanceRecord.setFkId(user.getCustomerId());
        customerBalanceRecordService.save(customerBalanceRecord);

        //更新商家余额
        userBalanceService.addAmount(shop.getId(),vipRechargeRecord.getRealAmount());
        //商家资金流水
        capitalFlowService.add(vipRechargeRecord.getRealAmount(),orderPay.getOrderId(),orderPay.getCreateTime(),CapitalFlowWayEnum.getEnumByChannelId(orderPay.getChannelId()),CapitalFlowStatusEnum.SUCCESS,shop.getId());
        return true;
    }

}
