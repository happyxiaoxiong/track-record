package cn.cnic.trackrecord.common.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Files {
    /**
     * 创建目录
     * @param path 路径
     */
    public static void createDirectory(String path) {
        createDirectory(new File(path));
    }

    /**
     * 创建目录
     * @param dir
     */
    public static void createDirectory(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 获取kmz解压后文件夹的真实路径，有些kmz文件解压后存在目录嵌套的情况
     * @param path
     * @return
     */
    public static String getRealTrackPath(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            String nextFileName = dir.getName();
            File tmpDir;
            while ((tmpDir = Paths.get(dir.getPath(), nextFileName).toFile()).exists() && tmpDir.isDirectory()) {
                dir = tmpDir;
            }
        }
        return dir.getAbsolutePath();
    }

    /**
     * 删除路径
     * @param filePath
     */
    public static void delete(String filePath) {
        delete(new File(filePath));
    }

    /**
     * 删除文件
     * @param file
     */
    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (Objects.nonNull(files)) {
                for (File tmpFile : files) {
                    delete(tmpFile);
                }
            }
        }
        file.delete();
    }

    /**
     * 拼接路径
     * @param first
     * @param more
     * @return
     */
    public static String getPathString(String first, String... more) {
        return Paths.get(first, more).normalize().toString();
    }

    /**
     * 拼接路径，并转换成File对象
     * @param first
     * @param more
     * @return
     */
    public static File getFile(String first, String... more) {
        return Paths.get(first, more).toFile();
    }

    /**
     * 拼接路径，并转换成Path对象
     * @param first
     * @param more
     * @return
     */
    public static Path getPath(String first, String... more) {
        return Paths.get(first, more).normalize();
    }

    /**
     * 判断路径是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        return new File(path).exists();
    }
}
