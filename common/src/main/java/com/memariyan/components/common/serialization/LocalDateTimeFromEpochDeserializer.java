package com.memariyan.components.common.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeFromEpochDeserializer extends StdDeserializer<LocalDateTime> {

    protected LocalDateTimeFromEpochDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException,
            JacksonException {
        return fromEpocMilli(jsonParser.readValueAs(Long.class));
    }

    public static LocalDateTime fromEpocMilli(Long epocMillisecond) {
        if (epocMillisecond == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epocMillisecond), ZoneId.systemDefault());
    }
}
