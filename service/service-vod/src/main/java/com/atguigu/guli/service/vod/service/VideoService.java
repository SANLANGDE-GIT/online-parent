package com.atguigu.guli.service.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    String upload(MultipartFile file);

    String uploadSecret(MultipartFile file);

    String getVideoUrl(String videoId);

    String getVideoPlayAuth(String videoId);

    void deleteVideoById(String videoId);

    void batchDeleteVideoById(String videoIds);
}
