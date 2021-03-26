package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.VodVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VodVideoServiceFallBack implements VodVideoService {


    @Override
    public R batchDeleteVideo(String videoIds) {
        log.error("熔断保护");
        return R.error().message("服务器繁忙，请稍后重试！");
    }
}
