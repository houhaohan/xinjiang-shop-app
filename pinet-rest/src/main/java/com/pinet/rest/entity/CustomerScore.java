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
 * 用户积分表
 * </p>
 *
 * @author wlbz
 * @since 2024-06-06
 */
@Getter
@Setter
@TableName("customer_score")
@ApiModel(value = "CustomerScore对象", description = "用户积分表")
public class CustomerScore extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    private Long customerId;

    @ApiModelProperty("积分")
    private Double score;


}
