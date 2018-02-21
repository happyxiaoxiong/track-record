package cn.cnic.trackrecord.plugin.lucene;

import org.apache.lucene.document.Field;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;

public class SpatialUtils {
    private static final SpatialContext CTX = SpatialContext.GEO;
    private static final SpatialPrefixTree GRID = new GeohashPrefixTree(CTX, 11);
    private static final SpatialStrategy STRATEGY = new RecursivePrefixTreeStrategy(GRID, "location");

    public static Shape makePoint(double x, double y) {
        return getShapeFactory().pointXY(x, y);
    }

    public static Field[] createIndexFields(Shape shape) {
        return STRATEGY.createIndexableFields(shape);
    }

    public static Field[] createIndexFields(double x, double y) {
        return createIndexFields(makePoint(x, y));
    }

    public static ShapeFactory getShapeFactory() {
        return CTX.getShapeFactory();
    }

    public static SpatialStrategy getSpatialStrategy() {
        return STRATEGY;
    }
}
