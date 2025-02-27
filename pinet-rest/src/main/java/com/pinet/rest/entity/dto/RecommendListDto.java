package com.pinet.rest.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.core.page.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 推荐记录dto
 * @author: hhh
 * @create: 2023-06-14 17:02
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendListDto extends PageRequest {
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "查询时间")
    private Date queryDate;

    @ApiModelProperty(value = "内部封装参数")
    private Long customerId;
}
