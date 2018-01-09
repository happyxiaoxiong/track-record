package cn.cnic.trackrecord.plugin.hadoop;

import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class HadoopBean {
    @Autowired
    private HadoopProperties properties;
    private Configuration configuration;
    private Writer writer;

    @PostConstruct
    public void init() {
        System.setProperty("HADOOP_USER_NAME", properties.getUser());
        //FIXED java.io.FileNotFoundException: HADOOP_HOME and hadoop.home.dir are unset.
        System.setProperty("hadoop.home.dir", properties.getHomeDir());

        configuration = new Configuration();
        configuration.set("fs.defaultFS", properties.getUri());

        //FIXED dfs.client.block.write.replace-datanode-on-failure.policy DEFAULT error
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
    }

    public synchronized Map<String, Position> write(File file, boolean isDelete) throws IOException {
        createWriter();

        Map<String, Position> map = write(file);
        closeWriter();

        if (isDelete) {
            Files.delete(file);
        }
        return map;
    }

    private synchronized Map<String, Position> write(File file) throws IOException {
        Map<String, Position> map = new HashMap<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (Objects.nonNull(files)) {
                for (File tmpFile : files) {
                    if (tmpFile.isFile()) {
                        map.put(tmpFile.getName(), append(tmpFile));
                    } else {
                        map.putAll(write(tmpFile));
                    }
                }
            }
        } else {
            map.put(file.getName(), append(file));
        }
        return map;
    }

    private synchronized Position append(File file) throws IOException {
        Position position = new Position();
        position.setStart(writer.getLength());
        writer.append(NullWritable.get(), new BytesWritable(FileUtils.readFileToByteArray(file)));
        writer.sync();
        position.setEnd(writer.getLength());
        return position;
    }

    private void createWriter() throws IOException {
        writer = SequenceFile.createWriter(configuration, SequenceFile.Writer.file(new Path(properties.getFilePath())),
                SequenceFile.Writer.keyClass(NullWritable.class),
                SequenceFile.Writer.valueClass(BytesWritable.class),
//                SequenceFile.Writer.bufferSize(fs.getConf().getInt("io.file.buffer.size",4096)),
//                SequenceFile.Writer.replication(fs.getDefaultReplication()),
                SequenceFile.Writer.appendIfExists(true),
                SequenceFile.Writer.blockSize(properties.getBlockSize()),
                SequenceFile.Writer.compression(SequenceFile.CompressionType.BLOCK, new DefaultCodec()),
                SequenceFile.Writer.progressable(null),
                SequenceFile.Writer.metadata(new SequenceFile.Metadata()));
    }

    private void closeWriter() throws IOException {
        writer.close();
    }
}
