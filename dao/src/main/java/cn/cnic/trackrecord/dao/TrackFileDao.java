package cn.cnic.trackrecord.dao;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.TrackFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrackFileDao {
    /**
     * 添加轨迹上传记录
     * @param trackFile
     * @return
     */
    int add(TrackFile trackFile);

    /**
     * 判断是否存在轨迹文件，根据md5和文件大小
     * @param md5
     * @param fileSize
     * @return
     */
    boolean existByMd5AndFileSize(@Param("md5") String md5, @Param("fileSize") int fileSize);

    /**
     * 获取所有未处理完的轨迹文件
     * @return
     */
    List<TrackFile> getUnfinished();

    /**
     * 根据用户id获取该用户所有在开始时间后的上传轨迹文件记录
     * @param uploadTime 上传时间（大于等于）
     * @param userId
     * @return
     */
    List<TrackFile> getByStartUploadTimeAndUserId(@Param("uploadTime") LongDate uploadTime, @Param("userId") int userId);

    /**
     * 根据id获取轨迹上传记录
     * @param id
     * @return
     */
    TrackFile get(@Param("id") int id);

    /**
     * 更新轨迹上传文件记录
     * @param trackFile
     * @return
     */
    int update(TrackFile trackFile);
}
