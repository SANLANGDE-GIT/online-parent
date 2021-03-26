package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
public interface SubjectService extends IService<Subject> {
    void batchImport(MultipartFile file);

    Subject getByTitle(String title);

    Subject getSubByTitle(String title, String parentId);

    List<SubjectVo> nestedList();

    List<Subject> selectNestedList(String parentId);

    List<Map<String, Object>> nestedList2();
}
