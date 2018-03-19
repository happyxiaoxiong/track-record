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

@Slf4j
public class Ants {

    public static void unzip(String filePath, String unZipPath, boolean isDelete) throws IOException {
        File unzipFile = new File(filePath);
        if (!unzipFile.exists() || unzipFile.length() <= 0) {
            log.debug("file: {} is not exist", filePath);
            return;
        }
        ZipFile zipFile = new ZipFile(unzipFile, "UTF-8");//统一utf-8编码
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();

            File file = Paths.get(unZipPath, zipEntry.getName()).toFile();
            if (zipEntry.isDirectory()) {
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

    public static void zip(File source, File dest) throws IOException {
        zip(source, new FileOutputStream(dest));
    }

    public static void zip(File source, OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.setEncoding("UTF-8");
        zip(source, "", zos);
        zos.closeEntry();
        zos.close();
    }

    private static void zip(File source, String basePath, ZipOutputStream zos) throws IOException {
        String path = Files.getPathString(basePath, source.getName());
        if (source.isFile()) {
            zos.putNextEntry(new ZipEntry(path));
            FileUtils.copyFile(source, zos);
        } else {
            File[] files = source.listFiles();
            if (Objects.nonNull(files)) {
                for (File file : files) {
                    zip(file, path, zos);
                }
            }
        }
    }
}
