package cn.cnic.trackrecord.plugin.lucene;

import org.apache.lucene.document.Document;

/**
 * lucene的document对象和java对象的转换器
 * @param <S>
 */
public interface LuceneFormatter<S> {
    Document to(S source);

    S from(Document doc);
}
