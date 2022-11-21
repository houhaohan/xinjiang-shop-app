package com.pinet.core.vo;

import lombok.Data;

@Data
public class GsTournamentInfoVo {

    /**
     * id
     */
    private Long id;

    /**
     * 赛事名称
     */
    private String name;

    /**
     * 赛事logo
     */
    private String logo;

    /**
     * 赛事开始时间
     */
    private Long startTime;

    /**
     * 赛事结束时间
     */
    private Long endTime;

    /**
     * 游戏分类 {code: 1, text: "红色警戒"}
     */
    private NormalVo category;

}
