package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.listener.ExcelSubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional(rollbackFor = Exception.class)
    /**
     * 导入课程分类列表
     */
    @Override
    public void batchImport(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectDataListener(baseMapper)).excelType(ExcelTypeEnum.XLS).sheet().doRead();
        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    /**
     * 查询一级课程名称是否存在
     * @param title
     * @return
     */
    @Override
    public Subject getByTitle(String title) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");//一级分类
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 查询子类课程名称是否存在
     * @param title 课程名称
     * @param parentId 父id
     * @return
     */
    @Override
    public Subject getSubByTitle(String title, String parentId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 课程分类列表
     * @return
     */
    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.nestedList();
    }

    /**
     * 课程分类列表2
     * @return
     */
    public List<Map<String,Object>> nestedList2(){
        List<Map<String,Object>> list=new ArrayList<>();

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","title");
        queryWrapper.eq("parent_id",0);
        List<Subject> selectList = baseMapper.selectList(queryWrapper);
        for (Subject subject : selectList) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",subject.getId());
            map.put("title",subject.getTitle());
            List<Subject> subjects = selectNestedList(subject.getId());
            if (subjects!=null && subjects.size()>0){
                map.put("children",subjects);
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Subject> selectNestedList(String parentId) {
        return baseMapper.selectNestedList(parentId);
    }
}
