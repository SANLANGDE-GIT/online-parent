package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface CourseCollectService extends IService<CourseCollect> {

    void saveCollectCourse(String courseId, String memberId);

    Boolean isCollect(String courseId, String memberId);

    List<CourseCollectVo> getCollectList(String memberId);

    boolean removeCollectByCourseId(String courseId);
}
