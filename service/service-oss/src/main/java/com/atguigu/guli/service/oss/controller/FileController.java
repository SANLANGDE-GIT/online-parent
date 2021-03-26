package com.atguigu.guli.service.oss.controller;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "阿里OSS文件管理")
@RestController
//@CrossOrigin
@RequestMapping("/admin/oss/file")
@Slf4j
@RefreshScope
public class FileController {
    @Autowired
    private FileService fileService;
    @Value("${server.port}")
    private String port;
    //文件上传
    @ApiOperation("头像上传")
    @PostMapping("/upload")
    public R upload(@ApiParam("图片文件") MultipartFile file, @ApiParam("模块") @RequestParam("module") String module){
        String path = fileService.upload(file,module);
        return R.ok().data("path",path).message("文件上传成功");
    }

    @ApiOperation("删除头像")
    @GetMapping("/remove")
    public R removeFile(@ApiParam("文件路径")@RequestParam String url){
        fileService.removeFile(url);
        return R.ok();
    }

    @ApiOperation("批量删除头像")
    @PostMapping("/batchRemove")
    public R batchRemoveFile(@ApiParam("文件列表") @RequestBody List<String> keys){
        fileService.batchRemoveFile(keys);
        return R.ok();
    }

    @GetMapping("/test")
    public R test(){
        log.info("oss 被调用了");
        System.out.println(port+"被调用了");
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return R.ok();
    }
}
