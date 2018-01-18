package cn.cnic.trackrecord.plugin.hadoop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
public class Hadoops {

    @Autowired
    private HadoopBean hadoopBean;

    @Autowired
    private ObjectMapper objectMapper;


    public Map<String, Long> pathToMap(String path) throws IOException {
        return objectMapper.readValue(path, new TypeReference<Map<String, Long>>(){});
    }

    public String writeFile(File file, boolean isDelete) throws IOException {
        return objectMapper.writeValueAsString(hadoopBean.write(file, isDelete));
    }

    /**
     * 默认删除压缩文件
     * @param file
     * @return
     * @throws IOException
     */
    public String writeFile(File file) throws IOException {
        return writeFile(file, true);
    }

    public InputStream readAsInputStream(long offset) throws IOException {
       return hadoopBean.readAsInputStream(offset);
    }

    public File readAsFile(long offset) throws IOException {
        return hadoopBean.readAsFile(offset);
    }

    public Bytes readAsBytes(long offset) throws IOException {
        return hadoopBean.readAsBytes(offset);
    }
}
