package com.pinet.core.controller;

import com.pinet.core.http.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);


//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
//        {
//            @Override
//            public void setAsText(String text){
//                if (StringUti.is(text)) {
//                    setValue(null);
//                }else {
//                    setValue(DateUtil.parseDate(text));
//                }
//            }
//        });
//    }

    public HttpResult success() {
        return HttpResult.ok();
    }

    public HttpResult error() {
        return HttpResult.error();
    }

    public HttpResult success(String msg) {
        return HttpResult.ok(msg);
    }

    public HttpResult error(String msg) {
        return HttpResult.error(msg);
    }

}
