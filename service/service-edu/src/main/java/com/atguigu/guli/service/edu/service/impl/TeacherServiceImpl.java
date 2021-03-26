package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.QueryTeacher;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private OssFileService ossFileService;
    @Autowired
    CourseService courseService;

    /**
     * 根据条件查询分页
     * @param pageNum 页码
     * @param pageSize 每页显示条数
     * @param queryTeacher 讲师查询对象
     * @return 分页数据
     */
    @Override
    public Page<Teacher> queryList(Integer pageNum, Integer pageSize, QueryTeacher queryTeacher) {
        Page<Teacher> page = new Page<>(pageNum,pageSize);
        if(queryTeacher==null){
            return baseMapper.selectPage(page,null);
        }
        String name = queryTeacher.getName();
        Integer level = queryTeacher.getLevel();
        String joinDateBegin = queryTeacher.getJoinDateBegin();
        String joinDateEnd = queryTeacher.getJoinDateEnd();
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            wrapper.likeRight("name",name);
        }
        if(level!=null){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(joinDateBegin)){
            wrapper.ge("join_date",joinDateBegin);
        }
        if(!StringUtils.isEmpty(joinDateEnd)){
            wrapper.le("join_date",joinDateEnd);
        }
        return baseMapper.selectPage(page,wrapper);
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @Override
    public boolean removeAvatarById(String id) {
        Teacher teacher = baseMapper.selectById(id);
        if(teacher!=null){
            String avatar = teacher.getAvatar();
            if(!StringUtils.isEmpty(avatar)){
                //删除图片
                R r = ossFileService.removeFile(avatar);
                //路径置空
                teacher.setAvatar("");
                baseMapper.updateById(teacher);
                return r.getSuccess();
            }
        }
        return false;
    }

    /**
     * 删除OSS对象，将讲师头像置空
     * @param ids
     */
    @Override
    public void batchRemoveFile(List<String> ids) {
        List<Teacher> teachers = baseMapper.selectBatchIds(ids);
        List<String> keys=new ArrayList<>();
        for (Teacher teacher : teachers) {
            keys.add(teacher.getAvatar());
        }
        //批量移除OSS对象
        ossFileService.batchRemoveFile(keys);
        //讲师头像置空
        baseMapper.blankingAvatar(ids);
    }

    /**
     * 返回讲师名称集合
     * @param name
     * @return
     */
    @Override
    public List<Map<String, Object>> queryTeacherByName(String name) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.select("name");
        teacherQueryWrapper.likeRight("name",name);
        return baseMapper.selectMaps(teacherQueryWrapper);
    }

    @Override
    public List<Teacher> teachersList4Names() {

        QueryWrapper<Teacher> wrapper =new QueryWrapper<>();
        wrapper.select("id","name");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getTeacherById(String id) {
        Map<String, Object> map=new HashMap<>();
        map.put("teacher",baseMapper.selectById(id));

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.select("id","title","cover");
        wrapper.eq("teacher_id",id);
        wrapper.eq("status",Course.COURSE_NORMAL);
        List<Course> courses = courseService.list(wrapper);
        map.put("courseList",courses);
        return map;
    }
}
