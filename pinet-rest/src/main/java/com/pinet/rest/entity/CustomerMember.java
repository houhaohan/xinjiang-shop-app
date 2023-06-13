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
 * 
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@Getter
@Setter
@TableName("customer_member")
@ApiModel(value = "CustomerMember对象", description = "")
public class CustomerMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long customerId;

    @ApiModelProperty("会员等级  10普通会员  20店帮主")
    private Integer memberLevel;


}
