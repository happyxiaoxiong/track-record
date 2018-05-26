package cn.cnic.trackrecord.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举类型接口
 */
public interface ValuedEnum {
    int getValue();

    @JsonValue
    String getName();
}
