package com.wx.genealogy.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @ClassName SwaggerConfig
 * @Author hangyi
 * @Data 2020/5/26 16:14
 * @Description swagger2配置类
 * @Version 1.0
 **/
@Configuration
//@EnableSwagger2WebMvc
//@ConditionalOnProperty(prefix = "swagger",value = {"enabled"},havingValue = "true")
@EnableSwagger2
@Profile("test")//配置只允许在测试的时候暴露所有的接口
public class SwaggerConfig {
    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("族谱接口服务")
                        .termsOfServiceUrl("https:/192.168.31.152:8089/")
                        .contact(new Contact("cnzcs","","1373747270@qq.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("族谱接口服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.wx.genealogy.system.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
/*
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.wx.genealogy.system.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(setHeaderToken())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTFUL APIS")
                .description("Mos项目后台API接口文档")
                .version("1.0")
                .build();
    }

    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

    //路径配置
    private static final String splitor = ";";

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }*/
}
