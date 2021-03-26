package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Api(tags = "课程收藏管理")
@RestController
@RequestMapping("api/edu/course-collect")
public class ApiCourseCollectController {

    @Autowired
    CourseCollectService courseCollectService;

    @GetMapping("/auth/remove/{courseId}")
    public R remove(@PathVariable String courseId){

        boolean b = courseCollectService.removeCollectByCourseId(courseId);
        if(b)
            return R.ok();
        else
            return R.error();
    }

    //收藏列表
    @GetMapping("/auth/list")
    public R collectList(HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        List<CourseCollectVo> list = courseCollectService.getCollectList(memberId);
        return R.ok().data("items",list);
    }
    //收藏
    @GetMapping("auth/collect-course/{courseId}")
    public R collectCourse(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        courseCollectService.saveCollectCourse(courseId,memberId);
        return R.ok();
    }

    //判断是否收藏
    @GetMapping("auth/is-collect-course/{courseId}")
    public R isCollectCourse(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Boolean isCollect = courseCollectService.isCollect(courseId, memberId);
        return R.ok().data("isCollect",isCollect);
    }
}

