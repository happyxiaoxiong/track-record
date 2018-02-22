package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.TrackPoint;

import java.util.List;

public interface TrackPointService {
    int addAll(List<TrackPoint> points);
    List<TrackPoint> getByTrackId(int trackId);
}
