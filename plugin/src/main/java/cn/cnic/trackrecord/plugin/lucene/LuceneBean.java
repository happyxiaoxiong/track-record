package cn.cnic.trackrecord.plugin.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hdfs.server.federation.store.records.QueryResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private Analyzer analyzer = LuceneQueryUtils.getAnalyzer();
    @PostConstruct
    public void init() {
        try {
            directory = FSDirectory.open(Paths.get(properties.getIndexPath()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setMaxBufferedDocs(100);
    }

    public <T> void add(LuceneFormatter<T> formatter, T source) throws IOException {
        IndexWriter writer = new IndexWriter(directory, config);
        writer.addDocument(formatter.to(source));
        writer.close();
    }

    public ScoreDoc[] search(Query query) throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
        TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
        return topDocs.scoreDocs;
    }
}
