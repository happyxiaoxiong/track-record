package cn.cnic.trackrecord.core.track;

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
        doc.add(new TextField("name", track.getName(), Field.Store.NO));
        doc.add(new TextField("userName", track.getUserName(), Field.Store.NO));
        //如果要排序使用，必须同时创建同名的StoredField类与NumericDocValuesField类
        doc.add(new IntPoint("startTime", track.getStartTime().getValue()));
        doc.add(new NumericDocValuesField("startTime",track.getStartTime().getValue()));
        doc.add(new StoredField("startTime",track.getStartTime().getValue()));

//        doc.add(new IntPoint("endTime", track.getEndTime().getValue()));
        doc.add(new TextField("keySitesList", track.getKeySitesList(), Field.Store.NO));
        doc.add(new TextField("annotation", track.getAnnotation(), Field.Store.NO));
//        doc.add(new IntPoint("uploadTime", track.getUploadTime().getValue()));

        for (TrackPoint point : trackLucene.getPoints()) {
            for (Field field : SpatialUtils.createIndexFields(point.getLatitude(), point.getLongitude())) {
                doc.add(field);
            }
        }
        return doc;
    }
}
