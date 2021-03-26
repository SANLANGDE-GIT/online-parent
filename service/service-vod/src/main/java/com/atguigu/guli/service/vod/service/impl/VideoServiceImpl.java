package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.config.VodProperties;
import com.atguigu.guli.service.vod.service.VideoService;
import com.atguigu.guli.service.vod.utils.VodUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    private String accessKeyId;
    private String accessKeySecret;
    private String workFlowId;

    @PostConstruct
    public void init(){
        accessKeyId = vodProperties.getAccessKeyId();
        accessKeySecret = vodProperties.getAccessKeySecret();
        workFlowId = vodProperties.getWorkFlowId();
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String title = file.getOriginalFilename();
            return VodUtil.uploadStream(accessKeyId,accessKeySecret,title,title,inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    /**
     * 加密上传
     * @param file
     * @return
     */
    @Override
    public String uploadSecret(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String title = file.getOriginalFilename();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,accessKeySecret,title,title,inputStream);
            request.setWorkflowId(workFlowId); //
            return VodUtil.uploadStreamSecret(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @Override
    public String getVideoUrl(String videoId) {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        return VodUtil.getUrl(accessKeyId,accessKeySecret,request);
    }

    @Override
    public String getVideoPlayAuth(String videoId) {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        return VodUtil.getPlayAuthor(accessKeyId,accessKeySecret,request);
    }

    @Override
    public void deleteVideoById(String videoId) {
        VodUtil.deleteVideo(accessKeyId,accessKeySecret,videoId);
    }

    @Override
    public void batchDeleteVideoById(String videoIds) {
        VodUtil.batchDeleteVideo(accessKeyId,accessKeySecret,videoIds);
    }
}
