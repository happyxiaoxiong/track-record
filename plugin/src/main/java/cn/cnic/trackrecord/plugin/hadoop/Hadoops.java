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

    public FileMeta parsePath(String path, String fileName) throws IOException {
        List<FileMeta> fileMetas = parsePath(path);
        for (FileMeta fileMeta : fileMetas) {
            if (fileMeta.getName().equals(fileName)) {
                return fileMeta;
            }
        }
        return null;
    }

    public List<FileMeta> parsePath(String path) throws IOException {
        return objectMapper.readValue(path, new TypeReference<List<FileMeta>>(){});
    }

    public String appendKmzFiles(String id, File file, boolean isDelete) throws IOException {
        return objectMapper.writeValueAsString(hadoopBean.appendKmzFile(id, file, isDelete));
    }

    public String appendKmzFiles(String id, File file) throws IOException {
        return appendKmzFiles(id, file, true);
    }

    public void readToOutputStream(String id, FileMeta fileInfo, OutputStream out) throws IOException {
        hadoopBean.readToOutputStream(id, fileInfo, 0, fileInfo.getSize(), out);
    }

    public void readToOutputStream(String id, FileMeta fileInfo, int offset, int len, OutputStream out) throws IOException {
        hadoopBean.readToOutputStream(id, fileInfo, offset, len, out);
    }

    public void readToOutputStream(String id, FileMeta fileInfo, int offset, OutputStream out) throws IOException {
        hadoopBean.readToOutputStream(id, fileInfo, offset, fileInfo.getSize() - offset, out);
    }

    public void readToCallBack(String id, FileMeta fileInfo, CallBack callBack) throws IOException {
        hadoopBean.readToCallBack(id, fileInfo, callBack);
    }

}
