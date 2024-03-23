package com.pinet.core.util;

import com.pinet.core.entity.BaseEntity;
import com.pinet.core.enums.ApiExceptionEnum;
import com.pinet.core.exception.PinetException;

import java.util.List;
import java.util.Objects;


/**
 * @description 过滤工具类
 * @author chengshuanghui
 * @data 2024-03-23 12:00
 */
public class FilterUtil  {

    /**
     * 根据 id 过滤集合满足条件的元素
     * @param list
     * @param id
     * @param e
     * @return
     */
    public static <T extends BaseEntity> T filter(List<T> list, Long id, ApiExceptionEnum e){
        return list.stream()
                .filter(o-> Objects.equals(o.getId(),id))
                .findFirst()
                .orElseThrow(()-> new PinetException(e));

    }


}
