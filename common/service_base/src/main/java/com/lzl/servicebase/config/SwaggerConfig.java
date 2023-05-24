package com.lzl.servicebase.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                //调用apiInfo方法,创建一个ApiInfo实例,
                // 里面是展示在文档页面信息内容
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }


    // api文档的详细信息
    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("API文档接口测试")    //标题
                .description("本文档描述接口测试用例")  //描述
                .version("1.0")  //版本
                .contact(new Contact("java", "http://baidu.com", "123@qq.com"))
                .build();
    }
}