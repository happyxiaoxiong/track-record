package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.data.entity.Track;
import org.apache.ibatis.annotations.Param;

public interface TrackDao {
    int addAndGetId(Track track);
    boolean existByMd5AndFileSize(@Param("md5") String md5, @Param("fileSize") int fileSize);
}
