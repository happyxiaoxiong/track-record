package cn.cnic.trackrecord.common.hadoop;

import lombok.Data;

@Data
public class Conf {
    private String uri = "hdfs://localhost:9000/";
    private String filePath = "";
    private String user = "hadoop";
    private int blockSize = 1024 * 1024 * 64;
    private String home;
}
