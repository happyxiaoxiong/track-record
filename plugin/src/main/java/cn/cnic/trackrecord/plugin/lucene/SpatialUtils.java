package cn.cnic.trackrecord.plugin.lucene;

import org.apache.lucene.document.Field;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;

public abstract class SpatialUtils {
    private static final SpatialContext CTX = SpatialContext.GEO;
    private static final SpatialPrefixTree GRID = new GeohashPrefixTree(CTX, 11);
    /**
     * 索引和查询模型的策略接口
     */
    private static final SpatialStrategy STRATEGY = new RecursivePrefixTreeStrategy(GRID, "location");

    /**
     * 构造点
     * @param lng 经度
     * @param lat 纬度
     * @return
     */
    public static Shape makePoint(double lng, double lat) {
        return getShapeFactory().pointXY(lng, lat);
    }

    /**
     * 构造索引
     * @param shape
     * @return
     */
    public static Field[] createIndexFields(Shape shape) {
        return STRATEGY.createIndexableFields(shape);
    }

    /**
     * 构造索引
     * @param lng 经度
     * @param lat 纬度
     * @return
     */
    public static Field[] createIndexFields(double lng, double lat) {
        return createIndexFields(makePoint(lng, lat));
    }

    public static ShapeFactory getShapeFactory() {
        return CTX.getShapeFactory();
    }

    public static SpatialStrategy getSpatialStrategy() {
        return STRATEGY;
    }
}
