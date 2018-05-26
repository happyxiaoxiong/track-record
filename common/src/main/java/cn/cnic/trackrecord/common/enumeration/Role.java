package cn.cnic.trackrecord.common.enumeration;

/**
 * 用户角色枚举
 */
public enum Role implements ValuedEnum {
    ADMIN("管理员", 0),
    USER("巡护人", 1);

    private String name;
    private int value;

    Role(String name, int value) {
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
