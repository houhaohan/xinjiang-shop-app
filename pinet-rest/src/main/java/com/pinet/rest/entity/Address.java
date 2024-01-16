package com.pinet.rest.entity;

import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地址表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-09
 */
@Getter
@Setter
@ApiModel(value = "Address对象", description = "地址表")
public class Address{

    private static final long serialVersionUID = 1L;
    private Integer id;

    private String name;

    private Integer parentId;

    private String shortName;

    private Integer level;

    @ApiModelProperty("地址首字母")
    private String pinyin;

    @ApiModelProperty("是否删除")
    private Integer isDeleted;

    @ApiModelProperty("下次版本发部的地址名称")
    private String updateName;

    private Integer updateParentId;

    private String updateShortName;

    private Integer updateLevel;

    private String updatePinyin;

    private Integer updateIsDeleted;

    @ApiModelProperty("地址全称")
    private String address;


}
