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
 * 积分明细
 * </p>
 *
 * @author wlbz
 * @since 2023-12-22
 */
@Getter
@Setter
@TableName("score_record")
@ApiModel(value = "ScoreRecord对象", description = "积分明细")
public class ScoreRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("积分明细标题")
    private String scoreTitle;

    @ApiModelProperty("积分")
    private Integer score;

    @ApiModelProperty("外键id")
    private Long fkId;

    @ApiModelProperty("积分类型   1下单   2退款  3兑换")
    private Integer scoreType;


}
