package cn.cnic.trackrecord.plugin.lucene;

import org.apache.lucene.document.Document;

public interface LuceneFormatter<S> {
    Document to(S source);

    S from(Document doc);
}
