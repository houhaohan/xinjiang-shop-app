package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.CustomerBalanceRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 余额vo
 * @author: hhh
 * @create: 2023-09-13 15:44
 **/
@Data
public class BalanceVo {
    @ApiModelProperty("余额")
    private BigDecimal balance = BigDecimal.ZERO;

    private List<CustomerBalanceRecord> customerBalanceRecords;
}
