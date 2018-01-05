package cn.cnic.trackrecord.common.kmz;

import cn.cnic.trackrecord.common.util.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Enumeration;

@Slf4j
public class UnzipBean {

    public void unzip(String filePath, String unZipPath, boolean isDelete) throws IOException {
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


}
