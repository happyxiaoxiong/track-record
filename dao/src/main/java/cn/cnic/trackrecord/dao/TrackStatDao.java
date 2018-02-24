package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.data.entity.TrackStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackStatDao {
    int add(TrackStat trackStat);
    int addAll(List<TrackStat> trackStats);

    TrackStat countUserByMonth(@Param("userId") int userId, @Param("beginTime") ShortDate beginTime, @Param("endTime") ShortDate endTime);
}
