package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.RtGpsPoint;

import java.util.List;

public interface RtGpsPointService {
    /**
     * 获取所有大于等于startTime时间的用户实时位置
     * @param startTime 开始时间（大于等于）
     * @return
     */
    List<RtGpsPoint> getByGteTime(LongDate startTime);

    /**
     * 添加用户实时位置
     * @param gpsPoint
     * @return
     */
    int add(RtGpsPoint gpsPoint);
}
