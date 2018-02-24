package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.TrackFile;

import java.util.List;

public interface TrackFileService {
    int add(TrackFile trackFile);

    boolean existByMd5AndFileSize(String md5, int fileSize);

    List<TrackFile> getUnfinished();

    List<TrackFile> getByStartUploadTimeAndUserId(LongDate uploadTime, int userId);

    TrackFile get(int trackFileId);


    int update(TrackFile trackFile);

}
