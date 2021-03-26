package com.atguigu.guli.service.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

//jdbc依赖引入项目后，springboot的自动配置试图在配置文件中查找jdbc相关配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //主类上添加注解exclude属性，禁用jdbc自动配置
@ComponentScan(basePackages = {"com.atguigu.guli"})
@EnableDiscoveryClient
public class ServiceOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class,args);
    }
}
