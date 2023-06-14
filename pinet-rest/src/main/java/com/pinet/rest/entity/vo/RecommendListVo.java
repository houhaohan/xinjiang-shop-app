package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.bo.RecommendTimeBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 推荐记录
 * @author: hhh
 * @create: 2023-06-14 15:23
 **/
@Data
public class RecommendListVo {
    @ApiModelProperty(value = "年月日",name = "date")
    private String date;


    @ApiModelProperty(value = "时分秒",name = "date")
    private List<RecommendTimeBo> recommendTimeBos;

}
