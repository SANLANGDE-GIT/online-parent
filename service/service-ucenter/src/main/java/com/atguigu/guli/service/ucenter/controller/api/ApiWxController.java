package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.service.base.constant.ServiceConstant;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.service.WxService;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

@Api(tags = "微信登录")
@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;

    @Autowired
    private WxService wxService;

    @ResponseBody
    @GetMapping("set")
    public R set(HttpSession session) {
        session.setAttribute("skey", "zhangsan");
        return R.ok();
    }

    @ResponseBody
    @GetMapping("get")
    public R get(HttpSession session) {
        System.out.println("session = " + session.getClass().getName());
        Object skey = session.getAttribute("skey");
        System.out.println("skey = " + skey);
        return R.ok();
    }

    @ApiOperation("获取二维码")
    @GetMapping("wxLogin")
    public String wxLogin(HttpSession session) {
        Object skey = session.getAttribute("skey");
        System.out.println("skey = " + skey);
        System.out.println("session = " + session.getClass().getName());
        System.out.println("session = " + session.getId());
        String redirectStr = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //处理回调url
        String redirecturi = "";
        try {
            redirecturi = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //处理state：生成随机数，存入session
        String state = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("生成 state = " + state);
        session.setAttribute(ServiceConstant.WX_OPEN_STATE, state);

        String qrcodeUrl = String.format(
                redirectStr,
                ucenterProperties.getAppId(),
                redirecturi,
                state
        );

        return "redirect:" + qrcodeUrl;
    }

    //http://localhost:8160/api/ucenter/wx/callback?code=091PbgGa1JgUfA0TtZGa12BEtQ3PbgGk&state=1b42aecd-450c-4e33-93f3-74a18b4af3dd
    //微信扫描登录确认回调接口
    @GetMapping("callback")
    public String callback(String code, String state, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object skey = session.getAttribute("skey");
        System.out.println("skey = " + skey);
        System.out.println("session = " + session.getClass().getName());
        System.out.println("session = " + session.getId());
        //回调被拉起，并获得code和state参数
        log.info("callback被调用");
        log.info("code = " + code);
        log.info("state = " + state);
        //判断传回参数是否正确
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        /**
         * 请求路径为ip地址时无法获取session
         */
        Object sessionState = session.getAttribute(ServiceConstant.WX_OPEN_STATE);
        if (!state.equals(sessionState)) {
            log.error("验证失败，非法的回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        /*try {
            Object sessionState = session.getAttribute(ServiceConstant.WX_OPEN_STATE);
            if (!state.equals(sessionState)) {
                log.error("验证失败，非法的回调请求");
                //throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //根据code和state参数获取access_token
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        accessTokenUrl = String.format(accessTokenUrl, ucenterProperties.getAppId(), ucenterProperties.getAppSecret(), code);

        String content = wxService.getAccessTokenContent(accessTokenUrl);

        Gson gson = new Gson();
        Map map = gson.fromJson(content, Map.class);
        /**
         * 失败返回的参数
         * {"errcode":40029,"errmsg":"invalid code"}
         */
        Object errorObj = map.get("errcode");
        if (errorObj != null) {
            String errorMsg = (String) map.get("errmsg");
            Double errorCode = (Double) errorObj;
            log.error("获取access_token失败 - " + "message: " + errorMsg + ", errcode: " + errorCode);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        //System.out.println("map = " + map);
        /**
         * 成功返回的参数
         * {
         * access_token=40_H5KQME3MG864h8HppzFhpEswaTs0mHuW8o-o_54aIiwAfrisoR1-FduvW2oVB87fhmJ0drsvUszjZJ9Mlaprt0aEGPTEM3V37cPnb-MtcZk,
         * expires_in=7200.0,
         * refresh_token=40_SThzKBVw6qHE4OwB_520_-nO_aeQzHhzmsPOtst3ThZgwi7b-cnKSe3XKLidZlY78UxhpMYi29cX5-Yx-p7a9ld4rbUKiYO-_tMrLmDt-is,
         * openid=o3_SC5wWCKLCVxi4-5YKeRlKECm4,
         * scope=snsapi_login,
         * unionid=oWgGz1DFcU0IOffdo405gj6nMdF0
         * }
         */
        String accessToken = map.get("access_token").toString();
        String openId = (String) map.get("openid");
        //根据openid查询当前用户是否已经使用微信登录过该系统
        //根据access_token获取微信用户的基本信息
        //http请求方式: GET
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=$s";
        userInfoUrl = String.format(userInfoUrl, accessToken, openId);
        //获取jwtHelper 封装的 token
        String token = wxService.getMemberByOpenId(openId, accessToken, userInfoUrl);
        //携带token跳转
        return "redirect:http://localhost:3000?token=" + token;
    }

}
