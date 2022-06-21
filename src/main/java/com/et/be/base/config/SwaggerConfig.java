package com.et.be.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * swagger会帮组我们生成文档
     * 1. 配置生成的文档信息
     * 2. 配置生成的规则
     */

    /*Docket封装接口文档信息*/
    @Bean
    public Docket getDocket() {

        // 创建封面信息对象
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();

        // 设置文档标题、描述、联系人
        apiInfoBuilder.title("平台接口说明文档")
                .description("此文档说明了平台后端接口规范")
                .version("v 1.0.0")
                .contact(new Contact("chen_cheng", "www.chen_cheng.com","chen_cheng@qq.com"));

        ApiInfo apiInfo = apiInfoBuilder.build();

        // 指定文档风格
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo) // 指定生成的文档中的封面信息；文档标题、版本、作者
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.et.be"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }
}

