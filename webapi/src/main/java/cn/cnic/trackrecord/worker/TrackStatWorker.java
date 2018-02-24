package cn.cnic.trackrecord.worker;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.service.TrackStatService;
import cn.cnic.trackrecord.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

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
        List<User> users = userService.getAll();
        Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date yesterday = DateUtils.addDays(today, -1);

        List<TrackStat> trackStats = new ArrayList<>(users.size());
        for (User user : users) {
            TrackStat trackStat = trackService.countUserByDay(user.getId(), LongDate.from(yesterday), LongDate.from(today));
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
        List<User> users = userService.getAll();
        Date month = DateUtils.truncate(new Date(), Calendar.MONTH);
        Date lastMonth = DateUtils.addMonths(month, -1);

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
