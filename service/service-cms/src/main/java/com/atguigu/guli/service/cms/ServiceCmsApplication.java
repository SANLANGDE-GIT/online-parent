package com.atguigu.guli.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.atguigu.guli"})
@EnableFeignClients
@EnableCaching //开启缓存注解配置
public class ServiceCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsApplication.class,args);
    }
}
