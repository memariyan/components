package com.memariyan.components.common.serialization;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonAnnotationsInside
@JsonSerialize(using = LocalDateTimeToEpochSerializer.class)
@JsonDeserialize(using = LocalDateTimeFromEpochDeserializer.class)
public @interface LocalDateTimeSerialization {
}
