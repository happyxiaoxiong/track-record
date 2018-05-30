package cn.cnic.trackrecord.worker;

import cn.cnic.trackrecord.common.ant.Ants;
import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.common.xml.Stax.Staxs;
import cn.cnic.trackrecord.core.track.TrackLuceneFormatter;
import cn.cnic.trackrecord.core.track.xml.RouteRecordXml;
import cn.cnic.trackrecord.core.track.xml.TrackDetailXml;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.data.kml.PlaceMark;
import cn.cnic.trackrecord.data.kml.RoutePlaceMark;
import cn.cnic.trackrecord.data.kml.RouteRecord;
import cn.cnic.trackrecord.data.lucene.TrackLucene;
import cn.cnic.trackrecord.plugin.hadoop.Hadoops;
import cn.cnic.trackrecord.plugin.lucene.LuceneBean;
import cn.cnic.trackrecord.service.TrackFileService;
import cn.cnic.trackrecord.service.TrackPointService;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.service.UserService;
import cn.cnic.trackrecord.web.config.property.TrackFileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * kmz轨迹文件解析任务
 */
@Slf4j
@Component
public class TrackFileWorker {
    @Autowired
    private TrackFileProperties properties;

    @Autowired
    private TrackFileService trackFileService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrackPointService trackPointService;

    @Autowired
    private Hadoops hadoops;

    @Autowired
    private LuceneBean luceneBean;

    @Autowired
    private TrackLuceneFormatter trackLuceneFormatter;

    /**
     * 每个1分钟查询数据库看是否有kmz文件未处理
     */
    @Scheduled(fixedDelay = 1000 * 60)
    public void work() {
        List<TrackFile> trackFiles = trackFileService.getUnfinished();
        for (TrackFile trackFile : trackFiles) {
            process(trackFile);
        }
    }

    /**
     * 处理轨迹文件。
     *
     * 1.验证轨迹文件是否上传重复
     * 2.解压轨迹文件
     * 3.提取轨迹数据并保存
     *
     * 正常解析出错不重试，其他异常导致的解析终端会重试解析
     * @param trackFile
     */
    private void process(TrackFile trackFile) {
        checkTries(trackFile);
        if (TrackFileState.VERIFYING.equals(trackFile.getState())) {
            verifying(trackFile);
        }

        if (TrackFileState.UNZIPPING.equals(trackFile.getState())) {
            unzip(trackFile);
        }

        if (TrackFileState.EXTRACTING_AND_SAVING.equals(trackFile.getState())) {
            extractAndSave(trackFile);
        }
    }

    /**
     * 检查次数是否超限并累加
     * @param trackFile
     */
    private void checkTries(TrackFile trackFile) {
        if (trackFile.getTries() == 0) {
            trackFile.setState(TrackFileState.TRY_EXCEED);
            trackFile.setComment("错误:已重试" + properties.getTries() + "次");
        } else {
            trackFile.setTries(trackFile.getTries() - 1);
            if (TrackFileState.UPLOAD_SUCCESS.equals(trackFile.getState())) {
                trackFile.setState(TrackFileState.VERIFYING);
                trackFile.setComment("正在计算文件MD5");
            }
        }
        trackFile.setUpdateTime(new LongDate());
        trackFileService.update(trackFile);
    }

