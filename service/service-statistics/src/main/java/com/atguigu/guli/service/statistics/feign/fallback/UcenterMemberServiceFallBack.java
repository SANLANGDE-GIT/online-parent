package com.atguigu.guli.service.statistics.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UcenterMemberServiceFallBack implements UcenterMemberService {

    @Override
    public R countRegisterNum(String day) {
        log.error("熔断器执行");
        return R.error();
    }
}
