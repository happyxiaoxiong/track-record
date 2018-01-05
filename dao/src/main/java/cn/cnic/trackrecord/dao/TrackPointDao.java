package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.TrackPoint;

import java.util.List;

public interface TrackPointDao {
    int addAll(List<TrackPoint> points);
}
