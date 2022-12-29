package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.omg.CORBA.IDLType;

/**
 * <p>
 * 默认地址
 * </p>
 *
 * @author wlbz
 * @since 2022-12-29
 */
@Getter
@Setter
@TableName("default_address")
@ApiModel(value = "DefaultAddress对象", description = "默认地址")
public class DefaultAddress {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(type= IdType.AUTO)
    private Long id;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("地址")
    private String address;


}
