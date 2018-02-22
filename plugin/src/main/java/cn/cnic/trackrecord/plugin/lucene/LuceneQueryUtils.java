package cn.cnic.trackrecord.plugin.lucene;

import cn.cnic.trackrecord.plugin.lucene.ik.IKAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.locationtech.spatial4j.distance.DistanceUtils;


public class LuceneQueryUtils {
    private static Analyzer analyzer = new IKAnalyzer();

    public static Query multiFieldQuery(String keyword, String[] fields, BooleanClause.Occur[] occurs) throws ParseException {
        return MultiFieldQueryParser.parse(keyword, fields, occurs, analyzer);
    }

    public static Query multiFieldQuery(String keyword, String[] fields) throws ParseException {
        BooleanClause.Occur[] occurs = new BooleanClause.Occur[fields.length];
        for (int i = 0; i < occurs.length; ++i) {
            occurs[i] = BooleanClause.Occur.SHOULD;
        }
        return multiFieldQuery(keyword, fields, occurs);
    }

    public static Query spatialCircleQuery(double longitude, double latitude, double distance) {
        return SpatialUtils.getSpatialStrategy().makeQuery(new SpatialArgs(SpatialOperation.Intersects,
                SpatialUtils.getShapeFactory().circle(longitude, latitude,
                        DistanceUtils.dist2Degrees(distance, DistanceUtils.EARTH_MEAN_RADIUS_KM))));
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }
}
