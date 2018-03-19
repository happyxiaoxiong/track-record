package cn.cnic.trackrecord.core.track;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.lucene.TrackLucene;
import cn.cnic.trackrecord.plugin.lucene.LuceneFormatter;
import cn.cnic.trackrecord.plugin.lucene.SpatialUtils;
import org.apache.lucene.document.*;

public class TrackLuceneFormatter implements LuceneFormatter<TrackLucene> {

    @Override
    public Document to(TrackLucene trackLucene) {
        Track track = trackLucene.getTrack();
        Document doc = new Document();
        doc.add(new StoredField("id", track.getId()));
        doc.add(new TextField("name", track.getName(), Field.Store.YES));
        doc.add(new TextField("userName", track.getUserName(), Field.Store.YES));

        doc.add(new IntPoint("userId", track.getUserId()));
        doc.add(new StoredField("userId", track.getUserId()));
        //如果要排序使用，必须同时创建同名的StoredField类与NumericDocValuesField类
        doc.add(new IntPoint("startTime", track.getStartTime().getValue()));
        doc.add(new NumericDocValuesField("startTime",track.getStartTime().getValue()));
        doc.add(new StoredField("startTime",track.getStartTime().getValue()));

        doc.add(new StoredField("endTime", track.getEndTime().getValue()));
        doc.add(new StoredField("length", track.getLength()));
        doc.add(new StoredField("maxAltitude", track.getMaxAltitude()));
        doc.add(new TextField("keySitesList", track.getKeySitesList(), Field.Store.YES));
        doc.add(new StoredField("fileSize", track.getFileSize()));
        doc.add(new StoredField("filename", track.getFilename()));
        doc.add(new TextField("annotation", track.getAnnotation(), Field.Store.YES));
        doc.add(new StoredField("uploadUserId", track.getUploadUserId()));
        doc.add(new StoredField("uploadUserName", track.getUploadUserName()));
        doc.add(new StoredField("uploadTime", track.getUploadTime().getValue()));

        for (TrackPoint point : trackLucene.getPoints()) {
            for (Field field : SpatialUtils.createIndexFields(point.getLng(), point.getLat())) {
                doc.add(field);
            }
        }
        return doc;
    }

    @Override
    public TrackLucene from(Document doc) {
        Track track = new Track();
        track.setId(Integer.valueOf(doc.get("id")));
        track.setName(doc.get("name"));
        track.setUserName(doc.get("userName"));
        track.setUserId(Integer.valueOf(doc.get("userId")));
        track.setStartTime(new LongDate(Integer.valueOf(doc.get("startTime"))));
        track.setEndTime(new LongDate(Integer.valueOf(doc.get("endTime"))));
        track.setLength(Double.valueOf(doc.get("length")));
        track.setMaxAltitude(Double.valueOf(doc.get("maxAltitude")));
        track.setKeySitesList(doc.get("keySitesList"));
        track.setFileSize(Integer.valueOf(doc.get("fileSize")));
        track.setFilename(doc.get("filename"));
        track.setAnnotation(doc.get("annotation"));
        track.setUploadUserId(Integer.valueOf(doc.get("uploadUserId")));
        track.setUploadUserName(doc.get("uploadUserName"));
        track.setUploadTime(new LongDate(Integer.valueOf(doc.get("uploadTime"))));
        return new TrackLucene(track, null);
    }
}
