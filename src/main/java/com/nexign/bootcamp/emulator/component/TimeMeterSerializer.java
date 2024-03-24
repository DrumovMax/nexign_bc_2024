package com.nexign.bootcamp.emulator.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nexign.bootcamp.emulator.model.TimeMeter;

import java.lang.reflect.Type;

public class TimeMeterSerializer implements JsonSerializer<TimeMeter> {

    private String timeToString(long secs) {
        return "%02d:%02d:%02d".formatted(secs / 3600, secs / 60 % 60, secs % 60);
    }

    @Override
    public JsonElement serialize(TimeMeter timeMeter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("totalTime", timeToString(timeMeter.getTotalTime()));
        return jsonObject;
    }
}
