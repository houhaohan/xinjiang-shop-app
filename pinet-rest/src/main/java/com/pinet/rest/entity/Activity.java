package com.pinet.rest.entity;

import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 推广活动（已弃用）
 * </p>
 *
 * @author wlbz
 * @since 2022-11-21
 */
@Getter
@Setter
@ApiModel(value = "Activity对象", description = "推广活动（已弃用）")
public class Activity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String code;

    private Integer isNewUser;

    private String trigger;

    @ApiModelProperty("1为自己，2为好友")
    private Integer target;

    private String plans;

    private Integer publishTimes;

    private Long adminId;

    private Integer isDeleted;


}
