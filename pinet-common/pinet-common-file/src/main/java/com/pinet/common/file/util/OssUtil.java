package com.pinet.common.file.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @program: xinjiang-shop-app
 * @description: oss
 * @author: hhh
 * @create: 2022-12-12 09:45
 **/
@Component
@Slf4j
public class OssUtil {
    @Value("${oss.qiniu.access_key}")
    private String accessKey;

    @Value("${oss.qiniu.secret_key}")
    private String secretKey;

    @Value("${oss.qiniu.bucket}")
    private String bucket;

    @Value("${oss.qiniu.url}")
    private String url;

    private static final String IMG_PATH = "qingshi/img/";

    private static final String VIDEO_PATH = "qingshi/video/";


    public String upload(MultipartFile file) {
        try {
            return upload(file.getInputStream(), FileUtil.getSuffix(file.getOriginalFilename()));
        } catch (Exception e) {
            log.error("文件上传失败{}", e);
            return null;
        }

    }


    public String upload(InputStream inputStream, String suffix) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);


        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = IMG_PATH + IdUtil.simpleUUID()+"."+suffix;

        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);
            log.info(JSONObject.toJSONString(putRet));
            return url + "/" + putRet.key;

        } catch (Exception e){
            log.error("文件上传失败{}",e);
        }
        return null;
    }

}
