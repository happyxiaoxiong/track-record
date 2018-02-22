package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.TrackPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackPointDao {
    int addAll(List<TrackPoint> points);

    List<TrackPoint> getByTrackId(@Param("trackId") int trackId);
}
