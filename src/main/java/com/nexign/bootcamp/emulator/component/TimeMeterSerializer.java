package com.nexign.bootcamp.emulator.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nexign.bootcamp.emulator.model.TimeMeter;

import java.lang.reflect.Type;

/**
 * Serializer for converting TimeMeter objects to JSON format.
 */
public class TimeMeterSerializer implements JsonSerializer<TimeMeter> {

    /**
     * Converts time in seconds to a string representation of time in the format "hh:mm:ss".
     *
     * @param secs time in seconds
     * @return string representation of time
     */
    private String timeToString(long secs) {
        return "%02d:%02d:%02d".formatted(secs / 3600, secs / 60 % 60, secs % 60);
    }

    /**
     * Serializes a TimeMeter object to JSON format.
     *
     * @param timeMeter              the TimeMeter object to serialize
     * @param type                   the type of the object to serialize
     * @param jsonSerializationContext the context for serialization
     * @return the JSON representation of the TimeMeter object
     */
    @Override
    public JsonElement serialize(TimeMeter timeMeter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("totalTime", timeToString(timeMeter.getTotalTime()));
        return jsonObject;
    }
}
