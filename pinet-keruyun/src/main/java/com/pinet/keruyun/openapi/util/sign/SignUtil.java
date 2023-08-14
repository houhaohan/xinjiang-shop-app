package com.pinet.keruyun.openapi.util.sign;

import com.pinet.keruyun.openapi.type.Method;
import com.pinet.keruyun.openapi.util.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author zhaobo
 */
@Slf4j
public class SignUtil {
    private static final String APP_KER_ERROR = "Sign Error:appKey must exist";
    private static final String BRAND_OR_SHOP_ERROR = "Sign Error:brandId and shopIdenty only one can exist";
    private static final String VERSION_ERROR = "Sign Error:version must exist";
    private static final String TIMESTAMP_ERROR = "Sign Error:timestamp must exist";
    private static final String TOKEN_ERROR = "Sign Error:token must exist";
    private static final String SIGN_PARAM_STR_LOG_FORMAT = "sign param str:%s";
    private static final String SIGN_STR_LOG_FORMAT = "sign  str:%s";

    private SignUtil() {
    }

    public static String sign(SignDTO dto, String token) {
        validParam(dto, token);
        String signParamStr = getSignStr(dto, token);
        log.info(String.format(SIGN_PARAM_STR_LOG_FORMAT, signParamStr));
        String signStr = getSign(signParamStr);
        log.info(String.format(SIGN_STR_LOG_FORMAT, signStr));
        return signStr;
    }

    @SneakyThrows
    private static String getSign(String signStr) {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(signStr.getBytes());
        byte byteBuffer[] = messageDigest.digest();
        StringBuffer strHexString = new StringBuffer();
        for (int i = 0; i < byteBuffer.length; i++) {
            String hex = Integer.toHexString(0xff & byteBuffer[i]);
            if (hex.length() == 1) {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }
        // 得到返回結果
        String SHA256Sign = strHexString.toString();
        return SHA256Sign;
    }

    private static String getSignStr(SignDTO dto, String token) {
        Map<String, Object> params = new TreeMap<>();
        params.put("appKey", dto.getAppKey());
        switch (dto.getAuthType()) {
            case BRAND:
                params.put("brandId", dto.getOrgId());
                break;
            default:
                params.put("shopIdenty", dto.getOrgId());
        }
        params.put("version", dto.getVersion());
        //时间戳s
        params.put("timestamp", dto.getTimestamp());
        StringBuilder sortedParams = new StringBuilder();
        params.entrySet().stream().forEachOrdered(paramEntry -> sortedParams.append(paramEntry.getKey()).append(paramEntry.getValue()));
        if (Method.POST.equals(dto.getMethod()) && Objects.nonNull(dto.getRequestBody())) {
            //若存在requestBody则添加body+body体json字符串 否则忽略
            sortedParams.append("body").append(JsonUtil.toJson(dto.getRequestBody()));
        }
        //请替换成真实的token
        sortedParams.append(token);

        return sortedParams.toString();
    }

    private static void validParam(SignDTO dto, String token) {
        Assert.notNull(dto.getAppKey(),APP_KER_ERROR);
        Assert.notNull(dto.getOrgId(),BRAND_OR_SHOP_ERROR);
        Assert.isTrue(StringUtils.isNoneBlank(dto.getVersion()),VERSION_ERROR);
        Assert.notNull(dto.getTimestamp(),TIMESTAMP_ERROR);
        Assert.isTrue(StringUtils.isNoneBlank(token),TOKEN_ERROR);
    }

}
