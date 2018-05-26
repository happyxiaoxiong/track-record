package cn.cnic.trackrecord.common.xml.sax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Saxs {
    private static SAXParserFactory factory = SAXParserFactory.newInstance();

    /**
     * 解析xml文件
     * @param saxHandler sax解析器
     * @param xmlPath xml路径
     * @param <T> xml解析后的对象
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static  <T> T parse(SaxHandler<T> saxHandler, String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        return parse(saxHandler, new File(xmlPath));
    }

    /**
     * 解析xml文件
     * @param saxHandler sax解析器
     * @param xmlFile xml文件
     * @param <T> xml解析后的对象
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static  <T> T parse(SaxHandler<T> saxHandler, File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        return parse(saxHandler, new FileInputStream(xmlFile));
    }

    /**
     * 解析xml文件
     * @param saxHandler sax解析器
     * @param inputStream xml输入流
     * @param <T> xml解析后的对象
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static  <T> T parse(SaxHandler<T> saxHandler, InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        factory.newSAXParser().parse(inputStream, saxHandler);
        return saxHandler.getResult();
    }
}
