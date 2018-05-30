package cn.cnic.trackrecord.web.api.controller;

import cn.cnic.trackrecord.common.ant.Ants;
import cn.cnic.trackrecord.common.ant.FileSource;
import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.date.ShortDate;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import cn.cnic.trackrecord.common.http.HttpRes;
import cn.cnic.trackrecord.common.http.HttpResCode;
import cn.cnic.trackrecord.common.http.plupupload.Plupload;
import cn.cnic.trackrecord.common.http.plupupload.PluploadBean;
import cn.cnic.trackrecord.common.http.plupupload.PluploadCallback;
import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Medias;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.common.xml.Stax.Staxs;
import cn.cnic.trackrecord.core.track.TrackLuceneFormatter;
import cn.cnic.trackrecord.core.track.xml.RouteRecordXml;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackFile;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.entity.User;
import cn.cnic.trackrecord.data.kml.RouteRecord;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import cn.cnic.trackrecord.plugin.hadoop.FileMeta;
import cn.cnic.trackrecord.plugin.hadoop.Hadoops;
import cn.cnic.trackrecord.plugin.lucene.LuceneBean;
import cn.cnic.trackrecord.plugin.lucene.LuceneQueryUtils;
import cn.cnic.trackrecord.plugin.lucene.PageResult;
import cn.cnic.trackrecord.service.TrackFileService;
import cn.cnic.trackrecord.service.TrackService;
import cn.cnic.trackrecord.service.TrackStatService;
import cn.cnic.trackrecord.web.Const;
import cn.cnic.trackrecord.web.config.property.TrackFileProperties;
import cn.cnic.trackrecord.web.identity.UserDetailsServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "轨迹API", description = "轨迹API", tags = "TrackApi")
@Controller
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

    @Autowired
    private Hadoops hadoops;

    @Autowired
    private LuceneBean luceneBean;

    @Autowired
    private TrackLuceneFormatter trackLuceneFormatter;

    @Autowired
    private TrackStatService trackStatService;

    /**
     * 用户上传文件，服务器端接收之后忘track_file表中插入一条上传记录，并返回
     * @see cn.cnic.trackrecord.data.entity.TrackFile 。
     * 上传轨迹由
     * @see cn.cnic.trackrecord.worker.TrackFileWorker 任务进行处理。
     * @param plupload 上传参数
     * @return
     */
    @ApiOperation(value = "轨迹文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "文件名(不传默认使用文件名)", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "chunks", value = "文件分块总个数(文件分块上传使用参数)", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "chunk", value = "当前块数,从0计数(文件分块上传使用参数)", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "file", value = "文件", dataType = "__file", paramType = "form", required = true)
    })
    @RequestMapping(value = "file/upload", method = RequestMethod.POST, consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ResponseBody
    public HttpRes<TrackFile> upload(Plupload plupload) {
        try {
            TrackFile trackFile = pluploadBean.upload(plupload, properties.getUploadPath(), new TrackFilePluploadCallback());
            if (Objects.nonNull(trackFile)) {
                User user = userDetailsService.getLoginUser();
                trackFile.setUserId(user.getId());
                trackFile.setUserName(user.getName());
                trackFileService.add(trackFile);
            }
            return HttpRes.success(trackFile);
        } catch (IOException e) {
            return HttpRes.fail("文件上传失败", null);
        }
    }

    @ApiOperation(value = "获取最近一周轨迹文件上传状态")
    @RequestMapping(value = "file/upload/state", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<List<TrackFile>> getTrackFile() {
        LongDate startTime = LongDate.from(DateUtils.addDays(new Date(), -7));
        return HttpRes.success(trackFileService.getByStartUploadTimeAndUserId(startTime, userDetailsService.getLoginUser().getId()));
    }

    @ApiOperation(value = "根据轨迹上传文件id获取轨迹上传文件信息")
    @RequestMapping(value = "file/{track_file_id}", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<TrackFile> getTrackFileById(@PathVariable("track_file_id") int trackFileId) {
        return HttpRes.success(trackFileService.get(trackFileId));
    }

    @ApiOperation(value = "根据用户id获取用户所有轨迹")
    @RequestMapping(value = "{user_id:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<List<Track>> getAllTracksByUserId(@PathVariable("user_id") int userId) {
        return HttpRes.success(trackService.getByUserId(userId));
    }

    @ApiOperation(value = "轨迹搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间,格式:yyyy-MM-dd HH:mm:ss", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间,格式:yyyy-MM-dd HH:mm:ss", dataType = "string", paramType = "query"),
    })
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<PageInfo<Track>> search(TrackSearchParams params) {
        try {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            if (Objects.nonNull(params.getUserId())) {
                //数字精确搜索
                Query timeQuery = IntPoint.newExactQuery("userId", params.getUserId());
                queryBuilder.add(timeQuery, BooleanClause.Occur.MUST);
            }
            String keyWordStr = params.getKeyword().trim();
            if (!keyWordStr.isEmpty()) {
                //多关键字搜索
                String[] fields = {"name", "userName", "keySitesList", "annotation"};
                String[] keywords = keyWordStr.split(" "); // 以空格分隔出多关键字
                List<String> stringList = new ArrayList<>();
                for (String keyword : keywords) {
                    if (!keyword.isEmpty()) {
                        stringList.add(keyword);
                    }
                }
                Query keywordQuery = LuceneQueryUtils.multiFieldQuery(stringList.toArray(new String[0]), fields);
                queryBuilder.add(keywordQuery, BooleanClause.Occur.SHOULD);
            }
            if ((Objects.nonNull(params.getStartTime()) && !params.getStartTime().equals(LongDate.NullValue))
                    || (Objects.nonNull(params.getEndTime()) && !params.getEndTime().equals(LongDate.NullValue))) {
                if (params.getEndTime().equals(LongDate.NullValue)) {
                    params.setEndTime(new LongDate());
                }
                //数字范围搜索
                Query timeQuery = IntPoint.newRangeQuery("startTime", params.getStartTime().getValue(), params.getEndTime().getValue());
                queryBuilder.add(timeQuery, BooleanClause.Occur.MUST);
            }
            if (params.getDistance() > 0 && Objects.nonNull(params.getLat()) && Objects.nonNull(params.getLng())) {
                //范围搜索
                Query distanceQuery = LuceneQueryUtils.spatialCircleQuery(params.getLng(), params.getLat(), params.getDistance());
                queryBuilder.add(distanceQuery, BooleanClause.Occur.MUST);
            }
            BooleanQuery query = queryBuilder.build();
            if (query.clauses().isEmpty()) { // 搜索条件为空，返回所有文档
                query = queryBuilder.add(new MatchAllDocsQuery(), BooleanClause.Occur.SHOULD).build();
            }
            PageResult pageResult = luceneBean.search(query, params.getPageNum(), params.getPageSize());
            Page<Track> page = new Page<>(params.getPageNum(), params.getPageSize());
            page.setTotal(pageResult.getTotal());

            for (Document doc : pageResult.getDocs()) {
                page.add(trackLuceneFormatter.from(doc).getTrack());
            }
            return HttpRes.success(new PageInfo<>(page));
        } catch (Exception e) {
            log.error(e.getMessage());
            return HttpRes.fail();
        }
    }

    @ApiOperation(value = "获取整个轨迹路线信息")
    @RequestMapping(value = "route/{id}", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<RouteRecord> getRouteRecord(@ApiParam(name = "id", value = "轨迹id") @PathVariable int id) {
        try {
            final HttpRes<RouteRecord> res = new HttpRes<>();
            res.setCode(HttpResCode.SUCCESS.getCode());

            Track track = trackService.get(id);
            // 获得文件元信息
            FileMeta fileMeta = hadoops.parsePath(track.getPath(), properties.getRouteRecordFileName());
            hadoops.readToCallBack(String.valueOf(track.getUserId()), fileMeta, in -> {
                // 对hdfs文件流解析
                RouteRecordXml routeRecordXml = new RouteRecordXml();
                try {
                    res.setData(Staxs.parse(routeRecordXml, in));
                } catch (XMLStreamException e) {
                    log.error(e.getMessage());
                    res.setCode(HttpResCode.FAIL.getCode());
                    res.setMessage("解析轨迹元信息出错");
                }
            });
            return res;
        } catch (IOException e) {
            log.error(e.getMessage());
            return HttpRes.fail("解析轨迹元信息出错", null);
        }

    }

    @ApiOperation(value = "获取轨迹图片")
    @RequestMapping(value = "{id}/photo/{name:.+}", method = RequestMethod.GET)
    public void getPhoto(@ApiParam(name = "id", value = "轨迹id") @PathVariable int id,
                         @ApiParam(name = "name", value = "轨迹图片名称") @PathVariable String name,
                         HttpServletResponse res) {
        try {
            Track track = trackService.get(id);
            FileMeta fileInfo = hadoops.parsePath(track.getPath(), name);
            if (Objects.isNull(fileInfo)) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            res.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            if (Objects.nonNull(fileInfo.getThumb())) { // 有压缩图就获取压缩图的元信息，返回压缩图
               fileInfo = fileInfo.getThumb();
            }
            // 将hdfs文件流读取到 HttpServletResponse 的响应流中
            hadoops.readToOutputStream(String.valueOf(track.getUserId()), fileInfo, res.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @ApiOperation(value = "获取轨迹音/视频")
    @RequestMapping(value = "{id}/{media:audio|video}/{name:.+}", method = RequestMethod.GET)
    public void getVideo(@ApiParam(name = "id", value = "轨迹id") @PathVariable int id,
                                       @ApiParam(name = "media", value = "多媒体类型(取值audio或者video)" ) @PathVariable String media,
                                       @ApiParam(name = "name", value = "轨迹图片名称") @PathVariable String name,
                                       HttpServletRequest req, HttpServletResponse res) {
        try {
            Track track = trackService.get(id);
            FileMeta fileInfo = hadoops.parsePath(track.getPath(), name);
            if (Objects.isNull(fileInfo)) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (Objects.nonNull(fileInfo.getThumb())) {
                fileInfo = fileInfo.getThumb();
            }

            // 根据请求头部获读取的音视频文件的范围，播放音视频是有range头部，下载没有range头部，根据range头部来区分是播放还是下载
            int offset = 0;
            String range = req.getHeader("Range");
            if (Objects.isNull(range)) { // 下载
                res.setHeader("Content-Disposition", "attachment; filename=" + name);
                res.setContentLength(fileInfo.getSize());
            } else { // 音视频播放
                offset = Integer.valueOf(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
                int end = range.endsWith("-") ? fileInfo.getSize() - 1 : Integer.valueOf(range.substring(range.indexOf("-") + 1));
                res.setHeader("Content-Range", "bytes " + offset + "-" + end + "/" + String.valueOf(fileInfo.getSize()));
                res.setStatus(206);
            }

            res.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            hadoops.readToOutputStream(String.valueOf(track.getUserId()), fileInfo, offset, res.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @ApiOperation(value = "根据轨迹id下载轨迹")
    @RequestMapping(value = "download/{id}", method = RequestMethod.GET)
    public void download(@ApiParam(name = "id", value = "轨迹id") @PathVariable int id, HttpServletResponse res) {
        try {
            Track track = trackService.get(id);
            List<FileMeta> fileMetas = hadoops.parsePath(track.getPath());
            List<FileSource> sources = new ArrayList<>(fileMetas.size());
            // 获取kmz轨迹中的所有FileSource信息，在一起进行压缩
            for (FileMeta meta : fileMetas) {
                String pathPrefix = "";
                if (Medias.isImage(meta.getName())) {
                    pathPrefix = "photo";
                } else if (Medias.isVideo(meta.getName())) {
                    pathPrefix = "video";
                } else if (Medias.isAudio(meta.getName())) {
                    pathPrefix = "audio";
                }
                sources.add(new FileSource(Files.getPathString(pathPrefix, meta.getName()),
                        os -> hadoops.readToOutputStream(String.valueOf(track.getUserId()), meta, os, false)));
            }

            res.setHeader("Content-Disposition", "attachment; filename=" + track.getFilename());
            res.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//          注意压缩的kmz文件大小和track.getFileSize()不是一个大小，所以不能通过res.setContentLength(track.getFileSize())来设置文件大小，否则前端会下载失败

            Ants.zip(sources, res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "根据月份获取所有用户的统计信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "month", value = "开始时间,格式:yyyy-MM-dd", dataType = "string", paramType = "query", required = true)
    )
    @RequestMapping(value = "stat/month", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<List<TrackStat>> statByMonth(@RequestParam ShortDate month) {
        return HttpRes.success(trackStatService.getByMonth(month));
    }

    @ApiOperation(value = "根据月份和用户id获取该月内用户的所有日统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "beginTime", value = "开始时间,格式:yyyy-MM-dd", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间,格式:yyyy-MM-dd", dataType = "string", paramType = "query", required = true)
    })
    @RequestMapping(value = "stat/day", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<List<TrackStat>> stat(@RequestParam int userId, @RequestParam ShortDate beginTime, @RequestParam ShortDate endTime) {
        return HttpRes.success(trackStatService.getByUserIdAndRangeDay(userId, beginTime, endTime));
    }

    private class TrackFilePluploadCallback implements PluploadCallback<TrackFile> {
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
            trackFile.setTries(properties.getTries());
            trackFile.setFileSize((int) file.length());
            return trackFile;
        }
    }
}
