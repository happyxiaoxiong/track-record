package cn.cnic.trackrecord.plugin.lucene;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.document.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    /**
     * 总数
     */
    private int total;
    /**
     * 结果列表
     */
    private List<Document> docs;
}
