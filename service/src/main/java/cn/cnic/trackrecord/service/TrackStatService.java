package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.data.entity.TrackStat;

import java.util.List;

public interface TrackStatService {
    int add(TrackStat trackStat);
    int addAll(List<TrackStat> trackStats);

    TrackStat countUserByMonth(int userId, ShortDate beginTime, ShortDate endTime);
}
