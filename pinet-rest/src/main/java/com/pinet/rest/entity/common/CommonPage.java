package com.pinet.rest.entity.common;

import lombok.Data;

/**
 * @program: xinjiang-shop-app
 * @description: 分页
 * @author: hhh
 * @create: 2022-12-13 10:44
 **/
public class CommonPage {
    private Integer pageNum;

    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
