package cn.cnic.trackrecord.plugin.hadoop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FileMeta {
    /**
     * 在HDFS文件中的起始偏移位置
     */
    private long offset;
    /**
     * 文件大小
     */
    private int size;
    /**
     * 原始文件名
     */
    private String name;
    /**
     * HDFS存储文件名
     */
    private String storeName;
    /**
     * 压缩图片或者转码后的文件信息
     */
    private FileMeta thumb;
}