    /**
     * 计算文件md5，验证文件是否存在
     * @param trackFile
     * @return
     */
    private void verifying(TrackFile trackFile) {
        try {
            String md5 = DigestUtils.md5DigestAsHex(new FileInputStream(trackFile.getPath()));
            trackFile.setMd5(md5);
            if (trackService.existByMd5AndFileSize(md5, trackFile.getFileSize())) {
                trackFile.setState(TrackFileState.VERIFY_FAIL);
                trackFile.setComment("错误:文件重复");
            } else {
                trackFile.setState(TrackFileState.UNZIPPING);
                trackFile.setComment("正在解压文件");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            trackFile.setState(TrackFileState.VERIFY_FAIL);
            trackFile.setComment("错误:文件读取异常");
        } finally {
            trackFile.setUpdateTime(new LongDate());
            trackFileService.update(trackFile);
        }
    }

    /**
     * 解压kmz文件
     * @param trackFile
     */
    private void unzip(TrackFile trackFile) {
        try {
            String trackPath = Files.getPathString(properties.getUnzipPath(), trackFile.getFilename().replaceFirst("\\.\\w*$", ""));
            Ants.unzip(trackFile.getPath(), trackPath, false);

            trackFile.setPath(trackPath);//更换为解压目录
            trackFile.setState(TrackFileState.EXTRACTING_AND_SAVING);
            trackFile.setComment("正在提取数据并保存");
        } catch (IOException e) {
            log.error(e.getMessage());
            trackFile.setState(TrackFileState.UNZIP_FAIL);
            trackFile.setComment("错误:解压异常");
        } finally {
            trackFile.setUpdateTime(new LongDate());
            trackFileService.update(trackFile);
        }
    }

    /**
     * 提取kml文件数据,上传轨迹文件到hdfs中，保存到数据库中,建立轨迹数据索引
     * @param trackFile
     */
    private void extractAndSave(TrackFile trackFile) {
        try {
            String realTrackPath = Files.getRealTrackPath(trackFile.getPath());

            //解析TrackDetail.xml文件
            Track track;
            try {
                track = Staxs.parse(new TrackDetailXml(), Files.getPathString(realTrackPath, properties.getTrackDetailFileName()));
            } catch (XMLStreamException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new Exception(properties.getTrackDetailFileName() + "解析错误");
            }

            // 判断用户id是否存在，兼容老版本
            if (track.getUserId() <= 0) {
                // get user id
                User user = userService.getByName(track.getUserName());
                if (Objects.isNull(user)) {
                    log.error("name '{}' doesn't exist in user table, track file is {}", track.getUserName(), trackFile.getFilename());
                    throw new Exception("用户名不存在");
                }
                track.setUserId(user.getId());
            }
            track.setUploadUserId(trackFile.getUserId());
            if (Objects.nonNull(trackFile.getUserName())) {
                track.setUploadUserName(trackFile.getUserName());
            } else {
                track.setUploadUserName(track.getUserName());
            }

            log.debug("{}", track);

            //解析RouteRecord.kml文件
            RouteRecord routeRecord;
            try {
                routeRecord = Staxs.parse(new RouteRecordXml(), Files.getPathString(realTrackPath, properties.getRouteRecordFileName()));
            } catch (XMLStreamException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new Exception(properties.getRouteRecordFileName() + "解析错误");
            }

            //保存到hadoop中
            try {
                track.setPath(hadoops.appendKmzFiles(String.valueOf(track.getUserId()), new File(trackFile.getPath()), false));
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new Exception("文件保存到hdfs发生错误");
            }

            track.setFileSize(trackFile.getFileSize());
            track.setFilename(trackFile.getFilename());
            track.setMd5(trackFile.getMd5());
            track.setUploadTime(new LongDate());

            // 保存track和trackPoints
            trackService.addAndGetId(track);
            List<TrackPoint> points = new LinkedList<>();
            for (PlaceMark placeMark : routeRecord.getPlaceMarks()) {
                if (placeMark instanceof RoutePlaceMark) {
                    for (TrackPoint point : ((RoutePlaceMark) placeMark).getPoints()) {
                        point.setTrackId(track.getId());
                        points.add(point);
                    }
                }
            }
            trackPointService.addAll(points);

            //必须是先保存track和trackPoints，然后在建立轨迹索引
            try {
                createIndex(track, points);
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new Exception("轨迹索引创建失败");
            }

            trackFile.setState(TrackFileState.FINISH);
            trackFile.setTries(0);
            trackFile.setComment("操作成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            trackFile.setState(TrackFileState.EXTRACT_AND_SAVE_FAIL);
            trackFile.setComment(e.getMessage());
        }
        finally {
            trackFile.setUpdateTime(new LongDate());
            trackFileService.update(trackFile);
        }
    }

    private void createIndex(Track track, List<TrackPoint> trackPoints) throws IOException {
        TrackLucene trackLucene = new TrackLucene(track, trackPoints);
        luceneBean.add(trackLuceneFormatter, trackLucene);
    }

}
