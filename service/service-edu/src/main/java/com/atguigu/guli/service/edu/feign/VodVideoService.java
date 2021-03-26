package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.VodVideoServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-vod",fallback = VodVideoServiceFallBack.class)
public interface VodVideoService {
    @DeleteMapping("/admin/aliyun/vod/batch-delete/{videoIds}")
    R batchDeleteVideo( @PathVariable String videoIds);

}
