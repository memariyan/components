package com.memariyan.components.common.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class LocalDateTimeToEpochSerializer extends StdSerializer<LocalDateTime> {

    protected LocalDateTimeToEpochSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime time, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(toEpocMilli(time));
    }

    public static Long toEpocMilli(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
    }
}
