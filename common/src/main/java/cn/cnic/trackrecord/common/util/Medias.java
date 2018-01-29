package cn.cnic.trackrecord.common.util;

public class Medias {
    private static final String[] IMAGE_TYPES = { "jpg", "png", "jpeg", "bmp", "jpe", "gif", "ico" };
    private static final String[] VIDEO_TYPES = { "mp4", "avi", "rmvb", "mpeg", "mov", "mkv", "vob", "flv", "rm",
            "asf", "f4v", "m4v", "3gp", "ts", "divx", "mpg", "mpe", "wmv", "mts"};

    public static boolean isImage(String fileName) {
        String lowercaseName = fileName.toLowerCase();
        for (String imgType : IMAGE_TYPES) {
            if (lowercaseName.endsWith(imgType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVideo(String fileName) {
        String lowercaseName = fileName.toLowerCase();
        for (String videoType : VIDEO_TYPES) {
            if (lowercaseName.endsWith(videoType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAudio(String fileName) {
        return false;
    }
}
