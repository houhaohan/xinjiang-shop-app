package com.pinet.rest.entity.param;

import com.pinet.rest.entity.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel(value = "RecommendProductParam",description = "首页推荐商品参数")
public class RecommendProductParam extends CommonPage {

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "经度")
    private BigDecimal lng;
}
