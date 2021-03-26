package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
//@CrossOrigin //允许跨域访问
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    TeacherService teacherService;

    @ApiOperation("根据ID获取讲师信息和讲师课程信息")
    @GetMapping("/teacher/{id}")
    public R getTeacherInfo(@PathVariable String id){
        Map<String,Object> map = teacherService.getTeacherById(id);
        return R.ok().data(map);
    }

    @ApiOperation("查询讲师列表")
    @GetMapping("/list")
    public R teachersList(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list).message("查询讲师成功");
    }
}

