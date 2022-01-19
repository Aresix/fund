package com.fundMonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启用swagger2功能注解配置
 *
 * @author lli.chen
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fundMonitor.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 访问地址：http://项目实际地址/swagger-ui.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("中医九种体质接口")
                .description("1、第一步需要访问用户接口中的login 先登陆一下 否则会报 Full authentication is required to access this resource 错误\n" +
                        "2、新建时Id不要传！！！不要传！！！\n" +
                        "3、新建更新时有些字段可忽略，请参照 Model 中的注释\n" +
                        "4、文件获取地址 ： http://47.101.68.214:8090/resources/1574583200108/26.png")
                .build();
    }
}
