package com.atguigu.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    SmsProperties smsProperties;

    @Override
    public void sendMsg(String mobile, String code) {
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionId(),
                smsProperties.getAccessKeyId(),
                smsProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        Map<String, String> map = new HashMap<>();
        map.put("code",code);
        //将包含验证码的集合转换为json字符串
        Gson gson = new Gson();
        request.putQueryParameter("TemplateParam",gson.toJson(map));
        try {
            CommonResponse response = client.getCommonResponse(request);

            String data = response.getData();
            Map<String,String> params = gson.fromJson(data, Map.class);
            String success = params.get("Code");
            String message = map.get("Message");
            //配置参考：短信服务->系统设置->国内消息设置
            //错误码参考：
            //https://help.aliyun.com/document_detail/101346.html?spm=a2c4g.11186623.6.613.3f6e2246sDg6Ry
            //控制所有短信流向限制（同一手机号：一分钟一条、一个小时五条、一天十条）
            if ("isv.BUSINESS_LIMIT_CONTROL".equals(success)) {
                log.error("短信发送过于频繁 " + "【code】" + success + ", 【message】" + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
            }

            if (!"OK".equals(success)) {
                log.error("短信发送失败 " + " - code: " + success + ", message: " + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }
}
