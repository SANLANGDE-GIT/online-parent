package com.atguigu.guli.service.vod.utils;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.InputStream;
import java.util.List;

@Slf4j
public class VodUtil {

    /**
     * 文件流上传 不加密
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     * @param is
     */
    public static String uploadStream(String accessKeyId, String accessKeySecret, String title, String fileName, InputStream is) {
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId,accessKeySecret,title,fileName,is);

        UploadVideoImpl uploadVideo=new UploadVideoImpl();

        UploadStreamResponse response = uploadVideo.uploadStream(request);

        if(response.isSuccess()) {
            log.info("videoId = {}",response.getVideoId());
            return response.getVideoId();
        }
        return null;
    }

    /**
     * 文件流上传 工作流
     * @param request 自定义上传属性
     */
    public static String uploadStreamSecret(UploadStreamRequest request){

        //转码模板组ID
        //request.setTemplateGroupId("");
        //工作流ID
        //request.setWorkflowId("");
        //设置封面
        //request.setCoverURL("");

        UploadVideoImpl uploadVideo=new UploadVideoImpl();

        UploadStreamResponse response = uploadVideo.uploadStream(request);

        if(response.isSuccess()) {
            log.info("videoId = {}",response.getVideoId());
            return response.getVideoId();
        }
        return null;
    }

    /**
     * 初始化客户端
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 获取视频播放地址
     * @param accessKeyId
     * @param accessKeySecret
     * @param request 自定义上传属性
     */
    public static String getUrl(String accessKeyId,String accessKeySecret,GetPlayInfoRequest request){
        //初始化容器
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        //获取响应对象
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            return playInfoList.get(0).getPlayURL();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }
    /**
     * 获取视频播放凭证
     * @param accessKeyId
     * @param accessKeySecret
     * @param request 自定义上传属性
     */
    public static String getPlayAuthor(String accessKeyId,String accessKeySecret,GetVideoPlayAuthRequest request){

        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {

            response = client.getAcsResponse(request);
            //播放凭证
            return response.getPlayAuth();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.FETCH_VIDEO_UPLOADAUTH_ERROR);
        }
    }

    /**
     * 删除视频
     * @param accessKeyId AK
     * @param accessKeySecret AKS
     * @param videoId 视频ID
     */
    public static void deleteVideo(String accessKeyId,String accessKeySecret,String videoId) {
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        //删除媒体流函数
        /**
         * com.aliyuncs.exceptions.ClientException: MissingJobIds : JobIds is mandatory for this action.
         *
         *
         */

//        DeleteStreamRequest request = new DeleteStreamRequest();
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        DeleteVideoResponse response = new DeleteVideoResponse();
        try {
            response = client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    /**
     * 批量删除视频
     * @param accessKeyId
     * @param accessKeySecret
     * @param videoIds 支持传入多个视频ID，多个用逗号分隔
     */
    public static void batchDeleteVideo(String accessKeyId,String accessKeySecret,String videoIds) {
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        //批量删除源文件函数
        //DeleteMezzaninesRequest request = new DeleteMezzaninesRequest();
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoIds);

        //DeleteMezzaninesResponse response = new DeleteMezzaninesResponse();
        DeleteVideoResponse response = new DeleteVideoResponse();
        try {
            response = client.getAcsResponse(request);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

}
