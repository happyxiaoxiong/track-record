package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.dao.TrackDao;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import cn.cnic.trackrecord.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackDao trackDao;

    @Override
    public int addAndGetId(Track track) {
        return trackDao.addAndGetId(track);
    }

    @Override
    public boolean existByMd5AndFileSize(String md5, int fileSize) {
        return trackDao.existByMd5AndFileSize(md5, fileSize);
    }

    @Override
    public List<Track> getByTrackSearchParams(TrackSearchParams params) {
        return trackDao.getByTrackSearchParams(params);
    }

    @Override
    public Track get(int id) {
        return trackDao.get(id);
    }

    @Override
    public TrackStat countUserByDay(int userId, LongDate beginTime, LongDate endTime) {
        return trackDao.countUserByDay(userId, beginTime, endTime);
    }

    @Override
    public List<Track> getAll() {
        return trackDao.getAll();
    }

    @Override
    public List<Track> getByUserId(int userId) {
        return trackDao.getByUserId(userId);
    }
}
