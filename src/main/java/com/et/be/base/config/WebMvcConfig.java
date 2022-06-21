package com.et.be.base.config;


import com.et.be.online.constant.SysConfigConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * mvc配置
 *
 * @author yanpanyi
 * @date 2019/03/27
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( SysConfigConstant.DOC_DIR_PATH_PATTERNS)
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/",
                        "file:" +SysConfigConstant.SERVER_DIR+SysConfigConstant.DOC_DIR);
    }


}
