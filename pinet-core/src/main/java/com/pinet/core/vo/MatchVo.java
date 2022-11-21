package com.pinet.core.vo;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatchVo {

    private Long id;
    private String handicap;
    private String handicapTeam;
    private Boolean isNotLose;
    private String name;
    private Long playTime;
    private String threshold;
    private String victoryOddId;
    private List<MatchOddVo> odds = new ArrayList<>();
    private NormalVo status;
    private NormalVo type;
    private Long scheduleId;


}
