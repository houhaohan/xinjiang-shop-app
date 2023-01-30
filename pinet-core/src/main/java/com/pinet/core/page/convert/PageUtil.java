package com.pinet.core.page.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.page.PageResult;

public class PageUtil {

    public static PageResult convert(Page page){
        PageResult result = new PageResult();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotalSize(page.getTotal());
        result.setTotalPages(page.getPages());
        result.setContent(page.getRecords());
        return result;
    }
}
