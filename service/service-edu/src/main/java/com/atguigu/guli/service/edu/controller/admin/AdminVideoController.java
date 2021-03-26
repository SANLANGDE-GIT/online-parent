package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.VodVideoService;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Api(tags = "课时管理")
//@CrossOrigin
@RestController
@RequestMapping("/admin/edu/video")
public class AdminVideoController {

    @Autowired
    VodVideoService vodVideoService;

    @Autowired
    VideoService videoService;

    @ApiOperation("批量删除视频")
    @DeleteMapping("/batch-delete/{videoIds}")
    public R batchDeleteVideo(@ApiParam(value = "视频ID,多个id使用逗号隔开",required = true) @PathVariable String videoIds){
        return vodVideoService.batchDeleteVideo(videoIds);
    }

    @ApiOperation("添加课时")
    @PostMapping("/save")
    public R saveVideoInfo(@ApiParam("课时基本信息") @RequestBody Video videoInfo){

        boolean save = videoService.save(videoInfo);
        if(save)
            return R.ok().message("添加课时成功！");
        else
            return R.error().message("添加课时失败！");


    }
    @ApiOperation("修改课时")
    @PostMapping("/update")
    public R updateVideoInfo(@ApiParam("课时基本信息") @RequestBody Video videoInfo){

        boolean save = videoService.updateById(videoInfo);
        if(save)
            return R.ok().message("修改课时成功！");
        else
            return R.error().message("修改课时失败！");


    }
    @ApiOperation("删除课时")
    @DeleteMapping("/delete/{id}")
    public R deleteVideoById(@ApiParam("课时ID") @PathVariable String id){
        //删除课时&//删除视频TODO
        boolean save = videoService.removeVideoById(id);

        if(save)
            return R.ok().message("删除课时成功！");
        else
            return R.error().message("删除课时失败！");


    }
    @ApiOperation("查询课时信息")
    @GetMapping("/select/{id}")
    public R getVideoById(@ApiParam("课时ID") @PathVariable String id){
        //删除课时
        Video video = videoService.getById(id);
        if(video!=null)
            return R.ok().data("item",video);
        else
            return R.error().message("课时数据不存在");


    }

}

