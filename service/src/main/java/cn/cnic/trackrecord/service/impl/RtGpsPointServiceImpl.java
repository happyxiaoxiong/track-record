package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.dao.RtGpsPointDao;
import cn.cnic.trackrecord.data.entity.RtGpsPoint;
import cn.cnic.trackrecord.service.RtGpsPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RtGpsPointServiceImpl implements RtGpsPointService {

    @Autowired
    private RtGpsPointDao rtGpsPointDao;

    @Override
    public List<RtGpsPoint> getByGtTime(LongDate startTime) {
        return rtGpsPointDao.getByGteTime(startTime);
    }

    @Override
    public int add(RtGpsPoint gpsPoint) {
        return rtGpsPointDao.add(gpsPoint);
    }
}
