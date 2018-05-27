package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackDao {
    /**
     * 添加轨迹信息并获取id
     * @param track
     * @return
     */
    int addAndGetId(Track track);

    /**
     * 判断是否存在轨迹文件，根据md5和文件大小
     * @param md5
     * @param fileSize
     * @return
     */
    boolean existByMd5AndFileSize(@Param("md5") String md5, @Param("fileSize") int fileSize);

    /**
     * 轨迹搜索，目前只是简单实现，未包含所有查询字段，已废弃，如果需要使用，需要重新实现。轨迹搜索已使用lucene检索
     * @param params
     * @return
     */
    @Deprecated
    List<Track> getByTrackSearchParams(TrackSearchParams params);

    /**
     * 根据轨迹id获取轨迹信息
     * @param id
     * @return
     */
    Track get(@Param("id") int id);

    /**
     * 按天统计用户的轨迹信息
     * @param userId 用户id
     * @param beginTime 某天的开始时间（大于等于）
     * @param endTime 某天结束时间（小于）
     * @return
     */
    TrackStat countUserByDay(@Param("userId") int userId, @Param("beginTime") LongDate beginTime, @Param("endTime") LongDate endTime);

    /**
     * 获取所有轨迹信息
     * @return
     */
    List<Track> getAll();

    /**
     *  根据用户id获取该用户的所有轨迹信息
     * @param userId
     * @return
     */
    List<Track> getByUserId(@Param("userId") int userId);
}
