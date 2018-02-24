package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.dao.TrackStatDao;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.service.TrackStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackStatServiceImpl implements TrackStatService {

    @Autowired
    private TrackStatDao trackStatDao;

    @Override
    public int add(TrackStat trackStat) {
        return trackStatDao.add(trackStat);
    }

    @Override
    public int addAll(List<TrackStat> trackStats) {
        return trackStats.isEmpty() ? 0 : trackStatDao.addAll(trackStats);
    }

    @Override
    public TrackStat countUserByMonth(int userId, ShortDate beginTime, ShortDate endTime) {
        return trackStatDao.countUserByMonth(userId, beginTime, endTime);
    }
}
