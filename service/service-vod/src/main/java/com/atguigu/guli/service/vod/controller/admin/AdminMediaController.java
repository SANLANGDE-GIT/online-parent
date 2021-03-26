package com.atguigu.guli.service.vod.controller.admin;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@CrossOrigin
@RequestMapping("/admin/aliyun/vod")
@Api(tags = "阿里视频管理")
@RefreshScope
public class AdminMediaController {

    @Autowired
    VideoService videoService;

    @ApiOperation("视频上传")
    @PostMapping("upload")
    public R uploadVideo(@ApiParam(value = "文件",required = true) MultipartFile file){
        //加密上传
        String videoId = videoService.uploadSecret(file);
        if(!StringUtils.isEmpty(videoId))
            return R.ok().message("视频上传成功！").data("videoId",videoId);
        else
            return R.setResult(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
    }

    @ApiOperation("获取视频地址")
    @GetMapping("/get-url/{videoId}")
    public R getVideoUrl(@ApiParam(value = "视频ID",required = true) @PathVariable String videoId){

        String url= videoService.getVideoUrl(videoId);
        if(!StringUtils.isEmpty(url))
            return R.ok().data("url",url);
        else
            return R.setResult(ResultCodeEnum.FETCH_VIDEO_UPLOADAUTH_ERROR);
    }

    @ApiOperation("获取视频播放凭证")
    @GetMapping("/get-play-auth/{videoId}")
    public R getVideoPlayAuth(@ApiParam(value = "视频ID",required = true) @PathVariable String videoId){

        String playAuth= videoService.getVideoPlayAuth(videoId);
        if(!StringUtils.isEmpty(playAuth))
            return R.ok().data("playAuth",playAuth);
        else
            return R.setResult(ResultCodeEnum.FETCH_VIDEO_UPLOADAUTH_ERROR);
    }

    @ApiOperation("删除视频")
    @DeleteMapping("/delete/{videoId}")
    public R deleteVideo(@ApiParam(value = "视频ID",required = true) @PathVariable String videoId){
        videoService.deleteVideoById(videoId);
        return R.ok().message("删除成功！");
    }

    @ApiOperation("批量删除视频")
    @DeleteMapping("/batch-delete/{videoIds}")
    public R batchDeleteVideo(@ApiParam(value = "视频ID,多个id使用逗号隔开",required = true) @PathVariable String videoIds){
        videoService.batchDeleteVideoById(videoIds);
        return R.ok().message("删除成功！");
    }

}
