package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.CourseDescriptionMapper;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper descriptionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    /**
     * 新增课程信息
     * @param courseInfoForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        //
        BeanUtils.copyProperties(courseInfoForm, course);
        //保存课程基本信息
        baseMapper.insert(course);
        String courseId = course.getId();

        //保存课程相信信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(courseId);
        descriptionMapper.insert(courseDescription);

        return courseId;
    }

    /**
     * 查询课程信息
     * @param id
     * @return
     */
    @Override
    public CourseInfoForm getCourseInfo(String id) {
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        CourseDescription courseDescription = descriptionMapper.selectById(id);
        BeanUtils.copyProperties(course,courseInfoForm);
        if(courseDescription!=null)
            courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    /**
     * 修改课程信息
     * @param courseInfoForm
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        baseMapper.updateById(course);
        //保存课程相信信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        int i = descriptionMapper.updateById(courseDescription);
        if (i==0){
            descriptionMapper.insert(courseDescription);
        }
    }

    @Override
    public List<CourseVo> getCourseList() {
        return baseMapper.getCourseVoList();
    }

    @Override
    public WebCourseVo getCourseWebInfo(String courseId) {

        //验证用户是否登录
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);

        return baseMapper.getCourseWebInfo(courseId);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        return baseMapper.getCourseDtoById(courseId);
    }

    @Override
    public void updateBuyCountById(String courseId) {
        Course course = this.getById(courseId);
        course.setBuyCount(course.getBuyCount()+1);
        baseMapper.updateById(course);
    }

    @Override
    public List<Course> queryCourseList(WebCourseQuery webCourseQuery) {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.select("id","title","cover","price","view_count","buy_count");
        wrapper.eq("status",Course.COURSE_NORMAL);

        if(webCourseQuery==null){
            return baseMapper.selectList(wrapper);
        }

        if (!StringUtils.isEmpty(webCourseQuery.getSubjectId())){
            wrapper.eq("subject_id",webCourseQuery.getSubjectId());
        }
        if (!StringUtils.isEmpty(webCourseQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id",webCourseQuery.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(webCourseQuery.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(webCourseQuery.getPublishTimeSort())){
            wrapper.orderByDesc("publish_time");
        }
        if (!StringUtils.isEmpty(webCourseQuery.getPriceSort())&&2 == webCourseQuery.getType()){
            wrapper.orderByDesc("price");
        }

        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<CourseVo> queryCoursePageList(Integer limit, Integer size, CourseQuery courseQuery) {

        Page<CourseVo> pages=new Page<>(limit,size);

        QueryWrapper<CourseVo> wrapper=new QueryWrapper<>();

        if(!StringUtils.isBlank(courseQuery.getTitle()))
            wrapper.like("cc.title",courseQuery.getTitle());

        if (!StringUtils.isBlank(courseQuery.getTeacherId()))
            wrapper.eq("cc.teacher_id",courseQuery.getTeacherId());

        if (!StringUtils.isBlank(courseQuery.getSubjectId()))
            wrapper.eq("cc.subject_id",courseQuery.getSubjectId());

        if (!StringUtils.isBlank(courseQuery.getSubjectParentId()))
            wrapper.eq("cc.subject_parent_id",courseQuery.getSubjectParentId());

        List<CourseVo> list = baseMapper.queryCourseVoPageList(pages, wrapper);
        return pages.setRecords(list);
    }

    @Override
    public void updateCourseByCourseId(String courseId, CourseInfoForm courseInfoForm) {
        //保存课程基本信息 方法二
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm, course);
        course.setId(courseId);
        baseMapper.updateById(course);
        //保存课程相信信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(courseId);
        int i = descriptionMapper.updateById(courseDescription);
        if (i==0){
            descriptionMapper.insert(courseDescription);
        }
    }

    @Override
    public Boolean deleteCourseById(String id) {

        descriptionMapper.deleteById(id);

        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishVo(String courseId) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cc.id",courseId);

        return baseMapper.getPublishVo(wrapper);
    }

    @Override
    public void publishCourse(String courseId) {
        Course course = baseMapper.selectById(courseId);
        course.setPublishTime(new Date());
        course.setStatus(Course.COURSE_NORMAL);
        course.setId(courseId);
        baseMapper.updateById(course);
    }
}
