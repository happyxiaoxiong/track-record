package cn.cnic.trackrecord.common.enumeration;


public enum Gender implements ValuedEnum {
    UNKNOWN("保密", 0),
    MAIL("男", 1),
    FAMAIL("女", 2);

    private String name;
    private int value;

    Gender(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
