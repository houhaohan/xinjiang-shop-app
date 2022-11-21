package com.pinet.core.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GsTournamentDetailVo {

    /**
     * 赛事
     */
    private GsTournamentInfoVo tournament;

    /**
     * 赛程
     */
    private GsTournamentScheduleInfoVo schedule;

    /**
     * 盘口
     */
    private List<MatchVo> match = new ArrayList<>();
}
