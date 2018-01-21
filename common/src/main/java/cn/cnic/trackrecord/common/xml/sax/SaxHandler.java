package cn.cnic.trackrecord.common.xml.sax;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

@Slf4j
public abstract class SaxHandler<T> extends DefaultHandler {

    private String firstElement;

    public abstract T getResult();

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }
}
