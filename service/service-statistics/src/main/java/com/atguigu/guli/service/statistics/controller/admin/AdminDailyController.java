package com.atguigu.guli.service.statistics.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2021-01-07
 */
@Api(tags = "统计分析管理")
@RestController
@RequestMapping("/admin/statistics/daily")
public class AdminDailyController {

    @Autowired
    DailyService dailyService;

    @ApiOperation("生成统计记录")
    @GetMapping("/show-charts/{begin}/{end}")
    public R showCharts(@PathVariable String begin,@PathVariable String end){
        Map<String,Map<String,Object>> mapMap = dailyService.getChartData(begin,end);
        return R.ok().data("chartData",mapMap);
    }

    @ApiOperation("生成统计记录")
    @PostMapping("/create/{day}")
    public R createStatisticsByDay(@PathVariable String day){
        dailyService.createStatisticsByDay(day);
        return R.ok().message("生成统计成功");
    }

}

