package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.vo.UcenterMember;
import com.atguigu.guli.service.edu.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public UcenterMember getInfo(String id) {
        log.info("熔断执行！");
        return null;
    }
}
