package com.pinet.rest.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * swagger3 配置类
 * 访问路径ip:port/doc.html
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class Swagger3Config {

    /**
     * 配置Swagger的Docket的bean实例
     *
     * @param environment 当前环境
     * @return Docket
     */
    @Bean
    public Docket docket(Environment environment) {
        // 设置要配置的Swagger环境
        Profiles p = Profiles.of("local", "dev", "test","prod");
        boolean flag = environment.acceptsProfiles(p);
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置Swagger信息
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        String projectName = System.getProperty("user.dir");
        return new ApiInfoBuilder()
                .title("未来巴扎 API接口文档")
                .contact(new Contact("未来巴扎", null, "xxxxxxxx@qq.com"))
                .version("1.0.0")
                .description("API文档")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    /**
     * 解决springboot升级到2.6.x之后，swagger与actuator路径冲突解决
     *
     * @param webEndpointsSupplier        the web endpoints supplier
     * @param servletEndpointsSupplier    the servlet endpoints supplier
     * @param controllerEndpointsSupplier the controller endpoints supplier
     * @param endpointMediaTypes          the endpoint media types
     * @param corsProperties              the cors properties
     * @param webEndpointProperties       the web endpoint properties
     * @param environment                 the environment
     * @return the web mvc endpoint handler mapping
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
            ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
            CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties,
            Environment environment) {

        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping, null);
    }

    /**
     * shouldRegisterLinksMapping
     *
     * @param webEndpointProperties webEndpointProperties
     * @param environment           environment
     * @param basePath              /
     * @return boolean
     */
    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties,
                                               Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
