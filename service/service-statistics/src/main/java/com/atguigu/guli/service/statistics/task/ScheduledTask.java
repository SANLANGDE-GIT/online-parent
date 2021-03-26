package com.atguigu.guli.service.statistics.task;

import com.atguigu.guli.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTask {

    //@Scheduled(cron = "0/5 * * * * *")
    public void task1(){
        String preDay = new DateTime().minusDays(1).toString("yyyy-MM-dd HH:mm:ss");
        log.info("前一天：{}",preDay);
        String nextDay = new DateTime().plusDays(1).toString("yyyy-MM-dd HH:mm:ss");
        log.info("明天：{}",nextDay);
    }

    @Autowired
    DailyService dailyService;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void taskGenarateStatisticsData(){
        String preDay = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(preDay);
        log.info("taskGenarateStatisticsData,统计完毕");
    }

}
