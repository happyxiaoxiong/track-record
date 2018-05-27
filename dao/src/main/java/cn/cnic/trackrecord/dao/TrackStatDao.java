package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.data.entity.TrackStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackStatDao {
    /**
     * 添加轨迹统计信息
     * @param trackStat
     * @return
     */
    int add(TrackStat trackStat);

    /**
     * 批量添加轨迹统计信息
     * @param trackStats
     * @return
     */
    int addAll(List<TrackStat> trackStats);

    /**
     * 按月统计某个用户的所有轨迹信息
     * @param userId 用户id
     * @param beginTime 某月开始时间（大于等于）
     * @param endTime 某月结束时间（小于）
     * @return
     */
    TrackStat countUserByMonth(@Param("userId") int userId, @Param("beginTime") ShortDate beginTime, @Param("endTime") ShortDate endTime);

    /**
     * 获取某个月的所有统计信息
     * @param date
     * @return
     */
    List<TrackStat> getByMonth(@Param("date") ShortDate date);

    /**
     * 获取某个用户在时间段内的统计信息
     * @param userId
     * @param beginTime 开始时间（大于等于）
     * @param endTime 结束时间（小于）
     * @return
     */
    List<TrackStat> getByUserIdAndRangeDay(@Param("userId") int userId, @Param("beginTime") ShortDate beginTime, @Param("endTime") ShortDate endTime);
}
