package cn.cnic.trackrecord.common.xml;

import org.xml.sax.helpers.DefaultHandler;

import java.io.File;

public abstract class SaxHandler<T> extends DefaultHandler {

    private String xmlPath;

    public abstract T getResult();

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public File getXmlFile() {
        return new File(this.xmlPath);
    }
}
