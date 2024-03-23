package com.nexign.bootcamp.emulator.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "cdr")
public class CDR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer callType;

    private Long callerNumber;

    private Long startTime;

    private Long endTime;

}
