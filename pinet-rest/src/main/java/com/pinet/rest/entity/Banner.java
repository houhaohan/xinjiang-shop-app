package com.pinet.rest.entity;

import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * banner表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-14
 */
@Getter
@Setter
@ApiModel(value = "Banner对象", description = "banner表")
public class Banner extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片路径")
    private String imgUrl;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("0启用 1禁用")
    private String status;


}
