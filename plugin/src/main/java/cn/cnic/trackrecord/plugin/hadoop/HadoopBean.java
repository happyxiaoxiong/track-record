package cn.cnic.trackrecord.plugin.hadoop;

import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Medias;
import cn.cnic.trackrecord.common.util.Objects;
import cn.cnic.trackrecord.plugin.ffmpeg.FfmpegBean;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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

    /**
     * 在hdfs上创建目录
     * @param path
     * @throws IOException
     */
    private void createDirs(Path path) throws IOException {
        if (!writeFs.exists(path)) {
            writeFs.mkdirs(path);
        }
    }

    /**
     * 追加kmz文件
     * @param id 用户ID
     * @param file kmz文件
     * @param isDelete 成功后是否删除源文件
     * @return
     * @throws IOException
     */
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

    /**
     * 追加kmz文件,并返回文件在hdfs上的元信息
     * @param parent 父目录
     * @param file kmz文件
     * @return
     * @throws IOException
     */
    private List<FileMeta> appendKmzFile(Path parent, File file) throws IOException {
        List<FileMeta> fileMetas = new LinkedList<>();
        if (file.isDirectory()) { //追加文件夹
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
        } else { // 追加文件
            fileMetas.add(appendFile(parent, file));
        }
        return fileMetas;
    }

    private FileMeta appendFile(Path parent, File file) throws IOException {
        FileMeta meta = new FileMeta();
        String fileName = file.getName();
        if (file.length() > maxFileSize) { //超过阈值，不追加，单独存放在该用户目录下
            writeFs.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(parent, fileName));
            meta.setStoreName(fileName);
        } else { //小文件追加在该用户的ori文件上
            meta = appendCallback(new Path(parent, properties.getOriginFileName()), out ->
                    IOUtils.copyBytes(new FileInputStream(file), out, 4096, true));
        }
        if (Medias.isImage(fileName)) { // 是图片进行压缩
            meta.setThumb(thumbnail(parent, file));
        } else if (Medias.isVideo(fileName)) { // 是视频转码成mp4 h264
            meta.setThumb(video(parent, file));
        } else if (Medias.isAudio(fileName)) { // 是音频转码成,p3
            meta.setThumb(audio(parent, file));
        }
        meta.setName(fileName);
        log.debug("append file: {}", meta);
        return meta;
    }

    private FileMeta video(Path parent, File file) throws IOException {
        // 需要加上 .mp4后缀，否则会出错
        // 将转码后的文件暂时存放在本地的临时目录上
        String destPath = Files.getPathString(properties.getLocalTmpDir(), UUID.randomUUID().toString() + ".mp4");
        if (ffmpegBean.encodeVideo(file.getAbsolutePath(), destPath)) {
            // 转码成功后将转码后的文件追加到hdfs上的该用户下的thu文件上
            FileMeta fileMeta = appendCallback(new Path(parent, properties.getThumbFileName()), out ->
                    IOUtils.copyBytes(new FileInputStream(destPath), out, 4096, true));
            Files.delete(destPath);
            return fileMeta;
        }
        return null;
    }

    private FileMeta audio(Path parent, File file) throws IOException {
        String destPath = Files.getPathString(properties.getLocalTmpDir(), UUID.randomUUID().toString() + ".mp3");
        if (ffmpegBean.encodeAudio(file.getAbsolutePath(), destPath)) {
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
        });
    }

    /**
     * 以回调的方式进行追加,并返回元信息
     * @param path hdfs的文件路径
     * @param callback
     * @return
     * @throws IOException
     */
    private FileMeta appendCallback(Path path, Callback callback) throws IOException {
        FileMeta fileMeta = new FileMeta();
        fileMeta.setOffset(writeFs.getFileStatus(path).getLen()); //记录偏移位置
        OutputStream out = writeFs.append(path);
        //获取path路径在hdfs的输出流，作为参数传递给callback函数中，这样在callback中就可以向hdfs的path上追加文件
        callback.call(out);
        try {
            //must close, fix error: is already the current lease holder
            out.close();
        } catch (Exception ignored) {
        }
        fileMeta.setSize((int) (writeFs.getFileStatus(path).getLen() - fileMeta.getOffset()));
        fileMeta.setStoreName(path.getName());
        return fileMeta;
    }

    /**
     * 读取hdfs的文件流到out输出流中
     * @param id 用户id
     * @param fileMeta 文件元信息
     * @param offset 偏移位置
     * @param len 读取长度
     * @param out 输出流
     * @param close 是否关闭流
     * @throws IOException
     */
    void readToOutputStream(String id, FileMeta fileMeta, int offset, long len, OutputStream out, boolean close) throws IOException {
        InputStream in = readAsInputStream(id, fileMeta, offset);
        IOUtils.copyBytes(in, out, len, close);
        in.close();
    }

    /**
     * 读取hdfs的文件流到CallBack中
     * @param id 用户id
     * @param fileMeta 文件元信息
     * @param callBack 回调函数
     * @throws IOException
     */
    void readToCallBack(String id, FileMeta fileMeta, CallBack callBack) throws IOException {
        InputStream in = readAsInputStream(id, fileMeta, 0);
        callBack.call(in);
        in.close();
    }

    /**
     * 读取hdfs的文件流
     * @param id 用户id
     * @param fileMeta 文件元信息
     * @param offset 偏移位置
     * @return
     * @throws IOException
     */
    private InputStream readAsInputStream(String id, FileMeta fileMeta, long offset) throws IOException {
        FSDataInputStream in = FileSystem.get(configuration).open(getPath(properties.getStorePath(), id, fileMeta.getStoreName()));
        in.seek(fileMeta.getOffset() + offset);
        return in;
    }

    /**
     * hdfs路径拼接
     * @param first
     * @param more
     * @return
     */
    private Path getPath(String first, String... more) {
        return new Path(Files.getPathString(first, more));
    }

    /**
     * 回调接口，内部使用
     */
    private interface Callback {
        void call(OutputStream out) throws IOException;
    }
}
