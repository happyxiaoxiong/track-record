package cn.cnic.trackrecord.common.http.plupupload;

import java.io.File;

public interface PluploadCallback<T> {
    T callback(File file);
}
