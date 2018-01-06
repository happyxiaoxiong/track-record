package cn.cnic.trackrecord.core.track.xml;

import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.plugin.sax.SaxHandler;
import cn.cnic.trackrecord.data.entity.TrackPoint;
import cn.cnic.trackrecord.data.kml.PlaceMarkBuilder;
import cn.cnic.trackrecord.data.kml.PlaceMarkType;
import cn.cnic.trackrecord.data.kml.RouteRecord;
import cn.cnic.trackrecord.data.kml.RouteStyle;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteRecordXml extends SaxHandler<RouteRecord> {
    private RouteRecordBuilder routeRecordBuilder = new RouteRecordBuilder();
    private StringBuilder builder = new StringBuilder();

    public RouteRecordXml(String xmlPath) {
        this.setXmlPath(xmlPath);
    }

    @Override
    public RouteRecord getResult() {
        return routeRecordBuilder.build();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if (qName.equals("Placemark")) {
            routeRecordBuilder.startPlaceMark();
        } else if (qName.equals("LineString")) {//  LineString/coordinates
            routeRecordBuilder.placeMarkType(PlaceMarkType.ROUTE);
        } else if (qName.equals("Point")) {
            routeRecordBuilder.placeMarkType(PlaceMarkType.KEYPOINT);
        } else if (qName.equals("Style")) {
            routeRecordBuilder.style(attrs.getValue("id"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String text = builder.toString().trim();
        builder.delete(0, builder.length());
        if (qName.equals("Placemark")) {//一个Placemark解析完
            routeRecordBuilder.endPlaceMark();
        } else if (qName.equals("coordinates")) {
            routeRecordBuilder.coordinates(text);
        } else if (qName.equals("name")) {
            routeRecordBuilder.name(text);
        }else if (qName.equals("description")) {
            routeRecordBuilder.desc(text);
        }  else if (qName.equals("color")) {//路线样式颜色
            routeRecordBuilder.routeStyleColor(text);
        } else if (qName.equals("width")) {//路线样式宽度，
            routeRecordBuilder.routeStyleWidth(text);
        } else if (qName.equals("styleUrl")) {//说明:引用的样式要在该路线之前设置才能获取到
            routeRecordBuilder.styleUrl(text.substring(1));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }

    static private class RouteRecordBuilder {
        private static Pattern descPatten = Pattern.compile("(img|video|audio)\\s+src=\"([^\"]+)\"");
        private Map<String, RouteStyle> routeStyleMap = new HashMap<>();
        private RouteStyle routeStyle;
        private List<PlaceMarkBuilder> placeMarkBuilders = new LinkedList<>();
        private PlaceMarkBuilder placeMarkBuilder;

        RouteRecord build() {
            RouteRecord routeRecord = new RouteRecord();
            for (PlaceMarkBuilder placeMarkBuilder : placeMarkBuilders) {
                RouteStyle routeStyle = placeMarkBuilder.routeStyle();
                if (Objects.nonNull(routeStyle)) {
                    placeMarkBuilder.routeStyle(routeStyleMap.get(routeStyle.getId()));
                }
                routeRecord.getPlaceMarks().add(placeMarkBuilder.build());
            }
            return routeRecord;
        }

        void startPlaceMark() {
            placeMarkBuilder = new PlaceMarkBuilder();
        }

        void endPlaceMark() {
            placeMarkBuilders.add(placeMarkBuilder);
            placeMarkBuilder = null;
        }

        void style(String id) {
            if (!StringUtils.isEmpty(id)) {
                routeStyle = new RouteStyle();
                routeStyle.setId(id);
                routeStyleMap.put(id, routeStyle);

                if (Objects.nonNull(placeMarkBuilder)) {
                    placeMarkBuilder.routeStyle(routeStyle);
                }
            }
        }

        void placeMarkType(PlaceMarkType type) {
            placeMarkBuilder.placeMarkType(type);
        }

        void coordinates(String pointsText) {
            switch (placeMarkBuilder.placeMarkType()) {
                case KEYPOINT:
                    placeMarkBuilder.point(trackPoint(pointsText));
                    break;
                case ROUTE:
                    placeMarkBuilder.route(trackPoints(pointsText));
                    break;
            }
        }

        TrackPoint trackPoint(String pointText) {
            TrackPoint point = new TrackPoint();
            StringTokenizer tokenizer = new StringTokenizer(pointText, ",", false);
            if (tokenizer.hasMoreElements())
            {
                point.setLongitude(Double.parseDouble(tokenizer.nextToken()));
                point.setLatitude(Double.parseDouble(tokenizer.nextToken()));
                point.setAltitude(Double.parseDouble(tokenizer.nextToken()));
            }
            return point;
        }

        List<TrackPoint> trackPoints(String pointsText) {
            List<TrackPoint> points = new LinkedList<>();
            StringTokenizer tokenizer = new StringTokenizer(pointsText, " ", false);
            while (tokenizer.hasMoreElements()) {
                points.add(trackPoint(tokenizer.nextToken()));
            }
            return points;
        }

        void name(String name) {
            placeMarkBuilder.name(name);
        }

        void routeStyleColor(String color) {
            routeStyle.setColor(color);
        }

        void routeStyleWidth(String text) {
            routeStyle.setWidth(text);
        }

        void styleUrl(String id) {
            RouteStyle routeStyle = new RouteStyle();
            routeStyle.setId(id);
            placeMarkBuilder.routeStyle(routeStyle);
        }

        void desc(String descText) {
            List<String> desc = new LinkedList<>();
            Matcher matcher = descPatten.matcher(descText);
            while (matcher.find()) {
                //                    if (new File(result).exists()) {//判断下该文件是否存在
                //                        desc.add(result);//虚拟目录+父目录+当前的图片目录
                //                    }
                desc.addAll(Arrays.asList(matcher.group(2).split(",")));
            }
            placeMarkBuilder.desc(desc);
        }
    }
}
