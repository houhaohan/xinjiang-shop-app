package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.Area;
import lombok.Data;

import java.util.List;


@Data
public class AreaPyGroupVo {

    /**
     * 首字母
     */
    private String letter;

    /**
     * 城市列表
     */
    private List<Area> citys;
}
