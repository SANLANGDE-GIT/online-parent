package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.QueryTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface TeacherService extends IService<Teacher> {

    Page<Teacher> queryList(Integer pageNum, Integer pageSize, QueryTeacher queryTeacher);

    boolean removeAvatarById(String id);

    void batchRemoveFile(List<String> ids);

    List<Map<String, Object>> queryTeacherByName(String name);

    List<Teacher> teachersList4Names();

    Map<String, Object> getTeacherById(String id);
}
