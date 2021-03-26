package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.service.WxService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.java.emoji.EmojiConverter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class WxServiceImpl implements WxService {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    MemberService memberService;

    @Override
    public String getAccessTokenContent(String accessTokenUrl) {
        HttpClientUtils httpClient= new HttpClientUtils(accessTokenUrl);
        try {
            httpClient.get();
            return httpClient.getContent();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
    }

    @Override
    public String getMemberByOpenId(String openId, String accessToken,String userInfoUrl) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openId);

        Member member = memberMapper.selectOne(queryWrapper);
        String nickname="";
        if(member==null){
            String userContent = getUserContent(userInfoUrl);
            Gson gson = new Gson();
            HashMap<String, Object> resultUserInfoMap = gson.fromJson(userContent, HashMap.class);
            if(resultUserInfoMap.get("errcode") != null){
                log.error("获取用户信息失败" + "，message：" + resultUserInfoMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            nickname = (String)resultUserInfoMap.get("nickname");
            String headimgurl = (String)resultUserInfoMap.get("headimgurl");
            Double sex = (Double)resultUserInfoMap.get("sex");

            //用户注册
            member = new Member();
            member.setOpenid(openId);
            member.setNickname(EmojiConverter.getInstance().toHtml(nickname));
            member.setAvatar(headimgurl);
            member.setSex(sex.intValue());
            memberService.save(member);
        }
        JwtInfo jwtInfo=new JwtInfo();
        jwtInfo.setNickname(nickname);
        jwtInfo.setId(member.getId());
        jwtInfo.setAvatar(member.getAvatar()==null?"http://cr-0821-guli-file.oss-cn-shanghai.aliyuncs.com/avatar/2020/12/28/3800400e34124f1c80cd404bcf1fd187.jpg".toString():member.getAvatar());
        return JwtHelper.createToken(jwtInfo);
    }

    private String getUserContent(String userInfoUrl) {
        HttpClientUtils httpClient= new HttpClientUtils(userInfoUrl);
        try {
            httpClient.get();
            return httpClient.getContent();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }
}
