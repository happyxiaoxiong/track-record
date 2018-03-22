package cn.cnic.trackrecord.core.track.xml;

import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.xml.Stax.StaxHandler;
import cn.cnic.trackrecord.data.entity.Track;
import org.apache.commons.lang3.math.NumberUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class TrackDetailXml extends StaxHandler<Track> {
    private Track track = new Track();

    @Override
    public void parse() throws XMLStreamException {
        while (hasNext()) {
            XMLEvent xmlEvent = nextEvent();
           if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("name")) {
                    track.setName(nextData());
                } else if (startElement.getName().getLocalPart().equals("author")) {
                    track.setUserName(nextData());// author
                } else if (startElement.getName().getLocalPart().equals("authorid")) {
                    track.setUserId(Integer.valueOf(nextData()));// author
                } else if (startElement.getName().getLocalPart().equals("starttime")) {
                    track.setStartTime(LongDate.from(nextData()));
                } else if (startElement.getName().getLocalPart().equals("endtime")) {
                    track.setEndTime(LongDate.from(nextData()));
                } else if (startElement.getName().getLocalPart().equals("length")) {
                    track.setLength(NumberUtils.toDouble(nextData()));
                } else if (startElement.getName().getLocalPart().equals("maxaltitude")) {
                    track.setMaxAltitude(NumberUtils.toDouble(nextData()));
                } else if (startElement.getName().getLocalPart().equals("keysiteslist")) {
                    track.setKeySitesList(nextData());
                } else if (startElement.getName().getLocalPart().equals("annotation")) {
                    track.setAnnotation(nextData());
                }
            }
        }
    }

    @Override
    public Track getResult() {
        return track;
    }
}
