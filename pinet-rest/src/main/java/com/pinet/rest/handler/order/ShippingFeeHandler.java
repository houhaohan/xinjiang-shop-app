package com.pinet.rest.handler.order;

import com.pinet.core.exception.PinetException;
import com.pinet.core.util.Environment;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.rest.entity.enums.DeliveryPlatformEnum;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.service.IShippingFeeRuleService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * @description 费送费处理器
 * @author chengshuanghui
 * @data 2024-03-23 15:00
 */
public class ShippingFeeHandler {


    /**
     * 计算实际支付的配送费
     * @param distance
     * @return
     */
    public BigDecimal calculate(Integer orderType,Integer distance,String deliveryPlatform) {
        if(Objects.equals(orderType, OrderTypeEnum.SELF_PICKUP.getCode())){
            return BigDecimal.ZERO;
        }
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if(Objects.equals(DeliveryPlatformEnum.ZPS.getCode(),deliveryPlatform)){
            //商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }
        IShippingFeeRuleService shippingFeeRuleService = SpringContextUtils.getBean(IShippingFeeRuleService.class);
        BigDecimal shippingFee = shippingFeeRuleService.getByDistance(distance);
        return Optional.ofNullable(shippingFee).orElseThrow(() -> new PinetException("配送费查询失败"));
    }

}
