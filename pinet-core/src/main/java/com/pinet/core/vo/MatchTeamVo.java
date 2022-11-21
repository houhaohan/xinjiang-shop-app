package com.pinet.core.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchTeamVo {
    private Boolean isSupport;
    private Boolean isVictory;
    private Integer score;
    private BigDecimal supportRate;
    private TeamInfoVo team;
}
