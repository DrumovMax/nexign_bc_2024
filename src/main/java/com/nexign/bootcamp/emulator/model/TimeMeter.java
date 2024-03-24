package com.nexign.bootcamp.emulator.model;

import lombok.Data;

/**
 * Represents a time meter used for tracking call durations.
 */
@Data
public class TimeMeter {

    private Long totalTime;

    public TimeMeter (Long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * Merges this TimeMeter with another TimeMeter by adding their total times.
     *
     * @param other the other TimeMeter to merge with
     * @return a new TimeMeter with the total time being the sum of the total times of this TimeMeter and the other TimeMeter
     */
    public TimeMeter merge(TimeMeter other) {
        return new TimeMeter(this.totalTime + other.totalTime);
    }
}
