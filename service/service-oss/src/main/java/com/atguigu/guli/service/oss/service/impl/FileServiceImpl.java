package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.oss.config.OssProperties;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.utils.StringHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    OssProperties ossProperties;

    String bucketName;
    String scheme;
    String endpoint;
    String accessKeyId;
    String accessKeySecret;

    @PostConstruct  //jdk 通过的初始化方法，当前对象的构造器调用后立即执行
    public void init(){
        accessKeyId = ossProperties.getAccessKeyId();
        accessKeySecret = ossProperties.getAccessKeySecret();
        bucketName = ossProperties.getBucketName();
        scheme = ossProperties.getScheme();
        endpoint = ossProperties.getEndpoint();
    }

    @Override
    public String upload(MultipartFile file, String module) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint, accessKeyId, accessKeySecret);

        if (!ossClient.doesBucketExist(bucketName)){
            //创建bucket
            ossClient.createBucket(bucketName);
            //设置oss实例访问的权限为公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }

        // 上传文件流。
        InputStream inputStream = null;
        String path="";
        try {
            inputStream = file.getInputStream();
            //生成文件目录+名称
            String fileName = StringHandle.ossFilePath(file.getOriginalFilename(),module);
            ossClient.putObject(bucketName, fileName, inputStream);
            path=scheme+bucketName+"."+endpoint+"/"+fileName;
        }  catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
             throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return path;
    }

    @Override
    public void removeFile(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(scheme + endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        String path=bucketName+"."+endpoint+"/";

        int l =url.indexOf(path)+path.length();
        System.out.println(url.substring(l));

        //
        ossClient.deleteObject(bucketName, url.substring(l));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void batchRemoveFile(List<String> keys) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        List<String> paths = new ArrayList<String>();
        String dir=bucketName+"."+endpoint+"/";

        for (String key : keys) {
            int l= key.indexOf(dir)+dir.length();
            paths.add(key.substring(l));
        }
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(paths));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
