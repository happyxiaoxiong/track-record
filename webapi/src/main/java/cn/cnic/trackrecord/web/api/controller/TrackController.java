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
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.entity.TrackStat;
import cn.cnic.trackrecord.data.kml.RouteRecord;
import cn.cnic.trackrecord.data.lucene.TrackLucene;
import cn.cnic.trackrecord.data.vo.TrackSearchParams;
import cn.cnic.trackrecord.plugin.hadoop.FileMeta;
import cn.cnic.trackrecord.plugin.hadoop.Hadoops;
import cn.cnic.trackrecord.plugin.lucene.LuceneBean;
import cn.cnic.trackrecord.plugin.lucene.LuceneQueryUtils;
import cn.cnic.trackrecord.plugin.lucene.PageResult;
import cn.cnic.trackrecord.service.TrackFileService;
import cn.cnic.trackrecord.service.TrackPointService;
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

    @ApiOperation(value = "轨迹文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "文件名", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "chunks", value = "文件分块总个数", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "chunk", value = "当前块数,从0计数", dataType = "string", paramType = "form", required = true),
            @ApiImplicitParam(name = "file", value = "文件", dataType = "file", required = true)
    })
    @RequestMapping(value = "file/upload", method = RequestMethod.POST)
    @ResponseBody
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
    @ResponseBody
    public HttpRes<List<TrackFile>> getTrackFile() {
        LongDate startTime = LongDate.from(DateUtils.addDays(new Date(), -7));
        return HttpRes.success(trackFileService.getByStartUploadTimeAndUserId(startTime, userDetailsService.getLoginUser().getId()));
    }

    @ApiOperation(value = "根据track_file_id获取track_file")
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
            if (StringUtils.isNotBlank(params.getKeyword())) {
                //多关键字搜索
//                String[] fields = {"name", "userName", "keySitesList", "annotation"};
//                Query keywordQuery = LuceneQueryUtils.multiFieldQuery(params.getKeyword(), fields);
//                queryBuilder.add(keywordQuery, BooleanClause.Occur.SHOULD);
//                queryBuilder.add(new FuzzyQuery(new Term("name", params.getKeyword())), BooleanClause.Occur.SHOULD);
//                queryBuilder.add(new FuzzyQuery(new Term("userName", params.getKeyword())), BooleanClause.Occur.SHOULD);
//                queryBuilder.add(new FuzzyQuery(new Term("keySitesList", params.getKeyword())), BooleanClause.Occur.SHOULD);
//                queryBuilder.add(new FuzzyQuery(new Term("annotation", params.getKeyword())), BooleanClause.Occur.SHOULD);
                BooleanQuery.Builder keywordQueryBuilder = new BooleanQuery.Builder();
                keywordQueryBuilder.add(new WildcardQuery(new Term("name", "*" + params.getKeyword() + "*")), BooleanClause.Occur.SHOULD);
                keywordQueryBuilder.add(new WildcardQuery(new Term("userName", "*" + params.getKeyword() + "*")), BooleanClause.Occur.SHOULD);
                keywordQueryBuilder.add(new WildcardQuery(new Term("keySitesList", "*" + params.getKeyword() + "*")), BooleanClause.Occur.SHOULD);
                keywordQueryBuilder.add(new WildcardQuery(new Term("annotation", "*" + params.getKeyword() + "*")), BooleanClause.Occur.SHOULD);
                queryBuilder.add(keywordQueryBuilder.build(), BooleanClause.Occur.MUST);
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
            if (query.clauses().isEmpty()) {
                query = queryBuilder.add(new MatchAllDocsQuery(), BooleanClause.Occur.SHOULD).build();
            }
            PageResult pageResult = luceneBean.search(query, params.getPageNum(), params.getPageSize());
            Page<Track> page = new Page<>(params.getPageNum(), params.getPageSize());
            page.setTotal(pageResult.getTotal());

            for (Document doc : pageResult.getDocs()) {
                page.add(trackLuceneFormatter.from(doc).getTrack());
            }
            return HttpRes.success(new PageInfo<>(page));
        } catch (IOException e) {
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
            FileMeta fileMeta = hadoops.parsePath(track.getPath(), properties.getRouteRecordFileName());
            RouteRecordXml routeRecordXml = new RouteRecordXml();
            hadoops.readToCallBack(String.valueOf(track.getUserId()), fileMeta, in -> {
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
            if (Objects.nonNull(fileInfo.getThumb())) {
               fileInfo = fileInfo.getThumb();
            }
            hadoops.readToOutputStream(String.valueOf(track.getUserId()), fileInfo, res.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

//    @ApiOperation(value = "获取轨迹音频")
//    @RequestMapping(value = "{id}/audio/{name:.+}", method = RequestMethod.GET)
//    public byte[] getAudio(@ApiParam(name = "id", value = "轨迹id") @PathVariable int id,
//                           @ApiParam(name = "name", value = "轨迹图片名称") @PathVariable String name) {
//        return null;
//    }

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
            int offset = 0;
            String range = req.getHeader("Range");
            if (Objects.isNull(range)) {
                res.setHeader("Content-Disposition", "attachment; filename=" + name);
                res.setContentLength(fileInfo.getSize());
            } else {
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

    @ApiOperation(value = "获取轨迹视频")
    @RequestMapping(value = "download/{id}", method = RequestMethod.GET)
    public void download(@ApiParam(name = "id", value = "轨迹id") @PathVariable int id, HttpServletResponse res) {
        try {
            Track track = trackService.get(id);
            List<FileMeta> fileMetas = hadoops.parsePath(track.getPath());
            List<FileSource> sources = new ArrayList<>(fileMetas.size());
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
//            res.setContentLength(track.getFileSize());
            Ants.zip(sources, res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "轨迹统计")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "month", value = "开始时间,格式:yyyy-MM-dd", dataType = "string", paramType = "query", required = true)
    )
    @RequestMapping(value = "stat/month", method = RequestMethod.GET)
    @ResponseBody
    public HttpRes<List<TrackStat>> statByMonth(@RequestParam ShortDate month) {
        return HttpRes.success(trackStatService.getByMonth(month));
    }

    @ApiOperation(value = "轨迹统计")
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
