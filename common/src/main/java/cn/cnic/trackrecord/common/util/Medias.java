package cn.cnic.trackrecord.common.util;

public abstract class Medias {
    private static final String[] IMAGE_TYPES = {"jpg", "png", "jpeg"};
    private static final String[] VIDEO_TYPES = {"mp4"};
    private static final String[] AUDIO_TYPES = {"aac", "mp3"};

    /**
     * 根据文件名判断是否图片
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {
        return is(fileName, IMAGE_TYPES);
    }

    /**
     * 根据文件名判断是否视频
     * @param fileName
     * @return
     */
    public static boolean isVideo(String fileName) {
        return is(fileName, VIDEO_TYPES);
    }

    /**
     * 根据文件名判断是否音频
     * @param fileName
     * @return
     */
    public static boolean isAudio(String fileName) {
        return is(fileName, AUDIO_TYPES);
    }

    /**
     * 判断文件是否属于某种类型
     * @param fileName
     * @param types
     * @return
     */
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
