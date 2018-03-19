package cn.cnic.trackrecord.common.util;

public abstract class Objects {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }
}
