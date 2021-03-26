package com.atguigu.guli.service.statistics.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @GetMapping("/admin/ucenter/member/count-register/{day}")
    public R countRegisterNum(@PathVariable String day);
}
