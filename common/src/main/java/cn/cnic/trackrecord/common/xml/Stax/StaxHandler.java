package cn.cnic.trackrecord.common.xml.Stax;

import cn.cnic.trackrecord.common.util.Objects;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * stax方式解析xml接口
 * @param <T>
 */
public abstract class StaxHandler<T> {
    private XMLEventReader xmlEventReader;

    private boolean stop = false;
    private String firstElement = null;

    protected String nextData() throws XMLStreamException {
        XMLEvent event = xmlEventReader.nextEvent();
        if (event.isCharacters()) {
            return event.asCharacters().getData();
        } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(firstElement)) {
            stop = true;
        }
        return "";
    }

    protected boolean hasNext() {
        return !stop && xmlEventReader.hasNext();
    }

    protected XMLEvent nextEvent() throws XMLStreamException {
        XMLEvent event = xmlEventReader.nextEvent();
        if (event.isStartElement() && Objects.isNull(firstElement)) {
            firstElement = event.asStartElement().getName().getLocalPart();
        } else if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(firstElement)) {
            stop = true;
        }
        return event;
    }

    public void setXmlEventReader(XMLEventReader xmlEventReader) {
        this.xmlEventReader = xmlEventReader;
    }

    abstract public void parse() throws XMLStreamException;

    abstract public T getResult();
}
