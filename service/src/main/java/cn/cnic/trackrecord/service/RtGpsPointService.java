package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.RtGpsPoint;

import java.util.List;

public interface RtGpsPointService {
    List<RtGpsPoint> getByGtTime(LongDate startTime);

    int add(RtGpsPoint gpsPoint);
}
