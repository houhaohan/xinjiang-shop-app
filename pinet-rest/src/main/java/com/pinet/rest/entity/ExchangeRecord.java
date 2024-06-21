package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 兑换记录
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@Getter
@Setter
@TableName("exchange_record")
@ApiModel(value = "ExchangeRecord对象", description = "兑换记录")
public class ExchangeRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("兑换人id")
    private Long customerId;

    @ApiModelProperty("兑换商品id")
    private Long exchangeProductId;

    @ApiModelProperty("积分")
    private Double score;

    @ApiModelProperty("类型 1礼品  2优惠券")
    private Integer prodType;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("兑换数量")
    private Integer exchangeNum;

    @ApiModelProperty("门店id")
    private Long shopId;

    @ApiModelProperty("门店名称")
    private String shopName;


}
