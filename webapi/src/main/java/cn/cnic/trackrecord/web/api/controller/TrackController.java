package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.common.http.plupupload.Plupload;
import cn.cnic.trackrecord.common.http.plupupload.PluploadBean;
import cn.cnic.trackrecord.common.http.plupupload.PluploadCallback;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import cn.cnic.trackrecord.service.TrackFileService;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.config.property.TrackFileProperties;
import cn.cnic.trackrecord.web.identity.UserDetailsServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = Const.API_ROOT + "track")
public class TrackController {
    @Autowired
    private TrackFileProperties properties;

    @Autowired
    private PluploadBean pluploadBean;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private TrackFileService trackFileService;

    @ApiOperation(value = "轨迹文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "文件名", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "chunks", value = "文件分块总个数", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "chunk", value = "当前块数,从0计数", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true)
    })
    @RequestMapping(value = "file/upload", method = RequestMethod.POST)
    public HttpRes<TrackFile> upload(Plupload plupload, HttpServletRequest request, HttpServletResponse response) {
        plupload.setRequest(request);
        try {
            TrackFile trackFile = pluploadBean.upload(plupload, properties.getUploadPath(), new TrackFilePluploadCallback());
            if (Objects.nonNull(trackFile)) {
                trackFile.setUserId(userDetailsService.getLoginUser().getId());
                trackFileService.add(trackFile);
            }
            return HttpRes.success(trackFile);
        } catch (IOException e) {
            return HttpRes.fail("文件上传失败", null);
        }
    }

    @ApiOperation(value = "获取最近一周轨迹文件上传状态")
    @RequestMapping(value = "file/upload/state", method = RequestMethod.GET)
    public HttpRes<List<TrackFile>> getTrackFile() {
        LongDate startTime = LongDate.from(DateUtils.addDays(new Date(), -7));
        return HttpRes.success(trackFileService.getByStartUploadTimeAndUserId(startTime, userDetailsService.getLoginUser().getId()));
    }

    @ApiOperation(value = "根据track_file_id获取track_file")
    @RequestMapping(value = "file/{track_file_id}", method = RequestMethod.GET)
    public HttpRes<TrackFile> getTrackFileById(@PathVariable("track_file_id") int trackFileId) {
        return HttpRes.success(trackFileService.getById(trackFileId));
    }

    @ApiOperation(value = "轨迹搜索")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public HttpRes<PageInfo<Track>> search(TrackSearchParams params) {
        // TODO page
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        return HttpRes.success(new PageInfo<>(trackService.getByTrackSearchParams(params)));
    }


    private static class TrackFilePluploadCallback implements PluploadCallback<TrackFile> {
        @Override
        public TrackFile callback(File file) {
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
            return trackFile;
        }
    }
}
