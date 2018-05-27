package cn.cnic.trackrecord.plugin.hadoop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class Hadoops {

    @Autowired
    private HadoopBean hadoopBean;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 解析path，并获取fileName的元信息
     * @param path json形式
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public FileMeta parsePath(String path, String fileName) throws IOException {
        List<FileMeta> fileMetas = parsePath(path);
        for (FileMeta fileMeta : fileMetas) {
            if (fileMeta.getName().equals(fileName)) {
                return fileMeta;
            }
        }
        return null;
    }

    /**
     * 解析path
     * @param path json形式
     * @return
     * @throws IOException
     */
    public List<FileMeta> parsePath(String path) throws IOException {
        return objectMapper.readValue(path, new TypeReference<List<FileMeta>>(){});
    }

    /**
     * 追加kmz文件，并以json字符串的形式返回元信息
     * @param id 用户id
     * @param file 文件
     * @param isDelete 成功后是否删除
     * @return
     * @throws IOException
     */
    public String appendKmzFiles(String id, File file, boolean isDelete) throws IOException {
        return objectMapper.writeValueAsString(hadoopBean.appendKmzFile(id, file, isDelete));
    }

    /**
     * 追加kmz文件，并以json字符串的形式返回元信息,成功后删除源文件
     * @param id 用户id
     * @param file 文件
     * @return
     * @throws IOException
     */
    public String appendKmzFiles(String id, File file) throws IOException {
        return appendKmzFiles(id, file, true);
    }

    /**
     * 根据用户id和文件元信息读取hdfs文件流到out输出流中，读取完之后默认关闭流
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param out 用来接受hdfs文件流的输出流
     * @throws IOException
     */
    public void readToOutputStream(String id, FileMeta fileInfo, OutputStream out) throws IOException {
        //hadoopBean.readToOutputStream(id, fileInfo, 0, fileInfo.getSize(), out);
        readToOutputStream(id, fileInfo, 0, fileInfo.getSize(), out);
    }

    /**
     *
     * 根据用户id和文件元信息读取hdfs文件流到out输出流中，
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param out 用来接受hdfs文件流的输出流
     * @param close 读取完之后是否关闭流
     * @throws IOException
     */
    public void readToOutputStream(String id, FileMeta fileInfo, OutputStream out, boolean close) throws IOException {
        //hadoopBean.readToOutputStream(id, fileInfo, 0, fileInfo.getSize(), out);
        readToOutputStream(id, fileInfo, 0, fileInfo.getSize(), out, close);
    }

    /**
     * 根据用户id和文件元信息读取hdfs文件流到out输出流中
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param offset 文件的偏移位置
     * @param out 用来接受hdfs文件流的输出流
     * @throws IOException
     */
    public void readToOutputStream(String id, FileMeta fileInfo, int offset, OutputStream out) throws IOException {
        //hadoopBean.readToOutputStream(id, fileInfo, offset, fileInfo.getSize() - offset, out);
        readToOutputStream(id, fileInfo, offset, fileInfo.getSize() - offset, out);
    }

    /**
     * 根据用户id和文件元信息读取hdfs文件流到out输出流中
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param offset 文件的偏移位置
     * @param out 用来接受hdfs文件流的输出流
     * @param close 读取完之后是否关闭流
     * @throws IOException
     */
    public void readToOutputStream(String id, FileMeta fileInfo, int offset, OutputStream out, boolean close) throws IOException {
        //hadoopBean.readToOutputStream(id, fileInfo, offset, fileInfo.getSize() - offset, out);
        readToOutputStream(id, fileInfo, offset, fileInfo.getSize() - offset, out, close);
    }

    /**
     * 根据用户id和文件元信息读取hdfs文件流到out输出流中
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param offset 文件的偏移位置
     * @param len 读取长度
     * @param out 用来接受hdfs文件流的输出流
     * @throws IOException
     */
    public void readToOutputStream(String id, FileMeta fileInfo, int offset, int len, OutputStream out) throws IOException {
        readToOutputStream(id, fileInfo, offset, len, out, true);
    }

    /**
     * 根据用户id和文件元信息读取hdfs文件流到out输出流中
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param offset 文件的偏移位置
     * @param len 读取长度
     * @param out 用来接受hdfs文件流的输出流
     * @param close 读取完之后是否关闭流
     * @throws IOException
     */
    public void readToOutputStream(String id, FileMeta fileInfo, int offset, int len, OutputStream out, boolean close) throws IOException {
        hadoopBean.readToOutputStream(id, fileInfo, offset, len, out, close);
    }

    /**
     * 根据用户id和文件元信息读取hdfs文件流到callback函数中
     * @param id 用户id
     * @param fileInfo 文件元信息
     * @param callBack 回调函数，用于自定义处理hdfs文件流
     * @throws IOException
     */
    public void readToCallBack(String id, FileMeta fileInfo, CallBack callBack) throws IOException {
        hadoopBean.readToCallBack(id, fileInfo, callBack);
    }

}
