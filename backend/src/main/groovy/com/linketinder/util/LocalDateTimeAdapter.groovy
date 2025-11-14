package com.linketinder.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Override
    void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.format(FORMATTER))
        }
    }

    @Override
    LocalDateTime read(JsonReader input) throws IOException {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            return null
        }
        String dateTimeStr = input.nextString()
        return LocalDateTime.parse(dateTimeStr, FORMATTER)
    }
}

