package cn.cnic.trackrecord.plugin.hadoop;

import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Medias;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.plugin.ffmpeg.FfmpegBean;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
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
    private int maxFileSize;

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
//        try {
//            writeFs = FileSystem.get(configuration);
//            createDirs(new Path(properties.getStorePath()));
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }

        maxFileSize = (int) (properties.getBlockSize() * 0.75);
    }

    private void createDirs(Path path) throws IOException {
        if (!writeFs.exists(path)) {
            writeFs.mkdirs(path);
        }
    }

    synchronized List<FileMeta> appendKmzFile(String id, File file, boolean isDelete) throws IOException {
        writeFs = FileSystem.get(configuration);
        Path parent = getPath(properties.getStorePath(), id);
        createDirs(parent);
        writeFs.createNewFile(new Path(parent, properties.getOriginFileName()));
        writeFs.createNewFile(new Path(parent, properties.getThumbFileName()));

        List<FileMeta> fileMetas = appendKmzFile(parent, file);

        if (isDelete) {
            Files.delete(file);
        }
        writeFs.close();
        return fileMetas;
    }

    private List<FileMeta> appendKmzFile(Path parent, File file) throws IOException {
        List<FileMeta> fileMetas = new LinkedList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (Objects.nonNull(files)) {
                for (File tmpFile : files) {
                    if (tmpFile.isFile()) {
                        fileMetas.add(appendFile(parent, tmpFile));
                    } else {
                        fileMetas.addAll(appendKmzFile(parent, tmpFile));
                    }
                }
            }
        } else {
            fileMetas.add(appendFile(parent, file));
        }
        return fileMetas;
    }

    private FileMeta appendFile(Path parent, File file) throws IOException {
        FileMeta meta = new FileMeta();
        String fileName = file.getName();
        if (file.length() > maxFileSize) {//超过阈值
            writeFs.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(parent, fileName));
            meta.setStoreName(fileName);
        } else {
            meta = appendCallback(new Path(parent, properties.getOriginFileName()), out ->
                    IOUtils.copyBytes(new FileInputStream(file), out, 4096, true));
        }
        if (Medias.isImage(fileName)) {// 是图片进行压缩
            meta.setThumb(thumbnail(parent, file));
        } else if (Medias.isVideo(fileName)) {// 是视频转码成h264
            meta.setThumb(h264(parent, file));
        } else if (Medias.isAudio(fileName)) {

        }
        meta.setName(fileName);
        log.debug("append file: {}", meta);
        return meta;
    }

    private FileMeta h264(Path parent, File file) throws IOException {
        // 需要加上 .mp4后缀，否则会出错
        String destPath = Files.getPathString(properties.getLocalTmpDir(), UUID.randomUUID().toString() + ".mp4");
        if (ffmpegBean.encodeH264(file.getAbsolutePath(), destPath)) {
            FileMeta fileMeta = appendCallback(new Path(parent, properties.getThumbFileName()), out ->
                    IOUtils.copyBytes(new FileInputStream(destPath), out, 4096, true));
            Files.delete(destPath);
            return fileMeta;
        }
        return null;
    }

    private FileMeta thumbnail(Path parent, final File file) throws IOException {
        return appendCallback(new Path(parent, properties.getThumbFileName()), out -> {
            Thumbnails.of(file).width(1024).outputQuality(0.75).toOutputStream(out);
            //must close, fix error: is already the current lease holder
            out.close();
        });
    }

    private FileMeta appendCallback(Path path, Callback callback) throws IOException {
        FileMeta fileMeta = new FileMeta();
        fileMeta.setOffset(writeFs.getFileStatus(path).getLen());//记录偏移位置
        OutputStream out = writeFs.append(path);
        callback.call(out);
        fileMeta.setSize((int) (writeFs.getFileStatus(path).getLen() - fileMeta.getOffset()));
        fileMeta.setStoreName(path.getName());
        return fileMeta;
    }

    void readToOutputStream(String id, FileMeta fileMeta, int offset, long len, OutputStream out) throws IOException {
        IOUtils.copyBytes(readAsInputStream(id, fileMeta, offset), out, len, true);
    }

    void readToCallBack(String id, FileMeta fileMeta, CallBack callBack) throws IOException {
        InputStream in = readAsInputStream(id, fileMeta, 0);
        callBack.call(in);
        in.close();
    }

    private InputStream readAsInputStream(String id, FileMeta fileMeta, long offset) throws IOException {
        FSDataInputStream in = FileSystem.get(configuration).open(getPath(properties.getStorePath(), id, fileMeta.getStoreName()));
        in.seek(fileMeta.getOffset() + offset);
        return in;
    }

    private Path getPath(String first, String... more) {
        return new Path(Files.getPathString(first, more));
    }

    private interface Callback {
        void call(OutputStream out) throws IOException;
    }
}
