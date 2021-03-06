package com.atguigu.guli.service.cms.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @GetMapping("/admin/oss/file/remove")
    R removeFile(@RequestParam("url") String url);

}
