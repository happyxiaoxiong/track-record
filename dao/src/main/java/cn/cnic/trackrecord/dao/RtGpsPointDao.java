package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.RtGpsPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RtGpsPointDao {
    /**
     * 获取所有大于等于startTime时间的用户实时位置
     * @param startTime 开始时间（大于等于）
     * @return
     */
    List<RtGpsPoint> getByGteTime(@Param("startTime") LongDate startTime);

    /**
     * 根据用户id获取该用户的实时位置
     * @param userId
     * @return
     */
    RtGpsPoint getByUserId(@Param("userId") int userId);

    /**
     * 根据用户id获取时间段内该用户的位置轨迹
     * @param userId
     * @param startTime 开始时间（大于等于）
     * @param endTime 结束时间（小于等于）
     * @return
     */
    List<RtGpsPoint> getRouteByUserId(@Param("userId") int userId,
                                      @Param("startTime") LongDate startTime,
                                      @Param("endTime") LongDate endTime);

    /**
     * 添加用户实时位置
     * @param gpsPoint
     * @return
     */
    int add(RtGpsPoint gpsPoint);
}
