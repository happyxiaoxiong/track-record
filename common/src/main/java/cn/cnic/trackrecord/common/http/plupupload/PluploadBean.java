package cn.cnic.trackrecord.common.http.plupupload;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;

@Slf4j
public class PluploadBean {
    private static final int BUFFER_SIZE = 1024 * 1024;

    /**
     * 文件上传
     * @param plupload 上传参数
     * @param path 上传根路径
     * @param callback 上传完成回调
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> T upload(Plupload plupload, String path, PluploadCallback<T> callback) throws IOException {
        log.debug("start receive file: {}, file save path: {}", plupload, path);
        File targetFile = Paths.get(path, StringUtils.isEmpty(plupload.getName()) ? plupload.getFile().getOriginalFilename() : plupload.getName()).toFile();
        if (processMultiPartFile(plupload, path, targetFile)) {
            return callback.callback(targetFile);
        }
        return null;
    }

    /**
     * 处理上传文件
     * @param plupload 上传参数
     * @param path 上传根路径
     * @param targetFile 目标文件
     * @return
     * @throws IOException
     */
    private boolean processMultiPartFile(Plupload plupload, String path, File targetFile) throws IOException {
        boolean isUploadFinish = true;
        MultipartFile multipartFile = plupload.getFile();
        //当chunks>1则说明当前传的文件为一块碎片，需要合并
        if (plupload.getChunks() > 1) {
            File tempFile = Paths.get(path, multipartFile.getName()).toFile();
            saveMultiPartFile(multipartFile, targetFile, plupload.getChunk() > 0);
            if (plupload.getChunks() == 1 + plupload.getChunk()) {
                tempFile.renameTo(targetFile);
            } else {
                isUploadFinish = false;
            }
        } else {
            multipartFile.transferTo(targetFile);
        }
        return isUploadFinish;
    }

    /**
     * 保存上传文件
     * @param multipartFile 上传文件
     * @param targetFile 目标文件
     * @param append 是否追加（存在分块上传）
     * @throws IOException
     */
    private void saveMultiPartFile(MultipartFile multipartFile, File targetFile, boolean append) throws IOException {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile, append), BUFFER_SIZE);
        FileCopyUtils.copy(multipartFile.getInputStream(), out);
    }
}
