package com.atguigu.guli.infrastructure.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class InfrastructureApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApiGatewayApplication.class,args);
    }
    //跨域配置
    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration config = new CorsConfiguration();
        //CorsConfiguration 相关配置说明
        // 是否允许携带cookies
        //private Boolean allowCredentials;
        config.setAllowCredentials(true);
        // 允许的请求源
        // private List<String> allowedOrigins;
        config.addAllowedOrigin("*");
        // 允许的http方法
        //private List<String> allowedMethods;
        config.addAllowedMethod("*");
        // 允许的请求头
        //private List<String> allowedHeaders;
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(configSource);
    }
}
