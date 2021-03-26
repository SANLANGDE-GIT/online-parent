package com.atguigu.guli.service.vod.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.vod")
@Data
public class VodProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String workFlowId;

}
