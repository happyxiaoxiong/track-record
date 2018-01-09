package cn.cnic.trackrecord.service.impl;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.dao.TrackFileDao;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.service.TrackFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackFileServiceImpl implements TrackFileService {
    @Autowired
    private TrackFileDao trackFileDao;

    @Override
    public int add(TrackFile trackFile) {
        return trackFileDao.add(trackFile);
    }

    @Override
    public boolean existByMd5AndFileSize(String md5, int fileSize) {
        return trackFileDao.existByMd5AndFileSize(md5, fileSize);
    }

    @Override
    public List<TrackFile> getUnfinished() {
        return trackFileDao.getUnfinished();
    }

    @Override
    public List<TrackFile> getByStartUploadTimeAndUserId(LongDate uploadTime, int userId) {
        return trackFileDao.getByStartUploadTimeAndUserId(uploadTime, userId);
    }

    @Override
    public TrackFile getById(int trackFileId) {
        return trackFileDao.getById(trackFileId);
    }

    @Override
    public boolean update(TrackFile trackFile) {
        return trackFileDao.update(trackFile);
    }


}
