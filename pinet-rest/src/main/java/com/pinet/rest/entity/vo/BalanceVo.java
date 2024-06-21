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
    private Amount balance;

    private List<CustomerBalanceRecord> customerBalanceRecords;

    /**
     * 会员门店余额
     */
    @Data
    public static class Amount{
        @ApiModelProperty("门店ID")
        private Long shopId;

        @ApiModelProperty("门店名称")
        private String shopName;

        @ApiModelProperty("可用余额")
        private BigDecimal amount;

    }
}
