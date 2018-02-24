package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.dao.TrackPointDao;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.service.TrackPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackPointServiceImpl implements TrackPointService {

    @Autowired
    private TrackPointDao trackPointDao;

    @Override
    public int addAll(List<TrackPoint> points) {
        return points.isEmpty() ? 0 : trackPointDao.addAll(points);
    }

    @Override
    public List<TrackPoint> getByTrackId(int trackId) {
        return trackPointDao.getByTrackId(trackId);
    }
}
