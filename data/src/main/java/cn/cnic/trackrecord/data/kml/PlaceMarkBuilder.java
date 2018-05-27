package cn.cnic.trackrecord.data.kml;

import cn.cnic.trackrecord.data.entity.TrackPoint;

import java.util.List;

public class PlaceMarkBuilder {
    /**
     * 名称
     */
    private String name;

    /**
     * 关键点的描述
     */
    private List<String> desc;

    /**
     * 关键点的坐标
     */
    private TrackPoint point;

    /**
     * 轨迹的坐标
     */
    private List<TrackPoint> route;

    /**
     * 轨迹的样式
     */
    private RouteStyle routeStyle;

    /**
     * 标注类型
     */
    private PlaceMarkType type;

    public void name(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void desc(List<String> desc) {
        this.desc = desc;
    }

    public List<String> desc() {
        return desc;
    }

    public void point(TrackPoint point) {
        this.point = point;
    }

    public TrackPoint point() {
        return point;
    }

    public void  route(List<TrackPoint> route) {
        this.route = route;
    }

    public void routeStyle(RouteStyle routeStyle) {
        this.routeStyle = routeStyle;
    }

    public RouteStyle routeStyle() {
        return routeStyle;
    }

    public void placeMarkType(PlaceMarkType type) {
        this.type = type;
    }

    public PlaceMarkType placeMarkType() {
        return type;
    }

    public PlaceMark build() {
        switch (this.type) {
            case KEYPOINT:
                return buildKeyPointPlaceMark();
            case ROUTE:
                return buildRoutePlaceMark();
        }
        return null;
    }

    /**
     * 构建关键点标注
     * @return
     */
    private PlaceMark buildKeyPointPlaceMark() {
        KeyPointPlaceMark placeMark = new KeyPointPlaceMark();
        placeMark.setName(this.name);
        placeMark.setDesc(this.desc);
        placeMark.setPoint(this.point);
        return placeMark;
    }

    /**
     * 构建路线标注
     * @return
     */
    private PlaceMark buildRoutePlaceMark() {
        RoutePlaceMark placeMark = new RoutePlaceMark();
        placeMark.setName(this.name);
        placeMark.setPoints(this.route);
        placeMark.setStyle(this.routeStyle);
        return placeMark;
    }
}
