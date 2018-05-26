package cn.cnic.trackrecord.common.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回的编码，0表示成功，-1表示失败
 */
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

    public static Map<String, Integer> codeMap() {
        Map<String, Integer> result = new HashMap<>();
        for (HttpResCode code : HttpResCode.values()) {
            result.put(code.name().toLowerCase(), code.getCode());
        }
        return result;
    }
}
