package com.pinet.rest.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 地址管理表
* @TableName customer_address
*/
@Data
public class CustomerAddress implements Serializable {

    /**
    * 
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long customerId;
    /**
    * 省
    */
    @ApiModelProperty("省")
    private Integer provinceId;
    /**
    * 市
    */
    @ApiModelProperty("市")
    private Integer cityId;
    /**
    * 区
    */
    @ApiModelProperty("区")
    private Integer districtId;
    /**
    * 姓名
    */
    @ApiModelProperty("姓名")
    private String name;
    /**
    * 
    */
    @ApiModelProperty("")
    private String address;
    /**
    * 
    */
    @ApiModelProperty("")
    private String province;
    /**
    * 
    */
    @ApiModelProperty("")
    private String city;
    /**
    * 
    */
    @ApiModelProperty("")
    private String district;
    /**
    * 邮编
    */
    @ApiModelProperty("邮编")
    private String postcode;
    /**
    * 手机号码
    */
    @ApiModelProperty("手机号码")
    private String phone;
    /**
    * 电话
    */
    @ApiModelProperty("电话")
    private String tel;
    /**
    * 
    */
    @ApiModelProperty("")
    private Integer active;
    /**
    * 默认地址值为1
    */
    @ApiModelProperty("默认地址值为1")
    private Integer status;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long createTime;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long updateTime;
    /**
    * 经度
    */
    @ApiModelProperty("经度")
    private BigDecimal lng;
    /**
    * 纬度
    */
    @ApiModelProperty("纬度")
    private BigDecimal lat;
    /**
    * 门牌号
    */
    @ApiModelProperty("门牌号")
    private String houseNumber;
    /**
    * 地点名
    */
    @ApiModelProperty("地点名")
    private String addressName;

}
