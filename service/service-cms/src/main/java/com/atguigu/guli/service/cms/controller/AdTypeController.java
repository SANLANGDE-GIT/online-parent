package com.atguigu.guli.service.cms.controller;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
@RestController
@RequestMapping("/cms/ad-type")
public class AdTypeController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/set")
    public R setKv(){


        redisTemplate.opsForValue().set("k1","v1",20, TimeUnit.SECONDS);

        Gson gson = new Gson();

        String adStr = new Ad().toString();

        stringRedisTemplate.opsForSet().add("ad1",adStr);
        return R.ok();
    }

    @GetMapping("/get")
    public R getKv(){

        Object k1 = redisTemplate.opsForValue().get("k1");
        System.out.println("k1 = " + k1);

//        String ad1 = stringRedisTemplate.opsForSet().;
//        System.out.println("ad1 = " + ad1);

        return R.ok();
    }


}

