package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Repository
public interface SubjectMapper extends BaseMapper<Subject> {
    List<SubjectVo> nestedList();

    List<Subject> selectNestedList(@Param("parentId") String parentId);
}
