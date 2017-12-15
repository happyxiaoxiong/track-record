package cn.cnic.trackrecord.common.http;

import java.util.HashMap;
import java.util.Map;

public enum HttpResCode {
    SUCCESS(0),
    FAIL(-1),
    NOT_FOUND(-2);

    private int code;

    public int getCode() {
        return code;
    }

    HttpResCode(int code) {
        this.code = code;
    }

    public static Map<String, Integer> httpResCodeMap() {
        Map<String, Integer> result = new HashMap<>();
        for (HttpResCode code : HttpResCode.values()) {
            result.put(code.name().toLowerCase(), code.getCode());
        }
        return result;
    }
}
