package cn.cnic.trackrecord.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract interface ValuedEnum {
    int getValue();

    @JsonValue
    String getName();
}
