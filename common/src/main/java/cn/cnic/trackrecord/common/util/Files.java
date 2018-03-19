package cn.cnic.trackrecord.common.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Files {
    public static void createDirectory(String path) {
        createDirectory(new File(path));
    }

    public static void createDirectory(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

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

    public static void delete(String filePath) {
        delete(new File(filePath));
    }

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

    public static String getPathString(String first, String... more) {
        return Paths.get(first, more).normalize().toString();
    }

    public static File getFile(String first, String... more) {
        return Paths.get(first, more).toFile();
    }

    public static Path getPath(String first, String... more) {
        return Paths.get(first, more).normalize();
    }

    public static boolean exists(String path) {
        return new File(path).exists();
    }
}
