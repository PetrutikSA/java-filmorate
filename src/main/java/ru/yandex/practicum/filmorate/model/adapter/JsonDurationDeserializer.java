package ru.yandex.practicum.filmorate.model.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class JsonDurationDeserializer extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonParser.getValueAsString()));
    }
}
