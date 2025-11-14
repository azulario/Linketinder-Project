package com.linketinder.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Adaptador para serialização/desserialização de LocalDate com Gson
 */
class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE

    @Override
    void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.format(FORMATTER))
        }
    }

    @Override
    LocalDate read(JsonReader input) throws IOException {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            return null
        }
        String dateStr = input.nextString()
        return LocalDate.parse(dateStr, FORMATTER)
    }
}

