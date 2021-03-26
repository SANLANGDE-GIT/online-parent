package com.atguigu.guli.service.vod.controller.api;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin
@RequestMapping("/api/aliyun/vod")
@Api(tags = "阿里视频管理")
@RefreshScope
public class ApiMediaController {

    @Autowired
    VideoService videoService;

    @ApiOperation("获取视频播放凭证")
    @GetMapping("/auth/getAuth/{videoId}")
    public R getVideoPlayAuth(@ApiParam(value = "视频ID",required = true) @PathVariable String videoId){
        System.out.println("videoId = " + videoId);
        String playAuth= videoService.getVideoPlayAuth(videoId);
        if(!StringUtils.isEmpty(playAuth))
            return R.ok().data("playAuth",playAuth);
        else
            return R.error().message("视频凭证获取失败");
    }

}
