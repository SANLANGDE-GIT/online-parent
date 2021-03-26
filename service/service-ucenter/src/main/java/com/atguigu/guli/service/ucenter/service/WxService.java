package com.atguigu.guli.service.ucenter.service;

public interface WxService {
    String getAccessTokenContent(String accessTokenUrl);

    String getMemberByOpenId(String openId,String accessToken,String userInfoUrl);
}
