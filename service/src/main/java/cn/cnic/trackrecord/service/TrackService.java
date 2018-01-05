package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.Track;

public interface TrackService {
    int addAndGetId(Track track);
    boolean existByMd5AndFileSize(String md5, int fileSize);
}
