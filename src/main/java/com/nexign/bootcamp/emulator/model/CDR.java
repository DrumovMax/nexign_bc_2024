package com.nexign.bootcamp.emulator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Call Detail Record (CDR) entity storing information about a call.
 */
@Data
@Builder
@Entity
@Table(name = "cdr")
@NoArgsConstructor
@AllArgsConstructor
public class CDR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CallType callType;

    private Long callerNumber;

    private Long startTime;

    private Long endTime;

    public Long getDuration () {
        return this.getEndTime() - this.getStartTime();
    }
}
