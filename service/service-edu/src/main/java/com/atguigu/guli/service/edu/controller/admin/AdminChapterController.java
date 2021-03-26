package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
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
@Api(tags = "章节管理")
//@CrossOrigin  //跨域访问
@RestController  //
@RequestMapping("/admin/edu/chapter")
public class AdminChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation("新建章节")
    @PostMapping("/save")
    public R saveChapter(@ApiParam("章节信息") @RequestBody Chapter chapter){
        Boolean result = chapterService.save(chapter);
        if(result)
            return R.ok().message("保存成功！");
        else
            return R.error().message("保存失败！");
    }
    @ApiOperation("修改章节")
    @PutMapping("/update")
    public R updateChapter(@ApiParam("章节信息") @RequestBody Chapter chapter){
        boolean result = chapterService.updateById(chapter);
        if(result)
            return R.ok().message("修改成功！");
        else
            return R.error().message("修改失败！");
    }

    @ApiOperation("根据ID查询章节信息")
    @GetMapping("/get/{id}")
    public R getChapterInfoById(@ApiParam(value = "章节ID" ,required = true)@PathVariable String id){
        Chapter info = chapterService.getById(id);
        if(info != null)
            return R.ok().data("item",info);
        else
            return R.error().message("数据不存在，请刷新后重试！");
    }
    @ApiOperation("删除章节")
    @DeleteMapping("/delete/{id}")
    public R deleteChapter(@ApiParam(value = "章节ID" ,required = true)@PathVariable String id){
        Boolean result = chapterService.removeChapterById(id);
        if(result)
            return R.ok().message("删除成功！");
        else
            return R.error().message("删除失败！");
    }

    //方法一：
    @ApiOperation("根据课程ID查询章节、课时列表")
    @GetMapping("/chapter-nested-list/{courseId}")
    public R getChapterNestedList(@ApiParam(value = "课程ID" ,required = true)@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterNestedList(courseId);
        return R.ok().data("items",list);
    }
    //方法二：
    @ApiOperation("根据课程ID查询章节、课时列表")
    @GetMapping("/select/{courseId}")
    public R chapterNestedList(@ApiParam(value = "课程ID" ,required = true)@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterListByCourseId(courseId);
        return R.ok().data("items",list);
    }

}

