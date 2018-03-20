package cn.cnic.trackrecord.plugin.hadoop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FileMeta {
    private long offset;// 在HDFS文件中的起始偏移位置
    private int size;// 文件大小
    private String name;// 原始文件名
    private String storeName;// HDFS存储文件名
    private FileMeta thumb;// 压缩图片或者转码后的文件信息
}
