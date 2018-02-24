package cn.cnic.trackrecord.common.ant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileSource {
    private String pathName;
    private Source source;

    public static interface Source {
        void read(OutputStream os) throws IOException;
    }
}
