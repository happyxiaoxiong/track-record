package cn.cnic.trackrecord.common.xml.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Saxs {
    private static SAXParserFactory factory = SAXParserFactory.newInstance();

    public static  <T> T parse(SaxHandler<T> saxHandler, String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        return parse(saxHandler, new File(xmlPath));
    }

    public static  <T> T parse(SaxHandler<T> saxHandler, File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        return parse(saxHandler, new FileInputStream(xmlFile));
    }

    public static  <T> T parse(SaxHandler<T> saxHandler, InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        factory.newSAXParser().parse(inputStream, saxHandler);
        return saxHandler.getResult();
    }
}
