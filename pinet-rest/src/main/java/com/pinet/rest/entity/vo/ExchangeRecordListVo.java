package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 兑换记录vo
 * @author: hhh
 * @create: 2024-01-17 10:33
 **/
@Data
public class ExchangeRecordListVo {
    private Long id;

    private String prodName;

    private String imgUrl;

    private Integer score;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
