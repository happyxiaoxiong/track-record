package cn.cnic.trackrecord.worker;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.service.TrackStatService;
import cn.cnic.trackrecord.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 轨迹数据统计任务
 */
@Component
@Slf4j
public class TrackStatWorker {

    @Autowired
    private TrackStatService trackStatService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private UserService userService;

    /**
     * 每天凌晨2点半
     */
    @Scheduled(cron = "0 30 2 * * ?")
    public void statByDay() {
        statByDay(new Date());
    }

    public void statByDay(Date date) {
        List<User> users = userService.getAll();
        Date cur = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date yesterday = DateUtils.addDays(cur, -1);

        log.debug("stat day: {} to {}", yesterday, cur);

        List<TrackStat> trackStats = new ArrayList<>();
        // 统计每个用户前一天的巡护总次数、巡护总时间、巡护总长度
        for (User user : users) {
            TrackStat trackStat = trackService.countUserByDay(user.getId(), LongDate.from(yesterday), LongDate.from(cur));
            log.debug("user: {}, stat: {}", user.getId(), trackStat.getUserId());
            if (user.getId() == trackStat.getUserId()) {
                trackStat.setTotalDay(0);
                trackStat.setType(0);
                trackStat.setDate(ShortDate.from(yesterday));
                trackStats.add(trackStat);
            }
        }
        trackStatService.addAll(trackStats);
    }

    /**
     * 每月1日凌晨4点半
     */
    @Scheduled(cron = "0 30 4 1 * ?")
    public void statByMonth() {
        statByMonth(new Date());
    }

    public void statByMonth(Date date) {
        // sql_mode="STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"
        Date month = DateUtils.truncate(date, Calendar.MONTH);
        List<User> users = userService.getAll();
        Date lastMonth = DateUtils.addMonths(month, -1);

        log.debug("stat month: {} to {}", lastMonth, month);

        List<TrackStat> trackStats = new ArrayList<>();
        // 统计每个用户前一个月的巡护总次数、巡护总时间、巡护总长度、巡护总天数
        for (User user : users) {
            TrackStat trackStat = trackStatService.countUserByMonth(user.getId(), ShortDate.from(lastMonth), ShortDate.from(month));
            log.debug("user: {}, stat: {}", user.getId(), trackStat.getUserId());
            if (user.getId() == trackStat.getUserId()) {
                trackStat.setDate(ShortDate.from(lastMonth));
                trackStat.setType(1);
                trackStats.add(trackStat);
            }
        }
        trackStatService.addAll(trackStats);
    }
}
