package com.pinet.keruyun.openapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pinet.keruyun.openapi.config.KryApiParamConfig;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.type.KryAPI;
import com.pinet.keruyun.openapi.type.Method;
import com.pinet.keruyun.openapi.util.JsonUtil;
import com.pinet.keruyun.openapi.util.sign.SignDTO;
import com.pinet.keruyun.openapi.util.sign.SignUtil;
import com.pinet.keruyun.openapi.vo.KryResponse;
import com.pinet.keruyun.openapi.vo.ShopTokenVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zhaobo
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KryCallService {

    private final OkHttpClient okHttpClient;

    private final KryApiParamConfig kryApiParamConfig;

    @Cacheable(value = {"authTypeAndOrgIdMapToken"}, key = "#authType+'_'+#orgId")
    public String getToken(AuthType authType, Long orgId) {
        String responseStr = getCall(KryAPI.GET_SHOP_TOKEN, authType, orgId, kryApiParamConfig.getAppSecret());
        KryResponse<ShopTokenVO> response = JsonUtil.fromJson(responseStr, new TypeReference<KryResponse<ShopTokenVO>>() {
        });
        if (0 == response.getCode()) {
            return response.getResult().getToken();
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public String getCall(KryAPI api, AuthType authType, Long orgId, String token) {
        SignDTO dto = getSignDto(Method.GET, authType, orgId);
        Request request = new Request.Builder()
                .url(getUrlAndParamStr(api, dto, token))
                .addHeader(kryApiParamConfig.getProjectName(), kryApiParamConfig.getProjectVersion())
                .build();
        return execCall(request);
    }

    public String postCall(KryAPI api, AuthType authType, Long orgId, String token, Object body) {
        SignDTO dto = getSignDto(Method.POST, authType, orgId, body);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Objects.nonNull(body) ? JsonUtil.toJson(body) : "");
        Request request = new Request.Builder()
                .url(getUrlAndParamStr(api, dto, token))
                .addHeader(kryApiParamConfig.getProjectName(), kryApiParamConfig.getProjectVersion())
                .post(requestBody)
                .build();
        return execCall(request);
    }

    private String getUrlAndParamStr(KryAPI api, SignDTO dto, String token) {
        StringBuilder sb = new StringBuilder(kryApiParamConfig.getUrl())
                .append(api.getUri())
                .append("?appKey=").append(dto.getAppKey());
        switch (dto.getAuthType()) {
            case BRAND:
                sb.append("&brandId=").append(dto.getOrgId());
                break;
            default:
                sb.append("&shopIdenty=").append(dto.getOrgId());
        }
        sb.append("&version=2.0&timestamp=").append(dto.getTimestamp())
                .append("&sign=").append(SignUtil.sign(dto, token));
        return sb.toString();
    }

    private String execCall(Request request) {
        Response response = null;
        try {
//            log.info("kry_request:{}", JsonUtil.toJson(request));
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
//                log.info("kry_response:{}", responseStr);
                return responseStr;
            }
        } catch (Exception e) {
            log.error("OkHttpClient Error:{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("OkHttpClient Error:", e.getCause());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        throw new RuntimeException("OkHttpClient Error:" + response.message());
    }

    private SignDTO getSignDto(Method method, AuthType authType, Long orgId) {
        return getSignDto(method, authType, orgId, null);
    }

    private SignDTO getSignDto(Method method,AuthType  authType, Long orgId, Object requestBody) {
        return SignDTO.builder()
                .method(method)
                .authType(authType)
                .orgId(orgId)
                .appKey(kryApiParamConfig.getAppKey())
                .timestamp(System.currentTimeMillis() / 1000)
                .version("2.0")
                .requestBody(requestBody)
                .build();
    }

}
