package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.QueryTeacher;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
@RestController
@RequestMapping("/admin/edu/teacher")
@Api(tags = "讲师管理")
@Slf4j
//@CrossOrigin  //允许当前模块被跨域访问，利用spring的拦截器实现往response里添加 Access-Control-Allow-Origin等响应头信息允许跨域访问
public class AdminTeacherController {

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    TeacherService teacherService;


    @RequestMapping("/message1")
    public String message(){
        return "Hello World";
    }

    @ApiOperation("测试API调用")
    @GetMapping("/test")
    public R test(){
        ossFileService.test();
        return R.ok();
    }

    @ApiOperation("根据左关键字查询讲师名列表")
    @GetMapping("/query/name/{name}")
    public R queryNames(@PathVariable String name){
        List<Map<String,Object>> nameList = teacherService.queryTeacherByName(name);
        return R.ok().data("nameList",nameList);
    }

    @ApiOperation("查询一个讲师")
    @GetMapping("/query/{id}")
    public R queryOne(@ApiParam(value = "讲师ID",required = true)@PathVariable String id){
        Teacher teacher = teacherService.getById(id);
        if(teacher!=null)
            return R.ok().data("item",teacher);
        else
            return R.ok().message("你要查询的讲师不存在");
    }
    @ApiOperation("修改讲师")
    @PostMapping("/update")
    public R updateTeacher(@RequestBody Teacher teacher){
        boolean b = teacherService.updateById(teacher);
        if(b)
            return R.ok();
        else
            return R.error().message("修改失败");
    }
    @ApiOperation(value = "添加讲师")
    @PostMapping("/add")
    //@RequestBody 响应报文的请求体必须是json格式
    public R addTeacher(@RequestBody Teacher teacher){
        teacher.setJoinDate(new Date());
        boolean b = teacherService.save(teacher);
        if(b){
            return R.ok().message("添加成功");
        }else {
            return R.error().message("讲师添加失败");
        }
    }

    @ApiOperation(value = "根据条件查询讲师分页数据")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public R queryList(@ApiParam(value = "页码")@PathVariable Integer pageNum,
                       @ApiParam(value = "每页显示条数")@PathVariable Integer pageSize, QueryTeacher queryTeacher){
        Page<Teacher> page = teacherService.queryList(pageNum,pageSize,queryTeacher);
        return R.ok().data("pages",page);
    }

    /*@ApiOperation("查询讲师分页数据")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public R pageList(@ApiParam(value = "页码",required = true)@PathVariable Integer pageNum,
                      @ApiParam(value = "每页显示条数",required = true)@PathVariable Integer pageSize){
        Page<Teacher> page = new Page<>(pageNum,pageSize);
        page = teacherService.page(page);
        return R.ok().data("page",page);
    }*/

    @ApiOperation("查询讲师列表")
    @GetMapping("/list")
    public R teachersList(){
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list).message("查询讲师成功");
    }
    @ApiOperation("查询讲师选择列表")
    @GetMapping("/list4names")  //请求地址不能存在相同
    public R teachersList4Names(){
        List<Teacher> list = teacherService.teachersList4Names();
        return R.ok().data("items",list).message("查询讲师成功");
    }

    @ApiOperation("根据ID删除讲师")
    @DeleteMapping("/delete/{id}")
    public R deleteTeacherById(@ApiParam(value = "讲师ID",required = true) @PathVariable String id){
        //删除图片
        teacherService.removeAvatarById(id);
        //删除讲师
        boolean b = teacherService.removeById(id);
        if(b)
            return R.ok();
        else
            return R.error().message("您要删除的数据不存在！");
    }
    @ApiOperation("根据ID批量删除讲师")
    @DeleteMapping("/batch/delete")
    public R deleteTeacherById(@ApiParam(value = "讲师ID",required = true) @RequestBody List<String> ids){
        //批量删除头像
        teacherService.batchRemoveFile(ids);
        //批量删除讲师
        boolean b = teacherService.removeByIds(ids);
        if(b)
            return R.ok();
        else
            return R.error().message("您要删除的数据不存在！");
    }
}

