package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Repository
public interface ChapterMapper extends BaseMapper<Chapter> {

    List<ChapterVo> getChapterList(String courseId);

    List<ChapterVo> getChapterListShortly(String courseId);

    /**
     * Constants.WRAPPER：常量  值为：ew  自定义条件使用 ${ew.customSqlSegment}  来接受
     *      * 获取自定义SQL 简化自定义XML复杂情况
     *      * <p>使用方法</p>
     *      * <p>`自定义sql` + ${ew.customSqlSegment}</p>
     *      * <p>1.逻辑删除需要自己拼接条件 (之前自定义也同样)</p>
     *      * <p>2.不支持wrapper中附带实体的情况 (wrapper自带实体会更麻烦)</p>
     *      * <p>3.用法 ${ew.customSqlSegment} (不需要where标签包裹,切记!)</p>
     *      * <p>4.ew是wrapper定义别名,可自行替换</p>
     * @param queryWrapper 自定查询条件
     * @return
     */
    List<ChapterVo> getChapterListByQueryWrapper(@Param("ew") QueryWrapper queryWrapper);
}
