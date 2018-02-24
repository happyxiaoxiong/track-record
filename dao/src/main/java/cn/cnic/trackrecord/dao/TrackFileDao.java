package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.TrackFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackFileDao {
    int add(TrackFile trackFile);

    boolean existByMd5AndFileSize(@Param("md5") String md5, @Param("fileSize") int fileSize);

    List<TrackFile> getUnfinished();

    List<TrackFile> getByStartUploadTimeAndUserId(@Param("uploadTime") LongDate uploadTime, @Param("userId") int userId);

    TrackFile get(@Param("id") int id);

    int update(TrackFile trackFile);
}
