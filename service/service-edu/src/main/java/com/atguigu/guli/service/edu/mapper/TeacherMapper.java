package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Repository
public interface TeacherMapper extends BaseMapper<Teacher> {
    void blankingAvatar(List<String> ids);
}
