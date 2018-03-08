package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackDao {
    int addAndGetId(Track track);
    boolean existByMd5AndFileSize(@Param("md5") String md5, @Param("fileSize") int fileSize);
    List<Track> getByTrackSearchParams(TrackSearchParams params);

    Track get(@Param("id") int id);
    TrackStat countUserByDay(@Param("userId") int userId, @Param("beginTime") LongDate beginTime, @Param("endTime") LongDate endTime);

    List<Track> getAll();

    List<Track> getByUserId(@Param("userId") int userId);
}
