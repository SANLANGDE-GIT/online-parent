package com.atguigu.guli.service.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssProperties {
    private String bucketName;
    private String scheme;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
}
