package com.pinet.core.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchOddVo {
    private Long id;
    private String  currency;
    private Boolean isNotLose;
    private BigDecimal odd;
    private NormalVo status;
    private TeamInfoVo team;
}
