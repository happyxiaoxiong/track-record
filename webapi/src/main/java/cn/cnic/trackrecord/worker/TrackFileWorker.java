package cn.cnic.trackrecord.worker;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.plugin.sax.SaxParser;
import cn.cnic.trackrecord.core.track.xml.RouteRecordXml;
import cn.cnic.trackrecord.core.track.xml.TrackDetailXml;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.kml.PlaceMark;
import cn.cnic.trackrecord.data.kml.RoutePlaceMark;
import cn.cnic.trackrecord.data.kml.RouteRecord;
import cn.cnic.trackrecord.plugin.ant.UnzipBean;
import cn.cnic.trackrecord.plugin.hadoop.HadoopBean;
import cn.cnic.trackrecord.plugin.hadoop.Position;
import cn.cnic.trackrecord.service.TrackFileService;
import cn.cnic.trackrecord.service.TrackPointService;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.web.config.property.TrackFileProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private TrackPointService trackPointService;

    @Autowired
    private UnzipBean unzipBean;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HadoopBean hadoopBean;

    @Scheduled(fixedDelay = 5000)
    public void work() {
        List<TrackFile> trackFiles = trackFileService.getUnfinished();
        for (TrackFile trackFile : trackFiles) {
            process(trackFile);
        }
    }

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
        if (trackFile.getTries() >= properties.getTries()) {
            trackFile.setState(TrackFileState.TRY_EXCEED);
            trackFile.setComment("错误:已重试" + properties.getTries() + "次");
        } else {
            trackFile.setTries(trackFile.getTries() + 1);
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
            log.debug(e.getMessage());
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
            String trackPath = Files.getPath(properties.getUnzipPath(), trackFile.getFilename().replaceFirst("\\.\\w*$", ""));
            unzipBean.unzip(trackFile.getPath(), trackPath, false);

            trackFile.setPath(trackPath);//更换为解压目录
            trackFile.setState(TrackFileState.EXTRACTING_AND_SAVING);
            trackFile.setComment("正在提取数据并保存");
        } catch (IOException e) {
            log.debug(e.getMessage());
            trackFile.setState(TrackFileState.UNZIP_FAIL);
            trackFile.setComment("错误:解压异常");
        } finally {
            trackFile.setUpdateTime(new LongDate());
            trackFileService.update(trackFile);
        }
    }

    /**
     * 提取kml文件数据,保存在数据库中
     * @param trackFile
     */
    private void extractAndSave(TrackFile trackFile) {
        try {
            //TODO
            String realTrackPath = Files.getRealTrackPath(trackFile.getPath());
            Track track = SaxParser.parse(new TrackDetailXml(Files.getPath(realTrackPath, properties.getTrackDetailFileName())));
            RouteRecord routeRecord = SaxParser.parse(new RouteRecordXml(Files.getPath(realTrackPath, properties.getRouteRecordFileName())));

            Map<String, Position> map = hadoopBean.write(new File(trackFile.getPath()), false);
            track.setPath(objectMapper.writeValueAsString(map));
            track.setFileSize(trackFile.getFileSize());
            track.setMd5(trackFile.getMd5());
            track.setUploadTime(new LongDate());
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
            trackFile.setState(TrackFileState.FINISH);
            trackFile.setComment("操作成功");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.debug(e.getMessage());
            trackFile.setState(TrackFileState.EXTRACT_AND_SAVE_FAIL);
            trackFile.setComment("错误:数据格式异常");
        } finally {
            trackFile.setUpdateTime(new LongDate());
            trackFileService.update(trackFile);
        }
    }
}