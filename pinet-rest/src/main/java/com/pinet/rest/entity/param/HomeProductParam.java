package com.pinet.rest.entity.param;

import com.pinet.core.constants.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@ApiModel(value = "HomeProductParam",description = "首页热卖排行版参数")
@Data
public class HomeProductParam {

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat = new BigDecimal(CommonConstant.DEFAULT_LAT);

    @ApiModelProperty(value = "经度")
    private BigDecimal lng = new BigDecimal(CommonConstant.DEFAULT_LNG);
}
