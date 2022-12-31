package com.pinet.rest.controller;

import com.pinet.common.file.util.OssUtil;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/file")
@RestController
public class FileController {
    @Autowired
    private OssUtil ossUtil;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @NotTokenSign
    public Result upload(@RequestParam("file") MultipartFile file){
        String upload = ossUtil.upload(file);
        return Result.ok("操作成功",upload);
    }
}
