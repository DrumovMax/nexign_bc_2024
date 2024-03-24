package com.nexign.bootcamp.emulator.model;

import lombok.Data;

@Data
public class TimeMeter {

    private Long totalTime;

    public TimeMeter (Long totalTime) {
        this.totalTime = totalTime;
    }

    public TimeMeter merge(TimeMeter other) {
        return new TimeMeter(this.totalTime + other.totalTime);
    }
}
