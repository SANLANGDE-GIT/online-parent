package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {

    List<CourseCollectVo> getCollectList(String memberId);
}
