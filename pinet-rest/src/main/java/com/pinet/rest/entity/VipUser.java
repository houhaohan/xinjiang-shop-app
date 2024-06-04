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
 * VIP用户
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_user")
@ApiModel(value = "VipUser对象", description = "VIP用户")
public class VipUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户 ID")
    private Long customerId;

    @ApiModelProperty("客如云用户ID")
    private String kryCustomerId;

    @ApiModelProperty("性别，0代表女,1代表男,2代表其他")
    private Integer sex;

    @ApiModelProperty("VIP等级，1-VIP1,2-VIP2,3-VIP3,4-VIP4,5-VIP5")
    @TableField("`level`")
    private Integer level;

    @ApiModelProperty("VIP名称")
    private String vipName;

    @ApiModelProperty("会员手机号")
    private String phone;

    @ApiModelProperty("入会门店ID")
    private Long shopId;

    @ApiModelProperty("用户状态,1-启用,0-停用")
    private Integer status;


}
