package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.VideoVo;
import com.atguigu.guli.service.edu.feign.VodVideoService;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    ChapterMapper chapterMapper;

    @Autowired
    VodVideoService vodVideoService;

//    @Override
//    public Boolean saveChapter(Chapter chapter) {
//        int i = baseMapper.insert(chapter);
//        if (i>0)
//            return true;
//        else
//            return false;
//    }

    /**
     * 章节、课时列表显示 ：mapper 配置关联
     *
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterNestedList(String courseId) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("cp.sort","vo.sort");
        queryWrapper.eq("cp.course_id",courseId);

        return baseMapper.getChapterListByQueryWrapper(queryWrapper);

    }
    /**
     * 章节、课时列表显示 :使用逻辑关联
     *
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterListByCourseId(String courseId) {

        /*QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        wrapper.eq("course_id",courseId);*/
        //Mapper文件配置查询无排序
        //return baseMapper.getChapterListShortly(courseId);
        //
        List<ChapterVo> chapterVoList = new ArrayList<>();

        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort");
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);
        chapterQueryWrapper.clear();  //清楚条件
        for (Chapter chapter : chapterList) {
            ChapterVo c = new ChapterVo();
            c.setId(chapter.getId());
            c.setTitle(chapter.getTitle());
            c.setSort(chapter.getSort());

            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.eq("course_id", courseId);
            videoQueryWrapper.eq("chapter_id", chapter.getId());
            videoQueryWrapper.orderByAsc("sort");
            List<Video> videoList = videoMapper.selectList(videoQueryWrapper);  //检查videoMapper 是否注入
            if (videoList.size() > 0) {
                ArrayList<VideoVo> videoVos = new ArrayList<>();
                for (Video video : videoList) {
                    VideoVo vo = new VideoVo();
                    vo.setId(video.getId());
                    vo.setFree(video.getFree());
                    vo.setSort(video.getSort());
                    vo.setTitle(video.getTitle());
                    vo.setVideoSourceId(video.getVideoSourceId());
                    vo.setVideoOriginalName(video.getVideoOriginalName());
                    videoVos.add(vo);
                }
                c.setChildren(videoVos);
            }
            chapterVoList.add(c);
            videoQueryWrapper.clear();  //清楚条件
        }

        return chapterVoList;
    }

//    @Override
//    public void updateChapter(Chapter chapter) {
//        baseMapper.updateById(chapter);
//    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeChapterById(String id) {
        //删除课时
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", id);

        List<Video> videos = videoMapper.selectList(wrapper);
        String videoIds;
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < videos.size(); i++) {
            if(i==0){
                sb.append(videos.get(i).getVideoSourceId());
            }else {
                sb.append(","+videos.get(i).getVideoSourceId());
            }
        }
        videoIds=sb.toString();
        //System.out.println("videoIds = " + videoIds);
        //删除视频
        vodVideoService.batchDeleteVideo(videoIds);
        //删除课时
        videoMapper.delete(wrapper);
        //删除章节
        return this.removeById(id);
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        chapterMapper.delete(wrapper);
    }
}
