package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
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
public interface ChapterService extends IService<Chapter> {

    //Boolean saveChapter(Chapter chapter);

    List<ChapterVo> getChapterListByCourseId(String courseId);

    List<ChapterVo> getChapterNestedList(String courseId);

    //void updateChapter(Chapter chapter);

    Boolean removeChapterById(String id);

    void removeChapterByCourseId(String courseId);
}
