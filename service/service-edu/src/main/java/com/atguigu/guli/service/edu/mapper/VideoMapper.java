package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.VideoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Repository
public interface VideoMapper extends BaseMapper<Video> {

    List<VideoVo> getVideoVoList(@Param("id") String id);
    void deleteVideoByChapterId(String chapterId);
}
