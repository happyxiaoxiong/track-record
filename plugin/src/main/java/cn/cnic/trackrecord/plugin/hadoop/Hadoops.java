package cn.cnic.trackrecord.plugin.hadoop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class Hadoops {

    @Autowired
    private HadoopBean hadoopBean;

    @Autowired
    private ObjectMapper objectMapper;


    public FileInfo getFileInfoFromPath(String path, String fileName) throws IOException {
        List<FileInfo> fileInfos = objectMapper.readValue(path, new TypeReference<List<FileInfo>>(){});
        for (FileInfo fileInfo : fileInfos) {
            if (fileInfo.getName().equals(fileName)) {
                return fileInfo;
            }
        }
        return null;
    }

    public List<FileInfo> pathToMap(String path) throws IOException {
        return objectMapper.readValue(path, new TypeReference<List<FileInfo>>(){});
    }

    public String writeFile(String id, File file, boolean isDelete) throws IOException {
        return objectMapper.writeValueAsString(hadoopBean.write(id, file, isDelete));
    }

    /**
     * 默认删除压缩文件
     * @param file
     * @return
     * @throws IOException
     */
    public String writeFile(String id, File file) throws IOException {
        return writeFile(id, file, true);
    }

    /**
     * 将hadoop的inputStream输出到OutputStream流中
     * @param id
     * @param fileInfo
     * @param out
     * @throws IOException
     */
    public void readToOutputStream(String id, FileInfo fileInfo, OutputStream out) throws IOException {
        hadoopBean.readToOutputStream(id, fileInfo, 0, fileInfo.getSize(), out);
    }

    /**
     *
     * @param id
     * @param fileInfo
     * @param offset 文件的偏移位置
     * @param len 读取多少个字节数
     * @param out
     * @throws IOException
     */
    public void readToOutputStream(String id, FileInfo fileInfo, int offset, int len,  OutputStream out) throws IOException {
        hadoopBean.readToOutputStream(id, fileInfo, offset, len, out);
    }

    public void readToCallBack(String id, FileInfo fileInfo, CallBack callBack) throws IOException {
        hadoopBean.readCallBack(id, fileInfo, callBack);
    }
}
