package com.pinet.core.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class ReqResUtils {


    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null)
            return null;
        else
            return ((ServletRequestAttributes) requestAttributes);
    }

    /**
     * 获得request 对象
     *
     * @return
     */
    public static HttpServletRequest request() {
        ServletRequestAttributes servletRequestAttributes = getRequestAttributes();

        return servletRequestAttributes == null ? null : servletRequestAttributes.getRequest();
    }

    /**
     * 获取response对象
     *
     * @return
     */
    public static HttpServletResponse response() {
        ServletRequestAttributes servletRequestAttributes = getRequestAttributes();
        return servletRequestAttributes == null ? null : servletRequestAttributes.getResponse();
    }

    public static void toJson(HttpServletResponse response, Object rs) throws IOException {
        response.setCharacterEncoding(CharsetUtils.UTF_8);
        response.setContentType(CharsetUtils.CONTENT_JSON);
        response.getWriter().write(JSON.toJSONString(rs));
    }

    public static byte[] getRequestBytes(HttpServletRequest request) {
        try {
            return IoUtil.readBytes(request.getInputStream());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得ip
     *
     * @param req
     * @return
     */
    @Nullable
    public static String getIp(HttpServletRequest req) {
        String ip = "";
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        for (String header : headers) {
            ip = req.getHeader(header);
            if (false == NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }
        ip = req.getRemoteAddr();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }

    /**
     * 获取服务端ip
     *
     * @param req
     * @return
     */
    private static String _getServerIp(HttpServletRequest req) {
        return req.getRemoteHost();
    }

    private static String _getServerName(HttpServletRequest req) {
        return req.getServerName();
    }

    public static String getServerIp() {
        return _getServerIp(request());
    }

    public static String getServerName() {
        return _getServerName(request());
    }

    /**
     * 获得请求参数
     *
     * @param req
     * @return
     * @throws IOException
     */
    public static String getReqParam(HttpServletRequest req) throws IOException {
        return getRequestStr(req, getRequestBytes(req));
    }


    public static String getRequestStr(HttpServletRequest request, byte[] buffer) throws IOException {
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = CharsetUtils.UTF_8;
        }
        String str = new String(buffer, charEncoding).trim();
        if (StringUtil.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = request.getParameter(key);
                StringUtil.appendBuilder(sb, key, "=", value, "&");
            }
            str = StringUtil.removeSuffix(sb.toString(), "&");
        }
        return str.replaceAll("&amp;", "&");
    }

}
