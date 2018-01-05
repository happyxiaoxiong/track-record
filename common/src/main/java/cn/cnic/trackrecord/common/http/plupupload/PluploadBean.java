package cn.cnic.trackrecord.common.http.plupupload;

import cn.cnic.trackrecord.common.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
public class PluploadBean {
    private static final int BUFFER_SIZE = 1024 * 1024;

    public<T> T upload(Plupload plupload, String path, PluploadCallback<T> callback) throws IOException {
        return upload(plupload, path, plupload.getName(), callback);
    }

    public <T> T upload(Plupload plupload, String path, String fileName, PluploadCallback<T> callback) throws IOException {
        log.debug("start receive file: {}, file save path: {}", fileName, path);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) plupload.getRequest();
        MultiValueMap<String, MultipartFile> map = multipartRequest.getMultiFileMap();

        if (Objects.nonNull(map)) {
            for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {
                for (MultipartFile multipartFile : entry.getValue()) {
                    plupload.setFile(multipartFile);
                    File targetFile = Paths.get(path, fileName).toFile();
                    if (processMultiPartFile(plupload, path, targetFile)) {
                        return callback.callback(targetFile);
                    }
                }
            }
        }
        return null;
    }

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

    private void saveMultiPartFile(MultipartFile multipartFile, File targetFile, boolean append) throws IOException {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile, append), BUFFER_SIZE);
        FileCopyUtils.copy(multipartFile.getInputStream(), out);
    }
}
