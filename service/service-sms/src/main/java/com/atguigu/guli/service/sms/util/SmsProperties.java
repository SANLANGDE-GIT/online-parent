package com.atguigu.guli.service.sms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
public class SmsProperties {

    private String regionId;//: cn-hangzhou
    private String accessKeyId;//:
    private String accessKeySecret;//:
    private String templateCode;//: SMS_207963792
    private String signName;//: 美年旅游

}
