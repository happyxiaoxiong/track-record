package cn.cnic.trackrecord.service;

import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;

import java.util.List;

public interface TrackService {
    int addAndGetId(Track track);

    boolean existByMd5AndFileSize(String md5, int fileSize);
    List<Track> getByTrackSearchParams(TrackSearchParams params);
}
