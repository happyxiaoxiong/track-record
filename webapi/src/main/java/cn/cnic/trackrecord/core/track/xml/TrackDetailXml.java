package cn.cnic.trackrecord.core.track.xml;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.plugin.sax.SaxHandler;
import cn.cnic.trackrecord.data.entity.Track;
import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.SAXException;

public class TrackDetailXml extends SaxHandler<Track> {
    private Track track = new Track();
    private StringBuilder builder = new StringBuilder();

    public void endElement(String uri, String localName, String qName) throws SAXException {
        String text = builder.toString().trim();
        builder.delete(0, builder.length());
        if ("name".equals(qName)) {
            track.setName(text);
        } else if ("author".equals(qName)) {
            //todo
//            track.setUserId(text);
        } else if ("starttime".equals(qName)) {
            track.setStartTime(LongDate.from(text));
        } else if ("endtime".equals(qName)) {
            track.setEndTime(LongDate.from(text));
        } else if ("length".equals(qName)) {
            track.setLength(NumberUtils.toDouble(text));
        } else if ("maxaltitude".equals(qName)) {
            track.setMaxAltitude(NumberUtils.toDouble(text));
        } else if ("keysiteslist".equals(qName)) {
            track.setKeySitesList(text);
        } else if ("annotation".equals(qName)) {
            track.setAnnotation(text);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        builder.append(ch, start, length);
    }

    @Override
    public Track getResult() {
        return track;
    }
}
