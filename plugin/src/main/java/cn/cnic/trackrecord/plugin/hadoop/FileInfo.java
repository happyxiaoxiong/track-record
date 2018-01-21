package cn.cnic.trackrecord.plugin.hadoop;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileInfo {
    private long offset;
    private int size;
    private String name;
    private FileInfo thumb;
}
