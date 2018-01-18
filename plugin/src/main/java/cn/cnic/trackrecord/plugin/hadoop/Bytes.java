package cn.cnic.trackrecord.plugin.hadoop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bytes {
    private byte[] bytes;
    private int size;
}
