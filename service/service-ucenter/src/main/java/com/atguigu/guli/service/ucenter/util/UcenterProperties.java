package com.atguigu.guli.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.open")
public class UcenterProperties {
    //# 微信开放平台 appid
    private String appId;//: wxed9954c01bb89b47
    //# 微信开放平台 appsecret
    private String appSecret;//: a7482517235173ddb4083788de60b90e
    //# 微信开放平台 重定向url（guli.shop需要在微信开放平台配置）
    private String redirectUri;//: http://guli.shop/api/ucenter/wx/callback8160
}
