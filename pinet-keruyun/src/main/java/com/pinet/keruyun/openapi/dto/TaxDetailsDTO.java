package com.pinet.keruyun.openapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaxDetailsDTO {

    /**
     * 单笔uuid
     */
    private String uuid;
    /**
     * 税费类型 1：商品税 2：附加税
     */
    private Integer type;
    /**
     * 商品原价
     */
    private BigDecimal originalPrice;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     *税费
     */
    private BigDecimal taxAmount;
    /**
     * 税费id
     */
    private String taxId;
    /**
     * 税费描述
     */
    private String taxDesc;
    /**
     * 税务包含类型
     */
    private Integer includeType;
    /**
     * 服务端创建时间
     */
    private String createTime;


    public TaxDetailsDTO() {
    }
}
