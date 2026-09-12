package com.zqc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("mybatis-plus 用户服务 API")
                        .description("MyBatis-Plus 练习工程接口文档")
                        .version("v1.0.0")
                        .contact(new Contact().name("zqc")));
    }
}
