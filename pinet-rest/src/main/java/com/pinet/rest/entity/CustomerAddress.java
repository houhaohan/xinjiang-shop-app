package com.pinet.rest.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 地址管理表
* @TableName customer_address
*/
@Data
public class CustomerAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
    * 
    */
    @ApiModelProperty("用户ID")
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
    * 地址
    */
    @ApiModelProperty("地址")
    private String address;
    /**
    * 省份
    */
    @ApiModelProperty("省份")
    private String province;
    /**
    * 城市
    */
    @ApiModelProperty("城市")
    private String city;
    /**
    * 
    */
    @ApiModelProperty("地区")
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
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Long createTime;
    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
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
