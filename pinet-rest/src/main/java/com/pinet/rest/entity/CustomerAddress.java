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
    @NotNull(message="[省]不能为空")
    @ApiModelProperty("省")
    private Integer provinceId;
    /**
    * 市
    */
    @NotNull(message="[市]不能为空")
    @ApiModelProperty("市")
    private Integer cityId;
    /**
    * 区
    */
    @NotNull(message="[区]不能为空")
    @ApiModelProperty("区")
    private Integer districtId;
    /**
    * 姓名
    */
    @NotBlank(message="[姓名]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("姓名")
    @Length(max= 64,message="编码长度不能超过64")
    private String name;
    /**
    * 
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("")
    @Length(max= 255,message="编码长度不能超过255")
    private String address;
    /**
    * 
    */
    @NotBlank(message="[]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("")
    @Length(max= 32,message="编码长度不能超过32")
    private String province;
    /**
    * 
    */
    @NotBlank(message="[]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("")
    @Length(max= 128,message="编码长度不能超过128")
    private String city;
    /**
    * 
    */
    @NotBlank(message="[]不能为空")
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("")
    @Length(max= 128,message="编码长度不能超过128")
    private String district;
    /**
    * 邮编
    */
    @Size(max= 12,message="编码长度不能超过12")
    @ApiModelProperty("邮编")
    @Length(max= 12,message="编码长度不能超过12")
    private String postcode;
    /**
    * 手机号码
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("手机号码")
    @Length(max= 32,message="编码长度不能超过32")
    private String phone;
    /**
    * 电话
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("电话")
    @Length(max= 32,message="编码长度不能超过32")
    private String tel;
    /**
    * 
    */
    @NotNull(message="[]不能为空")
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
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long createTime;
    /**
    * 
    */
    @NotNull(message="[]不能为空")
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
    @Size(max= 256,message="编码长度不能超过256")
    @ApiModelProperty("门牌号")
    @Length(max= 256,message="编码长度不能超过256")
    private String houseNumber;
    /**
    * 地点名
    */
    @Size(max= 256,message="编码长度不能超过256")
    @ApiModelProperty("地点名")
    @Length(max= 256,message="编码长度不能超过256")
    private String addressName;

}
