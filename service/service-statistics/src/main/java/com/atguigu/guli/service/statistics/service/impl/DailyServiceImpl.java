package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.feign.UcenterMemberService;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2021-01-07
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    UcenterMemberService ucenterMemberService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {
        //判断今天是否统计过,存在就删除
        QueryWrapper<Daily> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        baseMapper.delete(queryWrapper);

        R r = ucenterMemberService.countRegisterNum(day);
        Integer  registerNum = (Integer) r.getData().get("registerNum");
        int loginNum = RandomUtils.nextInt(100, 200);
        int videoViewNum = RandomUtils.nextInt(100, 200);
        int courseNum = RandomUtils.nextInt(100, 200);

        Daily daily=new Daily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        baseMapper.insert(daily);

    }

    @Override
    public Map<String, Map<String, Object>> getChartData(String begin, String end) {
        Map<String, Map<String, Object>> map =new HashMap<>();
        Map<String, Object> registerNum = this.getChartDataByType(begin,end,"register_num");
        Map<String, Object> loginNum = this.getChartDataByType(begin,end,"login_num");
        Map<String, Object> videoViewNum = this.getChartDataByType(begin,end,"video_view_num");
        Map<String, Object> courseNum = this.getChartDataByType(begin,end,"course_num");

        map.put("registerNum",registerNum);
        map.put("loginNum",loginNum);
        map.put("videoViewNum",videoViewNum);
        map.put("courseNum",courseNum);

        return map;
    }

    /**
     * 封装x、y的数据
     * @param begin 开始时间
     * @param end 结束时间
     * @param type 统计类型
     * @return 返回一个map集合
     */
    private Map<String, Object> getChartDataByType(String begin, String end, String type) {

        Map<String, Object> map =new HashMap<>();

        ArrayList<String> xList = new ArrayList<>();
        ArrayList<Integer> yList = new ArrayList<>();

        QueryWrapper<Daily> queryWrapper =new QueryWrapper<>();
        queryWrapper.select(type,"date_calculated").between("date_calculated",begin,end);
        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        for (Map<String, Object> objectMap : mapList) {
            xList.add((String) objectMap.get("date_calculated"));
            yList.add((Integer) objectMap.get(type));
        }
        map.put("xData",xList);
        map.put("yData",yList);
        return map;

    }
}
