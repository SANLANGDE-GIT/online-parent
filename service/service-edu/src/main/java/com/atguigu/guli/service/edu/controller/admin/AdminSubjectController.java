package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Api(tags = "课程列表管理")
//@CrossOrigin
@RestController
@RequestMapping("/admin/edu/subject")
public class AdminSubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("嵌套数据列表")
    @GetMapping("/nested-list")
    public R nestedList(){
        List<SubjectVo> list = subjectService.nestedList();
        //System.out.println("list = " + list);
        return R.ok().data("items",list);
    }
    @ApiOperation("嵌套数据列表2")
    @GetMapping("/select-nested-list")
    public R selectNestedList(){
        List<Map<String,Object>> list = subjectService.nestedList2();
        //System.out.println("list = " + list);
        return R.ok().data("items",list);
    }

    @ApiOperation("导入课程分类")
    @PostMapping("/import")
    public R readExcelSubject(@ApiParam(value = "Excel文件",required = true) @RequestParam("file") MultipartFile file){
        subjectService.batchImport(file);
        return R.ok().message("批量导入成功！");
    }

}

