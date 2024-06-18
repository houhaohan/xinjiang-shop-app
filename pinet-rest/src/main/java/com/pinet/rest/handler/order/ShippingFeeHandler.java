package com.pinet.rest.handler.order;

import cn.hutool.core.date.DateUtil;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.RedisKeyConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.Environment;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.rest.entity.VipUser;
import com.pinet.rest.entity.enums.DeliveryPlatformEnum;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.VipLevelEnum;
import com.pinet.rest.entity.request.DeliveryFeeRequest;
import com.pinet.rest.service.IShippingFeeRuleService;
import com.pinet.rest.service.IVipUserService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @description 费送费处理器
 * @author chengshuanghui
 * @data 2024-03-23 15:00
 */
public class ShippingFeeHandler {

    /**
     * 计算实际支付的配送费
     * @param request
     * @return
     */

    public BigDecimal calculate(DeliveryFeeRequest request, Integer vipLevel, RedisUtil redisUtil) {

        if(Objects.equals(request.getOrderType(), OrderTypeEnum.SELF_PICKUP.getCode())){
            return BigDecimal.ZERO;
        }
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if(Objects.equals(DeliveryPlatformEnum.ZPS.getCode(),request.getDeliveryPlatform())){
            //商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }
//        if(vipLevel >= VipLevelEnum.VIP4.getLevel()){
//            //VIP4/VIP5 每周免一单配送费
//            Date endOfWeek = DateUtil.endOfWeek(new Date()).toJdkDate();
//            int timeout = com.pinet.core.util.DateUtil.dateDiff(new Date(), endOfWeek);
//            String key = RedisKeyConstant.FREE_DELIVERY_FEE + request.getCustomerId();
//            boolean success = redisUtil.setIfAbsent(key, "1", timeout, TimeUnit.SECONDS);
//            if(success){
//                //本单免配送费
//                return BigDecimal.ZERO;
//            }
//        }
        IShippingFeeRuleService shippingFeeRuleService = SpringContextUtils.getBean(IShippingFeeRuleService.class);
        BigDecimal shippingFee = shippingFeeRuleService.getByDistance(request.getOrderDistance());
        return Optional.ofNullable(shippingFee).orElseThrow(() -> new PinetException("配送费查询失败"));
    }

}
