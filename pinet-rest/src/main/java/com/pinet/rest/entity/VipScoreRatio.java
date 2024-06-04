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
 * VIP积分比例
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_score_ratio")
@ApiModel(value = "VipScoreRatio对象", description = "VIP积分比例")
public class VipScoreRatio extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("VIP 等级ID")
    private Long levelId;

    @ApiModelProperty("VIP等级")
    private Integer level;

    @ApiModelProperty("VIP积分比率")
    private Double ratio;


}
