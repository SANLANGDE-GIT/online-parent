package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {
    @GetMapping("/admin/oss/file/test")
    R test();

    @GetMapping("/admin/oss/file/remove")
    R removeFile(@RequestParam("url") String url);

    @PostMapping(value = "/admin/oss/file/batchRemove")
    public R batchRemoveFile(@ApiParam("文件列表") @RequestBody List<String> keys);
}
