package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.EduCourseServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(value = "service-edu",fallback = EduCourseServiceFallBack.class)
public interface EduCourseService {

    @GetMapping("/api/edu/course/update-buy-count/{courseId}")
    R updateBuyCountById(@PathVariable String courseId);

    @GetMapping("/api/edu/course/get-course-dto/{courseId}")
    R getCourseDtoById(@PathVariable String courseId);

}
