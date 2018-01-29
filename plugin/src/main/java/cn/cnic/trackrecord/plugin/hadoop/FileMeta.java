package cn.cnic.trackrecord.plugin.hadoop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FileMeta {
    private long offset;
    private int size;
    private String name;
    private String storeName;
    private FileMeta thumb;
}
