package com.pinet.rest.entity.dto;

import com.pinet.core.page.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: xinjiang-shop-app
 * @description: 资金变动明细dto
 * @author: hhh
 * @create: 2023-09-13 16:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BalanceRecordListDto extends PageRequest {
    @ApiModelProperty("0全部  1支出  2收入")
    private Integer type;

    @ApiModelProperty("内部封装不用传")
    private Long customerId;

}
