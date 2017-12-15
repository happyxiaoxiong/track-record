package cn.cnic.trackrecord.common.serializer;

import cn.cnic.trackrecord.common.date.LongDate;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LongDateSerializer extends JsonSerializer<LongDate> {

    @Override
    public void serialize(LongDate longDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(longDate.getString());
    }
}
