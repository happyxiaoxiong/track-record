package cn.cnic.trackrecord.common.xml.Stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.Charset;

public abstract class Staxs {
    private static XMLInputFactory factory = XMLInputFactory.newInstance();

    /**
     * 解析xml文件
     * @param staxHandler stax解析器
     * @param xmlPath
     * @param <T>
     * @return
     * @throws XMLStreamException
     * @throws FileNotFoundException
     */
    public static  <T> T parse(StaxHandler<T> staxHandler, String xmlPath) throws XMLStreamException, FileNotFoundException {
        return parse(staxHandler, new File(xmlPath));
    }

    public static  <T> T parse(StaxHandler<T> staxHandler, File xmlFile) throws XMLStreamException, FileNotFoundException {
        return parse(staxHandler, new FileInputStream(xmlFile));
    }

    public static  <T> T parse(StaxHandler<T> staxHandler, InputStream inputStream) throws XMLStreamException {
        //指定编码解决'Invalid UTF-8 middle byte 0x7b'
        staxHandler.setXmlEventReader(factory.createXMLEventReader(new InputStreamReader(inputStream, Charset.forName("UTF-8"))));
        staxHandler.parse();
        return staxHandler.getResult();
    }
}
