package cn.cnic.trackrecord.core.track;

import cn.cnic.trackrecord.data.entity.Track;
import cn.cnic.trackrecord.plugin.lucene.LuceneFormatter;
import org.apache.lucene.document.*;

public class TrackLuceneFormatter implements LuceneFormatter<Track> {
    @Override
    public Document to(Track track) {
        Document doc = new Document();
        doc.add(new StoredField("id", track.getId()));
        doc.add(new TextField("name", track.getName(), Field.Store.NO));
        doc.add(new IntPoint("startTime", track.getStartTime().getValue()));
        doc.add(new IntPoint("endTime", track.getEndTime().getValue()));
        doc.add(new IntPoint("endTime", track.getEndTime().getValue()));
        doc.add(new TextField("keySitesList", track.getKeySitesList(), Field.Store.NO));
        doc.add(new TextField("annotation", track.getAnnotation(), Field.Store.NO));
        doc.add(new IntPoint("uploadTime", track.getUploadTime().getValue()));

//        SpatialContext ctx;
//        ctx.makeLineString()
//        doc.add(new IndexableField() {
//        });
        return doc;
    }
}
