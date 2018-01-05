package cn.cnic.trackrecord.common.xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class SaxParser {
    private static SAXParserFactory factory = SAXParserFactory.newInstance();

    public static  <T> T parse(SaxHandler<T> saxHandler, String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        saxHandler.setXmlPath(xmlPath);
        return parse(saxHandler);
    }

    public static  <T> T parse(SaxHandler<T> saxHandler) throws ParserConfigurationException, SAXException, IOException {
        factory.newSAXParser().parse(saxHandler.getXmlFile(), saxHandler);
        return saxHandler.getResult();
    }
}
