package cn.cnic.trackrecord.plugin.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Component
public class LuceneBean {
    @Autowired
    private LuceneProperties properties;
    private Directory directory;
    private IndexWriterConfig config;

    @PostConstruct
    public void init() {
        try {
            directory = FSDirectory.open(Paths.get(properties.getIndexPath()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setMaxBufferedDocs(100);
    }

    public <T> void add(LuceneFormatter<T> formatter, T source) throws IOException {
        IndexWriter writer = new IndexWriter(directory, config);
        writer.addDocument(formatter.to(source));
        writer.close();
    }

}
