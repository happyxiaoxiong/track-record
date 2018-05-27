package cn.cnic.trackrecord.common.xml.Stax;

import cn.cnic.trackrecord.common.util.Objects;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * stax方式解析xml接口，需要理解stax解析xml的机制
 * @param <T>
 */
public abstract class StaxHandler<T> {
    private XMLEventReader xmlEventReader;

    private boolean stop = false;
    /**
     *  xml根标签，用来记录解析是否结束
     */
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

    /**
     * xml文档是否解析完
     * @return
     */
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

    /**
     * 具体解析需要实现的方法
     * @throws XMLStreamException
     */
    public abstract void parse() throws XMLStreamException;

    /**
     * 获取解析后的结果
     * @return
     */
    public abstract T getResult();
}
