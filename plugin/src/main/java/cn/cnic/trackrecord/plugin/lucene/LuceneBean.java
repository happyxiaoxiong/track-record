package cn.cnic.trackrecord.plugin.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hdfs.server.federation.store.records.QueryResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

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

    public PageResult search(Query query, int n, Sort sort, int pageNum, int pageSize) throws IOException {
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocs = searcher.search(query, n, sort);
        ScoreDoc[] scoreDocs =  topDocs.scoreDocs;
        int begin = (pageNum - 1) * pageSize, end = Math.min(begin + pageSize, scoreDocs.length);
        List<Document> docs = new LinkedList<>();
        for (; begin < end; begin++) {
            docs.add(searcher.doc(scoreDocs[begin].doc));
        }
        reader.close();
        return new PageResult((int) topDocs.totalHits, docs);
    }

    public PageResult search(Query query, Sort sort, int pageNum, int pageSize) throws IOException {
        return search(query, Integer.MAX_VALUE, sort, pageNum, pageSize);
    }

    public PageResult search(Query query, int pageNum, int pageSize) throws IOException {
        return search(query, Integer.MAX_VALUE, new Sort(new SortField(null, SortField.Type.SCORE)), pageNum, pageSize);
    }
}
