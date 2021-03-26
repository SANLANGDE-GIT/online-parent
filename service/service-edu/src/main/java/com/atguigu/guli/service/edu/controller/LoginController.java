package com.atguigu.guli.service.edu.controller;

import com.atguigu.guli.service.base.result.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
//@CrossOrigin  //允许跨域访问
@RequestMapping("/user")
public class LoginController {

    @ResponseBody
    @GetMapping("get")
    public R get(HttpSession session) {
        System.out.println("session = " + session.getClass().getName());
        Object skey = session.getAttribute("skey");
        System.out.println("skey = " + skey);
        return R.ok();
    }

    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

    }

    @PostMapping("/logout")
    public R logout(){
        return R.ok();
    }

}
