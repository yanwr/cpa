package br.com.softplan.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("br.com.softplan"))
                .paths(PathSelectors.any()).build().globalOperationParameters(
                    Collections.singletonList(
                            new ParameterBuilder()
                                .name("Authorization")
                                .description("Header Token JWT")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()
                    )
                );
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("Usual Process Api Documentation")
                .description("This page shows all micro-services offered by Usual Process")
                .version("1.0")
                .contact(new Contact("Usual Process", "https://usualprocess.com.br", "usualprocess@gmail.com"))
                .license("License 2.0")
                .licenseUrl("https://usualprocess.com.br/license.html").build();
    }
}