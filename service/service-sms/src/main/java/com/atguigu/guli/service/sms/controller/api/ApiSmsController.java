package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.common.util.RandomUtils;
import com.atguigu.guli.service.base.constant.ServiceConstant;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

//@CrossOrigin
@RestController
@RequestMapping("/api/sms/")
@RefreshScope
public class ApiSmsController {

    @Autowired
    SmsService smsService;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("send/{mobile}")
    public R sendMsg(@PathVariable String mobile){

        boolean isMobile = FormUtils.isMobile(mobile);
        //验证手机号是否正确
        if(!isMobile)
            return R.setResult(ResultCodeEnum.LOGIN_PHONE_ERROR);
        //生成key:
        String key= ServiceConstant.REGISTER_CODE +mobile;
        //验证是否已经发送过短信
        Object o = redisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(o))
            return R.error().message("请勿重复发送！");
        //生成验证码
        String code = RandomUtils.getFourBitRandom();
        //发送验证码
        smsService.sendMsg(mobile,code);
        //set key 乱码：配置key 和 value 序列化  //Service-Base/Redis-config
        redisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
        return R.ok().message("短信发送成功！");
    }

    @GetMapping("/set")
    public R setKv(){
        redisTemplate.opsForValue().set("loginCode:15192458230", "123456", 5, TimeUnit.MINUTES);
        return R.ok();
    }

}
