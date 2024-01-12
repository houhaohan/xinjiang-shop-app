package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author wlbz
 * @since 2024-01-10
 */
@Getter
@Setter
@ApiModel(value = "Coupon对象", description = "优惠券表")
public class Coupon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("优惠券类型，1-满减券，2-折扣券")
    @TableField("`type`")
    private Integer type;

    @ApiModelProperty("优惠券使用门槛")
    private BigDecimal usePrice;

    @ApiModelProperty("优惠券金额")
    private BigDecimal couponPrice;

    @ApiModelProperty("折扣比例,50% 存50")
    private Integer discount;

    @ApiModelProperty("优惠券总数")
    private Integer quantity;

    @ApiModelProperty("已领取数量")
    private Integer claimedNum;

    @ApiModelProperty("有效期类型  1.领取时间  2.固定失效时间")
    private Integer effectType;

    @ApiModelProperty("领取后过期天数")
    private Integer effectDay;

    @ApiModelProperty("优惠券生效时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;

    @ApiModelProperty("优惠券失效时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pastTime;

    @ApiModelProperty("1-全部店铺，2-部分店铺")
    private Integer useShop;

    @ApiModelProperty("1-全部商品，2-部分商品")
    private Integer useProduct;

    @ApiModelProperty("1-未开始，2-进行中，3-已失效")
    private Integer status;

    @ApiModelProperty("0-禁用，1-启用")
    private Integer disableFlag;

    @ApiModelProperty("优惠券领取类型 1.用户领取 2.时间周期")
    private Integer recType;

    @ApiModelProperty("限领数量")
    private Integer restrictNum;

    @ApiModelProperty("领取周期")
    private Integer recCycle;


}
