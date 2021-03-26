package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = "网站微信支付")
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    WeixinPayService weixinPayService;

    @GetMapping("/auth/create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        String remoteAddr = request.getRemoteAddr();
        Map map =weixinPayService.createNative(memberId,orderNo,remoteAddr);
        return R.ok().data(map);
    }
    ///api/trade/weixin-pay/notify
    @PostMapping("/notify")
    public String notify(HttpServletRequest request){
        return weixinPayService.doNotify(request);
    }

}
