package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Api(tags = "课程")
//@CrossOrigin
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    CourseService courseService;

    @ApiOperation("根据课程id更改销售量")
    @GetMapping("update-buy-count/{courseId}")
    public R updateBuyCountById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String courseId){
        courseService.updateBuyCountById(courseId);
        return R.ok();
    }

    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/get-course-dto/{courseId}")
    public R getCourseDtoById(@PathVariable String courseId){
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return R.ok().data("courseDto",courseDto);
    }

    @ApiOperation("课程详细信息")
    @GetMapping("/course/{courseId}")
    public R courseWebInfo(@PathVariable String courseId){
        WebCourseVo webInfo = courseService.getCourseWebInfo(courseId);
        return R.ok().data("item",webInfo);
    }

    @ApiOperation("课程列表")
    @GetMapping("/list")
    public R list(@ApiParam("查询对象") WebCourseQuery webCourseQuery){

        List<Course> courses=courseService.queryCourseList(webCourseQuery);
        return R.ok().data("items",courses);
    }

}

