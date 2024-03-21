package com.pinet.rest.handler.order;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.Environment;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.enums.DeliveryPlatformEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.service.ICustomerAddressService;
import com.pinet.rest.service.ICustomerMemberService;
import com.pinet.rest.service.IDaDaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderAmountHandler {
    private final ICustomerAddressService customerAddressService;
    private final ICustomerMemberService customerMemberService;
    private final IDaDaService daDaService;

    /**
     * 获取平台配送费
     *
     * @return BigDecimal
     */
    public BigDecimal getShippingFeePlat(Integer orderType, Shop shop,Long customerAddressId,BigDecimal orderProdPrice ) {
        if (Objects.equals(orderType, OrderTypeEnum.SELF_PICKUP.getCode())) {
            return BigDecimal.ZERO;
        }
        //测试环境默认4元吧
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }

        if (StringUtil.isBlank(shop.getDeliveryShopNo())
                || shop.getDeliveryPlatform().equals(DeliveryPlatformEnum.ZPS.getCode())) {
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }
        //查询收货地址
        CustomerAddress customerAddress = customerAddressService.getById(customerAddressId);
        if (ObjectUtil.isNull(customerAddress)) {
            throw new PinetException("收货地址异常");
        }

        Snowflake snowflake = IdUtil.getSnowflake();

        AddOrderReq addOrderReq = AddOrderReq.builder()
                .shopNo(shop.getDeliveryShopNo())
                .originId(snowflake.nextIdStr())
                .cargoPrice(orderProdPrice.doubleValue())
                .prepay(0)
                .receiverName(customerAddress.getName())
                .receiverAddress(customerAddress.getAddress())
                .receiverPhone(customerAddress.getPhone())
                .callback("http://xinjiangapi.ypxlbz.com/house/qingshi/api/dada/deliverFee/callback")
                .cargoWeight(0.5)
                .receiverLat(customerAddress.getLat().doubleValue())
                .receiverLng(customerAddress.getLng().doubleValue())
                .build();
        try {
            AddOrderResp addOrderResp = daDaService.queryDeliverFee(addOrderReq);
            return BigDecimal.valueOf(addOrderResp.getDeliverFee());
        } catch (RpcException e) {
            throw new PinetException("查询配送费服务失败");
        }
    }

    /**
     * 是否满足计算佣金的条件
     *
     * @param customerId 下单人 ID
     * @param shareId 分享人 ID
     */
    public boolean commissionCondition(Long customerId,Long shareId) {
        if (ObjectUtil.isNull(shareId) || shareId <= 0) {
            return false;
        }

        //判断下单人和分享人是否是同一个人
        if (shareId.equals(customerId)) {
            return false;
        }
        //分享人会员等级
        Integer shareMemberLevel = customerMemberService.getMemberLevel(shareId);
        Integer orderMemberLevel = customerMemberService.getMemberLevel(customerId);

        //邀请人必须是店帮主  被邀人不能是店帮主
        return shareMemberLevel.equals(MemberLevelEnum._20.getCode()) && !orderMemberLevel.equals(MemberLevelEnum._20.getCode());
    }
}
