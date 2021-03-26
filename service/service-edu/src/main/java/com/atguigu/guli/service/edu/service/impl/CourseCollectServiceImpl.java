package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.mapper.CourseCollectMapper;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {

    @Override
    public void saveCollectCourse(String courseId, String memberId) {
        if(!isCollect(courseId,memberId)){
            CourseCollect collect=new CourseCollect();
            collect.setCourseId(courseId);
            collect.setMemberId(memberId);
            baseMapper.insert(collect);
        }
    }

    @Override
    public Boolean isCollect(String courseId, String memberId) {

        QueryWrapper<CourseCollect> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId)
                    .eq("member_id",memberId);
        Integer count = baseMapper.selectCount(queryWrapper);

        return count.intValue()>0;
    }

    @Override
    public List<CourseCollectVo> getCollectList(String memberId) {
        return baseMapper.getCollectList(memberId);
    }

    @Override
    public boolean removeCollectByCourseId(String courseId) {
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        return this.remove(queryWrapper);
    }


}
