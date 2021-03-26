package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.vo.UcenterMember;
import com.atguigu.guli.service.edu.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {
    @PostMapping("/api/ucenter/member/getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable String id);
}
