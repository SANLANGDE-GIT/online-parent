package com.atguigu.guli.service.edu.controller.admin;


import com.alibaba.excel.util.StringUtils;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Slf4j
@Api(tags = "课程管理")
//@CrossOrigin  //跨域访问
@RestController
@RequestMapping("/admin/edu/course-collect")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("发布课程")
    @PutMapping("/publish-course/{courseId}")
    public R publishCourse(@PathVariable String courseId){

        courseService.publishCourse(courseId);
        return R.ok().message("课程发布成功！");
    }

    @ApiOperation("发布课程表单")
    @GetMapping("/course-publish/{courseId}")
    public R getPublicVo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(courseId);
        if(coursePublishVo!=null){
            return R.ok().data("item",coursePublishVo);
        }else
            return R.error().message("数据加载失败，请刷新后重试！");
    }

    @ApiOperation("新增课程")
    @PostMapping("/save-course-info")
    public R saveCourseInfo(@ApiParam(value = "课程基本信息",required = true)
                                @RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        if(!StringUtils.isEmpty(courseId))
            return R.ok().data("courseId",courseId).message("保存课程成功！");
        else
            return R.error().message("课程新增失败");
    }

    @ApiOperation("课程修改")
    @PutMapping("/update-course/{id}")
    public R updateCourseInfo(@ApiParam(value = "课程ID",required = true) @PathVariable("id") String courseId,
                              @ApiParam(value = "课程基本信息",required = true) @RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseByCourseId(courseId,courseInfoForm);
            return R.ok().message("修改课程成功！");
    }
/*
    @ApiOperation("课程修改")
    @PostMapping(value = "/update-course-info")
    public R updateCourseInfo(@ApiParam(value = "课程基本信息",required = true)
                                  @RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseInfo(courseInfoForm);
            return R.ok().message("修改课程成功！");
    }
*/

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("/course-info/{id}")
    public R getCourseInfo(@ApiParam(value = "课程ID",required = true) @PathVariable String id){

        CourseInfoForm courseInfoForm = courseService.getCourseInfo(id);
        return R.ok().data("item",courseInfoForm);
    }

    @ApiOperation(value = "条件查询课程分页列表")
    @GetMapping("/query-course-page-list/{limit}/{size}")
    public R queryCoursePageList(@ApiParam("页码") @PathVariable Integer limit,
                                 @ApiParam("每页显示条数")@PathVariable Integer size,
                                 @ApiParam("查询对象") CourseQuery courseQuery){
                                 //@RequestBody CourseQuery courseQuery){  查询对象以参数传递
        Page<CourseVo> pages=courseService.queryCoursePageList(limit,size,courseQuery);
        return R.ok().data("pages",pages);
    }

    @ApiOperation(value = "查询课程列表")
    @GetMapping("/select-course-list")
    public R selectCourseList(){

        List<CourseVo> list = courseService.getCourseList();
        return R.ok().data("items",list);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/delete-course/{id}")
    public R deleteCourseById(@PathVariable String id){

        Boolean b = courseService.deleteCourseById(id);
        if (b)
            return R.ok().message("删除成功");
        else
            return R.error().message("删除失败");
    }

}

