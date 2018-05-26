package cn.cnic.trackrecord.common.http.plupupload;

import java.io.File;

/**
 * 上传完成回调接口
 * @param <T>
 */
public interface PluploadCallback<T> {
    T callback(File file);
}
