package cn.cnic.trackrecord.plugin.sax;

import org.xml.sax.helpers.DefaultHandler;


public abstract class SaxHandler<T> extends DefaultHandler {

    public abstract T getResult();
}
