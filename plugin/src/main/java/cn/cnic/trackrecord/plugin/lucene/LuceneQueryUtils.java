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


public abstract class LuceneQueryUtils {
    private static Analyzer analyzer = new IKAnalyzer();

    /**
     * 构造多关键字查询
     * @param keyword 关键词
     * @param fields 需要查询的关键字
     * @param occurs 多个关键字之间的连接条件运算符
     * @return
     * @throws ParseException
     */
    public static Query multiFieldQuery(String keyword, String[] fields, BooleanClause.Occur[] occurs) throws ParseException {
        return MultiFieldQueryParser.parse(keyword, fields, occurs, analyzer);
    }

    /**
     * 构造多关键字查询，关键字之间使用或连接
     * @param keyword 关键词
     * @param fields 需要查询的关键字
     * @return
     * @throws ParseException
     */
    public static Query multiFieldQuery(String keyword, String[] fields) throws ParseException {
        BooleanClause.Occur[] occurs = new BooleanClause.Occur[fields.length];
        for (int i = 0; i < occurs.length; ++i) {
            occurs[i] = BooleanClause.Occur.SHOULD;
        }
        return multiFieldQuery(keyword, fields, occurs);
    }

    /**
     * 构造空间查询
     * @param lng 经度
     * @param lat 纬度
     * @param distance 距离
     * @return
     */
    public static Query spatialCircleQuery(double lng, double lat, double distance) {
        return SpatialUtils.getSpatialStrategy().makeQuery(new SpatialArgs(SpatialOperation.Intersects,
                SpatialUtils.getShapeFactory().circle(lng, lat,
                        DistanceUtils.dist2Degrees(distance, DistanceUtils.EARTH_MEAN_RADIUS_KM))));
    }

    /**
     * 获取分词器
     * @return
     */
    public static Analyzer getAnalyzer() {
        return analyzer;
    }
}
