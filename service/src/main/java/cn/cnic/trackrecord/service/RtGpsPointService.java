package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.RtGpsPoint;

import java.util.List;

public interface RtGpsPointService {
    List<RtGpsPoint> getAll();

    int add(RtGpsPoint gpsPoint);
}
