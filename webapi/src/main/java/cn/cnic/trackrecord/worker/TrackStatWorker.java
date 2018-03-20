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

import java.util.*;

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

        List<TrackStat> trackStats = new ArrayList<>(users.size());
        for (User user : users) {
            TrackStat trackStat = trackService.countUserByDay(user.getId(), LongDate.from(yesterday), LongDate.from(cur));
            if (user.getId() == trackStat.getId()) {
                trackStat.setTotalDay(0);
                trackStat.setType(0);
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
        Date month = DateUtils.truncate(date, Calendar.MONTH);
        List<User> users = userService.getAll();
        Date lastMonth = DateUtils.addMonths(month, -1);

        log.debug("stat month: {} to {}", lastMonth, month);

        List<TrackStat> trackStats = new ArrayList<>(users.size());
        for (User user : users) {
            TrackStat trackStat = trackStatService.countUserByMonth(user.getId(), ShortDate.from(lastMonth), ShortDate.from(month));
            if (user.getId() == trackStat.getId()) {
                trackStat.setDate(ShortDate.from(lastMonth));
                trackStat.setType(1);
                trackStats.add(trackStat);
            }
        }
        trackStatService.addAll(trackStats);
    }
}
