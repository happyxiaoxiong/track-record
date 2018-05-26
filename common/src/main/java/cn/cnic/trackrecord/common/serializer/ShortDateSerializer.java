package cn.cnic.trackrecord.common.serializer;

import cn.cnic.trackrecord.common.date.ShortDate;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * json的ShortDate类型序列化
 */
public class ShortDateSerializer extends JsonSerializer<ShortDate> {

    @Override
    public void serialize(ShortDate shortDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(shortDate.getString());
    }
}
