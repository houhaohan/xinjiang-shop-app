package com.pinet.core.vo;

import lombok.Data;

@Data
public class GsTournamentScheduleInfoVo {

    private Long id;
    private Integer category;
    private Integer haveDataSource;
    private Boolean haveGuess;
    private Long playTime;
    private String round;
    private NormalVo status;
    private MatchTeamVo left;
    private MatchTeamVo right;
}
