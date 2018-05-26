package cn.cnic.trackrecord.common.ant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 对hdfs的文件封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSource {
    /**
     * 文件路径
     */
    private String pathName;
    /**
     * 文件来源
     */
    private Source source;

    public interface Source {
        /**
         * 将hdfs文件流读取到os输出流中
         * @param os
         * @throws IOException
         */
        void read(OutputStream os) throws IOException;
    }
}
