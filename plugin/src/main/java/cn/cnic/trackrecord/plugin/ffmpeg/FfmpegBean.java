package cn.cnic.trackrecord.plugin.ffmpeg;

import cn.cnic.trackrecord.common.util.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
public class FfmpegBean {

    @Autowired
    private FfmpegProperties properties;

    public boolean encodeH264(String srcVideoPath, String destVideoPath) {
        // ffmpeg -i input.avi -r 24 output.mp4
        // ffmpeg -i input.avi -r 24 output.ogg
        // ffmpeg -i input.mov -vcodec h264 -acodec aac -strict -2 -r 24 output.mp4 采用这种方式转码
        List<String> command = new java.util.ArrayList<>();
        command.add(properties.getPath());
        command.add("-y");
        command.add("-i");
        command.add(srcVideoPath);
        command.add("-vcodec");
        command.add("h264");
        command.add("-acodec");
        command.add("aac");
        command.add("-strict");
        command.add("-2");
        command.add("-r");
        command.add("24");
        command.add(destVideoPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            //DEBUG
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug(line);
            }
            process.waitFor();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        if (!Files.exists(destVideoPath)) {
            log.error("`{}`转换失败", srcVideoPath);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        FfmpegBean ffmpegBean = new FfmpegBean();
        ffmpegBean.properties = new FfmpegProperties();
        ffmpegBean.properties.setPath("D:/ffmpeg/bin/ffmpeg");
        ffmpegBean.encodeH264("D:\\ffmpeg\\bin\\test.mp4", "D:\\ffmpeg\\bin\\hello.mp4");
    }
}
