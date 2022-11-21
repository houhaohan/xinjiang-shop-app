package com.pinet.inter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;

@Component
public class BackendInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(BackendInterceptor.class);
    public static final String GATEWAY_TOKEN = "GATEWAY_TOKEN";
    @Value("${spring.application.name}")
    private String applicationName;
    @Resource
    public StringRedisTemplate stringRedisTemplate;

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String reqGatewayToken = request.getHeader("originToken");
//        String gatewayToken = stringRedisTemplate.opsForValue().get(GATEWAY_TOKEN);
//        logger.info("<<< 服务端检查是否允许访问{}", request.getRequestURI());
//        if (StringUtil.isEmpty(reqGatewayToken) || !reqGatewayToken.equals(gatewayToken)) {
//            if (handler instanceof HandlerMethod) {
//                Class clazz = ((HandlerMethod) handler).getBean().getClass();
//                if (clazz.getInterfaces() != null && clazz.getInterfaces().length > 0) {
//                    long count = Arrays.stream(clazz.getInterfaces()).filter(x -> x.getAnnotationsByType(FeignClient.class) != null).count();
//                    if(count>0){
//                        return true;
//                    }
//                }
//            }
//            response.setContentType("application/json;charset=UTF-8");
//            response.setCharacterEncoding("utf8");
//            response.getWriter().write(JSON.toJSONString(HttpResult.error(403, "系统没有权限访问")));
//            logger.warn("{}系统没有直接访问权限，请检查是否允许需要通过网关直接访问", request.getRequestURI());
//            return false;
//        }
//        logger.info(">>> 服务端允许访问:{}", request.getRequestURI());
//        return true;
//    }
}
