package com.atguigu.guli.service.oss.utils;

import org.joda.time.DateTime;

import java.util.UUID;

public class StringHandle {

    /**
     * oss文件名称:module/yyyy/MM/dd/uuid.suffix
     * @param fileName 文件全路径
     * @param module 模块名
     * @return oos 对象名称
     */
    public static String ossFilePath(String fileName,String module){
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String dirs = new DateTime().toString("/yyyy/MM/dd/");
        return module+dirs+uuid+suffix;
    }

}
