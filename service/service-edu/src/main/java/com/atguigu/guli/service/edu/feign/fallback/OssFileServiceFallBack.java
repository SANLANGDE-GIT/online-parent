package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.error("熔断保护");
        return R.error().message("服务器繁忙，请稍后重试！");
    }

    @Override
    public R batchRemoveFile(List<String> keys) {
        log.error("熔断保护");
        return R.error().message("服务器繁忙，请稍后重试！");
    }
}
