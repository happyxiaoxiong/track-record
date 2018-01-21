package cn.cnic.trackrecord.common.xml.Stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.Charset;

public class Staxs {
    private static XMLInputFactory factory = XMLInputFactory.newInstance();

    public static  <T> T parse(StaxHandler<T> saxHandler, String xmlPath) throws XMLStreamException, FileNotFoundException {
        return parse(saxHandler, new File(xmlPath));
    }

    public static  <T> T parse(StaxHandler<T> saxHandler, File xmlFile) throws XMLStreamException, FileNotFoundException {
        return parse(saxHandler, new FileInputStream(xmlFile));
    }

    public static  <T> T parse(StaxHandler<T> saxHandler, InputStream inputStream) throws XMLStreamException {
        //指定编码解决'Invalid UTF-8 middle byte 0x7b'
        saxHandler.setXmlEventReader(factory.createXMLEventReader(new InputStreamReader(inputStream, Charset.forName("UTF-8"))));
        saxHandler.parse();
        return saxHandler.getResult();
    }
}
