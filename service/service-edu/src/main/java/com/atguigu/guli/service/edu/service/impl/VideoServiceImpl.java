package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.VodVideoService;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    VodVideoService vodVideoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeVideoById(String id) {

        Video video = baseMapper.selectById(id);
        vodVideoService.batchDeleteVideo(video.getVideoSourceId());
        return this.removeById(id);
    }
}
