package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackDao {
    int addAndGetId(Track track);
    boolean existByMd5AndFileSize(@Param("md5") String md5, @Param("fileSize") int fileSize);
    List<TrackFile> getByTrackSearchParams(TrackSearchParams params);
}
