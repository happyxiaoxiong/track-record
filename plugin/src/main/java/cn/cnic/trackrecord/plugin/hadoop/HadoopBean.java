package cn.cnic.trackrecord.plugin.hadoop;

import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Images;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.plugin.ffmpeg.FfmpegBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
class HadoopBean {
    @Autowired
    private HadoopProperties properties;
    @Autowired
    private FfmpegBean ffmpegBean;
    private FileSystem writeFs;
    private Configuration configuration;
    private int maxFileSize = 0;
    private final String origin = "ori";
    private final String thumb = "thu";

    private final String[] imgTypes = { "jpg", "png", "jpeg", "bmp", "jpe", "gif", "ico" };
    private final String[] videoTypes = { "mp4", "avi", "rmvb", "mpeg", "mov", "mkv", "vob", "flv", "rm",
            "asf", "f4v", "m4v", "3gp", "ts", "divx", "mpg", "mpe", "wmv", "mts"};

    @PostConstruct
    public void init() {
        System.setProperty("HADOOP_USER_NAME", properties.getUser());
        //FIXED java.io.FileNotFoundException: HADOOP_HOME and hadoop.home.dir are unset.
        System.setProperty("hadoop.home.dir", properties.getHomeDir());

        configuration = new Configuration();
        configuration.set("fs.defaultFS", properties.getUri());

        //FIXED dfs.client.block.write.replace-datanode-on-failure.policy DEFAULT error
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        try {
            writeFs = FileSystem.get(configuration);
            createDirectory(new Path(properties.getStorePath()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        maxFileSize = (int) (properties.getBlockSize() * 0.75);
    }

    private void createDirectory(Path path) throws IOException {
        if (!writeFs.exists(path)) {
            writeFs.mkdirs(path);
        }
    }

    synchronized List<FileInfo> write(String id, File file, boolean isDelete) throws IOException {
        writeFs = FileSystem.get(configuration);
        Path parent = getPath(properties.getStorePath(), id);
        createDirectory(parent);
        writeFs.createNewFile(new Path(parent, origin));
        writeFs.createNewFile(new Path(parent, thumb));

        List<FileInfo> fileInfos = write(parent, file);

        if (isDelete) {
            Files.delete(file);
        }
        return fileInfos;
    }

    private List<FileInfo> write(Path parent, File file) throws IOException {
        List<FileInfo> fileInfos = new LinkedList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (Objects.nonNull(files)) {
                for (File tmpFile : files) {
                    if (tmpFile.isFile()) {
                        fileInfos.add(append(parent, tmpFile));
                    } else {
                        fileInfos.addAll(write(parent, tmpFile));
                    }
                }
            }
        } else {
            fileInfos.add(append(parent, file));
        }
        return fileInfos;
    }

    private FileInfo append(Path parent, File file) throws IOException {
        FileInfo fileInfo = new FileInfo();
        if (file.length() > maxFileSize) {//超过阈值
            writeFs.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(parent, file.getName()));
        } else {
            fileInfo = appendCallback(new Path(parent, origin), new Callback() {
                @Override
                public void call(OutputStream out) throws IOException {
                    IOUtils.copyBytes(new FileInputStream(file), out, 4096, true);
                }
            });
        }
        if (isImage(file)) {// 是图片进行压缩
            fileInfo.setThumb(appendThumbnail(parent, file));
        } else if (isVideo(file)) {// 是视频转码成h264
            fileInfo.setThumb(appendH264(parent, file));
        } else if (isAudio(file)) {

        }
        fileInfo.setName(file.getName());
        return fileInfo;
    }

    private FileInfo appendH264(Path parent, File file) throws IOException {
        // 需要加上 .mp4后缀，否则会出错
        String destPath = Files.getPathString(properties.getLocalTmpDir(), UUID.randomUUID().toString() + ".mp4");
        if (ffmpegBean.encodeH264(file.getAbsolutePath(), destPath)) {
            FileInfo fileInfo = appendCallback(new Path(parent, thumb), new Callback() {
                @Override
                public void call(OutputStream out) throws IOException {
                    IOUtils.copyBytes(new FileInputStream(destPath), out, 4096, true);
                }
            });
            Files.delete(destPath);
            return fileInfo;
        }
        return null;
    }

    private FileInfo appendThumbnail(Path parent, final File file) throws IOException {
        return appendCallback(new Path(parent, thumb), new Callback() {
            @Override
            public void call(OutputStream out) throws IOException {
                Images.createThumbnail(file, out);
            }
        });
    }

    private FileInfo appendCallback(Path path, Callback callback) throws IOException {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setOffset(writeFs.getFileStatus(path).getLen());//记录偏移位置
        OutputStream out = writeFs.append(path);
        callback.call(out);
        fileInfo.setSize((int) (writeFs.getFileStatus(path).getLen() - fileInfo.getOffset()));
        return fileInfo;
    }

    private boolean isImage(File file) {
        String lowercaseName = file.getName().toLowerCase();
        for (String imgType : imgTypes) {
            if (lowercaseName.endsWith(imgType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVideo(File file) {
        String lowercaseName = file.getName().toLowerCase();
        for (String videoType : videoTypes) {
            if (lowercaseName.endsWith(videoType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAudio(File file) {
        String lowercaseName = file.getName().toLowerCase();
        for (String videoType : videoTypes) {
            if (lowercaseName.endsWith(videoType)) {
                return true;
            }
        }
        return false;
    }

    void readToOutputStream(String id, FileInfo fileInfo, int offset, long len, OutputStream out) throws IOException {
        InputStream in = getInputStream(id, fileInfo, offset);
        IOUtils.copyBytes(in, out, len, false);
        out.flush();
        out.close();
        in.close();
    }

    void readCallBack(String id, FileInfo fileInfo, CallBack callBack) throws IOException {
        InputStream in = getInputStream(id, fileInfo, 0);
        callBack.call(in);
        in.close();
    }

    private InputStream getInputStream(String id, FileInfo fileInfo, int offset) throws IOException {
        long desired = fileInfo.getOffset() + offset;
        String fileName = origin;
        FileInfo thumbnailFileInfo = fileInfo.getThumb();
        if (!Objects.isNull(thumbnailFileInfo)) {
            fileName = thumb;
            desired = thumbnailFileInfo.getOffset() + offset;
        } else if (fileInfo.getSize() > maxFileSize) {//超过阈值
            fileName = fileInfo.getName();
        }
        FSDataInputStream in = FileSystem.get(configuration).open(getPath(properties.getStorePath(), id, fileName));
        in.seek(desired);
        return in;
    }

    private Path getPath(String first, String... more) {
        return new Path(Files.getPathString(first, more));
    }

    private static interface Callback {
        void call(OutputStream out) throws IOException;
    }
}
