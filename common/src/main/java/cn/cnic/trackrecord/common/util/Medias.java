package cn.cnic.trackrecord.common.util;

public class Medias {
    private static final String[] IMAGE_TYPES = { "jpg", "png", "jpeg" };
    private static final String[] VIDEO_TYPES = { "mp4"};
    private static final String[] AUDIO_TYPES = { "aac", "mp3"};

    public static boolean isImage(String fileName) {
        return is(fileName, IMAGE_TYPES);
    }

    public static boolean isVideo(String fileName) {
        return is(fileName, VIDEO_TYPES);
    }

    public static boolean isAudio(String fileName) {
        return is(fileName, AUDIO_TYPES);
    }

    private static boolean is(String fileName, String[] types) {
        String lowercaseName = fileName.toLowerCase();
        for (String type : types) {
            if (lowercaseName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
}
