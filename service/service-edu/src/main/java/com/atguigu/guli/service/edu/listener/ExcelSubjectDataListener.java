package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private static final int BATCH_COUNT=10;


    private SubjectMapper subjectMapper;

    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext analysisContext) {
        log.info("解析到一条记录：{}",data);
        //处理读取出来的数据
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();
        log.info("levelOneTitle:{}",levelOneTitle);
        log.info("levelTwoTitle:{}",levelTwoTitle);
        //
        Subject byTitle = this.getByTitle(levelOneTitle);
        String parentId=null;
        if (byTitle==null){
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);//一级分类名称
            subjectMapper.insert(subject);
            parentId=subject.getId();
        }else {
            parentId=byTitle.getId();
        }
        Subject subByTitle = this.getSubByTitle(levelTwoTitle, parentId);
        if(subByTitle==null){
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }

    /**
     * 根据分类名称查询这个一级分类是否存在
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");//一级分类
        return subjectMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类名称和父id查询这个二级分类是否存在
     * @param title
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        return subjectMapper.selectOne(queryWrapper);
    }
}
