package cn.cnic.trackrecord.common.ant;

import cn.cnic.trackrecord.common.util.Files;
import cn.cnic.trackrecord.common.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;

/**
 * Ants压缩工具类  @Slf4j是日志注解
 */
@Slf4j
public abstract class Ants {

    /**
     * kmz文件解压
     * @param filePath 文件路径
     * @param unZipPath 解压路径
     * @param isDelete 解压完成是否删除
     * @throws IOException
     */
    public static void unzip(String filePath, String unZipPath, boolean isDelete) throws IOException {
        File unzipFile = new File(filePath);
        if (!unzipFile.exists() || unzipFile.length() <= 0) {
            log.debug("file: {} is not exist", filePath);
            return;
        }
        ZipFile zipFile = new ZipFile(unzipFile, "UTF-8"); //统一utf-8编码
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();

            File file = Paths.get(unZipPath, zipEntry.getName()).toFile();
            if (zipEntry.isDirectory()) { //目录
                Files.createDirectory(file);
            } else {
                FileUtils.copyInputStreamToFile(zipFile.getInputStream(zipEntry), Paths.get(unZipPath, zipEntry.getName()).toFile());
            }
        }
        zipFile.close();
        if (isDelete) {
            Files.delete(unzipFile);
        }
    }

    /**
     * 直接对存储在hdfs中的轨迹文件进行压缩,避免中间文件的产生
     * 一个kmz轨迹包含多个文件，文件是存储在hdfs中的，这里对hdfs输出流进行压缩，然后输出到os输出流中。hdfs文件输出流使用FileSource类进行封装，
     * @param sources 代表一个kmz轨迹中的所有hdfs文件输出流
     * @param os 接受压缩流的输出流
     * @throws IOException
     */
    public static void zip(List<FileSource> sources, OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.setEncoding("UTF-8");
        for (FileSource source : sources) {
            zos.putNextEntry(new ZipEntry(source.getPathName()));
            source.getSource().read(zos);
        }
        zos.closeEntry();
        zos.close();
    }

    /**
     * 压缩本地磁盘文件
     * @param source 待压缩文件
     * @param dest 压缩后的文件
     * @throws IOException
     */
    public static void zip(File source, File dest) throws IOException {
        zip(source, new FileOutputStream(dest));
    }

    /**
     * 压缩本地磁盘文件
     * @param source 待压缩文件
     * @param os 输出流
     * @throws IOException
     */
    public static void zip(File source, OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.setEncoding("UTF-8");
        zip(source, "", zos);
        zos.closeEntry();
        zos.close();
    }

    /**
     * 压缩本地磁盘文件
     * @param source 待压缩文件
     * @param basePath 待压缩文件的根目录
     * @param zos 压缩输出流
     * @throws IOException
     */
    private static void zip(File source, String basePath, ZipOutputStream zos) throws IOException {
        String path = Files.getPathString(basePath, source.getName());
        if (source.isFile()) { // 对文件压缩
            zos.putNextEntry(new ZipEntry(path));
            FileUtils.copyFile(source, zos);
        } else { // 对目录压缩
            File[] files = source.listFiles();
            if (Objects.nonNull(files)) {
                for (File file : files) {
                    zip(file, path, zos);
                }
            }
        }
    }
}
