package com.pinet.rest.entity;

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
 * @since 2023-11-27
 */
@Getter
@Setter
@ApiModel(value = "Label对象", description = "")
public class Label extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签")
    private String labelName;


}
