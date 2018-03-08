package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.core.track.TrackLuceneFormatter;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.data.lucene.TrackLucene;
import cn.cnic.trackrecord.plugin.lucene.LuceneBean;
import cn.cnic.trackrecord.service.TrackFileService;
import cn.cnic.trackrecord.service.TrackPointService;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.service.UserService;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.config.property.TrackFileProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Api(value = "测试API", description = "测试API", tags = "Test")
@RestController
@RequestMapping(value = Const.API_ROOT + "test")
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TrackFileProperties properties;

    @Autowired
    private TrackFileService trackFileService;

    @Autowired
    private LuceneBean luceneBean;

    @Autowired
    private TrackPointService trackPointService;

    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.GET, value = "encode")
    public HttpRes<?> encodePassword() {
        for (User user : userService.getAll()) {
            if (user.getPassword().length() < 20) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userService.update(user);
            }
        }
        return HttpRes.success();
    }

    @RequestMapping(method = RequestMethod.GET, value = "import")
    public HttpRes<?> importTracks() {
        try {
            for (String fileName : FileUtils.readLines(new File("/root/path.txt"), "UTF-8")) {
                File file = new File(fileName + ".kmz");
                File destFile = Files.getFile(properties.getUploadPath(), file.getName());
                log.debug("srcFile: {}, destFile: {}", file.getName(), destFile.getAbsolutePath());
                if (file.exists()) {
                    log.debug("copy file: srcFile: {}, destFile: {}", file.getName(), destFile.getAbsolutePath());
                    FileUtils.copyFile(file, destFile);
                    LongDate curTime = new LongDate();

                    TrackFile trackFile = new TrackFile();
                    trackFile.setState(TrackFileState.UPLOAD_SUCCESS);
                    trackFile.setUploadTime(curTime);
                    trackFile.setPath(file.getAbsolutePath());
                    trackFile.setFilename(file.getName());
                    trackFile.setMd5("");//计算MD5耗时,在任务中计算
                    trackFile.setUpdateTime(curTime);
                    trackFile.setComment("");
                    trackFile.setTries(0);
                    trackFile.setFileSize((int) file.length());
                    trackFileService.add(trackFile);
                }
            }
            return HttpRes.success();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return HttpRes.fail();
        }
    }

    @ApiOperation(value = "创建索引")
    @RequestMapping(value = "lucene", method = RequestMethod.GET)
    public HttpRes<?> lucene() {
        List<Track> tracks = trackService.getAll();
        for (Track track: tracks) {
            List<TrackPoint> points = trackPointService.getByTrackId(track.getId());
            try {
                luceneBean.add(new TrackLuceneFormatter(), new TrackLucene(track, points));
            } catch (IOException e) {
                log.error(e.getMessage());
                return HttpRes.fail();
            }
        }
        return HttpRes.success();
    }
}