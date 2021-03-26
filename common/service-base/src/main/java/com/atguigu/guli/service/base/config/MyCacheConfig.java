package com.atguigu.guli.service.base.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;


@Configuration
public class MyCacheConfig {

    @Bean(value = "myGenerator")
    public KeyGenerator keyGenerator(){
        return new KeyGenerator(){

            @Override
            public Object generate(Object target, Method method, Object... params) {

                StringBuilder key =new StringBuilder();

                key.append(method.getName());
                for (Object param : params) {
                    key.append("_").append(param.hashCode());
                }
                return key+"_"+ Arrays.asList(target).toString();
            }
        };
    }

}
