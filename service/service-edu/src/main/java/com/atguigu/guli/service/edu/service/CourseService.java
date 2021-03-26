package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfo(String id);

    void updateCourseInfo(CourseInfoForm courseInfoForm);

    List<CourseVo> getCourseList();

    Page<CourseVo> queryCoursePageList(Integer limit, Integer size, CourseQuery courseQuery);

    void updateCourseByCourseId(String courseId, CourseInfoForm courseInfoForm);

    Boolean deleteCourseById(String id);

    CoursePublishVo getCoursePublishVo(String courseId);

    void publishCourse(String courseId);

    List<Course> queryCourseList(WebCourseQuery webCourseQuery);

    WebCourseVo getCourseWebInfo(String courseId);

    CourseDto getCourseDtoById(String courseId);

    void updateBuyCountById(String courseId);
}
