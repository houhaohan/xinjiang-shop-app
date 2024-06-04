package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIP充值记录
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_recharge_record")
@ApiModel(value = "VipRechargeRecord对象", description = "VIP充值记录")
public class VipRechargeRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long customerId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("实际充值金额")
    private BigDecimal realAmount;

    @ApiModelProperty("赠送金额")
    private BigDecimal giftAmount;

    @ApiModelProperty("赠送优惠券")
    private Long giftCouponId;

    @ApiModelProperty("微信/支付宝 充值单号")
    private String outTradeNo;

    @ApiModelProperty("充值模板 ID")
    private Long templateId;

    @ApiModelProperty("充值状态，SUCCESS-成功，FAIL-失败")
    private String status;


}
