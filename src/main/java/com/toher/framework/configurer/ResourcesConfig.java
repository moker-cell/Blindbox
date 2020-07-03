package com.toher.framework.configurer;

import com.toher.framework.interceptor.ApiUserInterceptor;
import com.toher.framework.interceptor.OriginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/18 10:05
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    /** 注入自定义配置 */
    @Resource
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 头像上传路径 */
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + readApplicationYmlUtil.getUploadPath());
        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /** 构造方法注入 */
    private OriginInterceptor originInterceptor;
    private ApiUserInterceptor apiUserInterceptor;
    @Autowired
    public ResourcesConfig(OriginInterceptor originInterceptor, ApiUserInterceptor apiUserInterceptor){
        this.originInterceptor = originInterceptor;
        this.apiUserInterceptor = apiUserInterceptor;
    }

    /** 接口拦截 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截/api/**，过滤/api/wx/**
        registry.addInterceptor(originInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/wx/**");
        registry.addInterceptor(apiUserInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/wx/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}