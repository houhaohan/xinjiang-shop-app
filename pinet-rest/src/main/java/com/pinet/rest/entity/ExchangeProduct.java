package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 兑换商品表
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@Getter
@Setter
@TableName("exchange_product")
@ApiModel(value = "ExchangeProduct对象", description = "兑换商品表")
public class ExchangeProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("积分")
    private Integer score;

    @ApiModelProperty("状态 1在售  2停售")
    private Integer prodStatus;

    @ApiModelProperty("类型 1礼品  2优惠券")
    private Integer prodType;

    @ApiModelProperty("图片")
    private String imgUrl;

    @ApiModelProperty("优惠券id")
    private Long couponId;

    @ApiModelProperty("有效日期")
    @TableField(exist=false)
    private String expireTimeStr;


}
