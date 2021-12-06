package com.foodapp.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
//    @Autowired
//    GatewayInterceptor gatewayInterceptor;

    @Value("${pathurl}")
    private String root;

//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        registry.addInterceptor(gatewayInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry
                .addResourceHandler("/image/**")
                .addResourceLocations("file:" + root +"/");
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

}
