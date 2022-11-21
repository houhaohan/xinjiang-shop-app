package com.pinet.core.page.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pinet.core.page.PageResult;

public class PageConvert {
    public static PageResult convert(IPage page){
        PageResult result = new PageResult();
        result.setPageNum((int)page.getCurrent());
        result.setPageSize((int)page.getSize());
        result.setTotalSize(page.getTotal());
        result.setTotalPages((int)page.getPages());
        result.setContent(page.getRecords());
        return result;
    }
}
