package cn.cnic.trackrecord.data.kml;

public enum PlaceMarkType {
    KEYPOINT("关键点标注", 1),
    ROUTE("路线标注", 2);

    private int value;
    private String name;

    PlaceMarkType(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
