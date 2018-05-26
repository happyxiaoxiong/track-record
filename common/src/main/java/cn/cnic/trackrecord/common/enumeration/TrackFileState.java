package cn.cnic.trackrecord.common.enumeration;

/**
 * kmz文件处理状态
 */
public enum TrackFileState implements ValuedEnum {
    // 定义中间步骤遵守以 ... 结尾, 0表示完成，正整数代表正在进行到哪一步，对应的负整数表明哪一步出的啥错误
    UPLOAD_SUCCESS("上传成功待验证...", 1),
    VERIFYING("验证中...", 2),
    UNZIPPING("解压中...", 3),
    EXTRACTING_AND_SAVING("提取和保存中...", 4),
    FINISH("完成", 0),
    VERIFY_FAIL("验证失败", -2),
    UNZIP_FAIL("解压失败", -3),
    EXTRACT_AND_SAVE_FAIL("提取和保存失败", -4),
    TRY_EXCEED("重试失败", -5);

    private String name;
    private int value;

    TrackFileState(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
