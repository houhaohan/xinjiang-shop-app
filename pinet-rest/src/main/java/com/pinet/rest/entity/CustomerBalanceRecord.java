package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 资金流水表
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@Getter
@Setter
@TableName("customer_balance_record")
@ApiModel(value = "CustomerBalanceRecord对象", description = "资金流水表")
public class CustomerBalanceRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("变动金额")
    private BigDecimal money;

    @ApiModelProperty("1资源通任务增加   2充值增加   3轻食下单消费扣减  4提现扣减  5店帮主充值 ")
    private Integer type;

    @ApiModelProperty("关联业务外键")
    private Long fkId;


}
