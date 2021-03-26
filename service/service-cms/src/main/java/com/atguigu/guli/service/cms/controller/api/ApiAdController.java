package com.atguigu.guli.service.cms.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
//@CrossOrigin
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    AdService adService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/set")
    public R setKv(){
//        redisTemplate.opsForValue();
//        redisTemplate.opsForSet();  无序set,底层是value
//        redisTemplate.opsForHash();
//        redisTemplate.opsForList(); 双向链表
//        redisTemplate.opsForZSet(); 根据分数有序的set

//        redisTemplate.opsForValue().set("k1","value1");
//        stringRedisTemplate.opsForValue().set("k2","value2",30, TimeUnit.SECONDS);

        redisTemplate.opsForValue().set("ad1",new Ad());
        //将对象转为字符串
        Gson gson = new Gson();

        stringRedisTemplate.opsForValue().set("ad2",gson.toJson(new Ad()));


        return  R.ok();
    }

    @GetMapping("/get")
    public R getKv(){
//        Object k1 = redisTemplate.opsForValue().get("k1");
//        System.out.println("k1 = " + k1);
//        String k2 = stringRedisTemplate.opsForValue().get("k2");
//        System.out.println("k2 = " + k2);

        //获取对象
        Object ad1 = redisTemplate.opsForValue().get("ad1"); //对象需要实现序列化
        System.out.println("ad1 = " + ad1);

        String ad2 = stringRedisTemplate.opsForValue().get("ad2");
        //
        Gson gson = new Gson();
        Ad ad = gson.fromJson(ad2, Ad.class);
        System.out.println("ad2 = " + ad);

        return  R.ok();
    }


    @ApiOperation("广告推荐位列表")
    @GetMapping("/list/{typeId}")
    public R listByTypeId(@PathVariable String typeId){

        List<Ad> ads = adService.selectListByTypeId(typeId);
        return R.ok().data("items",ads);
    }



}

