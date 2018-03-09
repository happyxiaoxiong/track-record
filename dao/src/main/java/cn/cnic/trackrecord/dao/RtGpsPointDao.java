package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.RtGpsPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RtGpsPointDao {
    List<RtGpsPoint> getByGtTime(@Param("startTime") LongDate startTime);
    RtGpsPoint getByUserId(@Param("userId") int userId);
    List<RtGpsPoint> getRouteByUserId(@Param("userId") int userId,
                                      @Param("startTime") LongDate startTime,
                                      @Param("endTime") LongDate endTime);

    int add(RtGpsPoint gpsPoint);
}
