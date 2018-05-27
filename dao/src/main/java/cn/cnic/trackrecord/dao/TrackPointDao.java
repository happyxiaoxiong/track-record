package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.TrackPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackPointDao {
    /**
     * 批量添加轨迹点
     * @param points 注意points不能为空
     * @return
     */
    int addAll(List<TrackPoint> points);

    /**
     * 根据轨迹id获取轨迹的所有轨迹点信息
     * @param trackId
     * @return
     */
    List<TrackPoint> getByTrackId(@Param("trackId") int trackId);
}
