package com.atguigu.guli.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String upload(MultipartFile file, String module);
    void removeFile(String url);

    void batchRemoveFile(List<String> keys);
}
