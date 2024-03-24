package com.nexign.bootcamp.emulator.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a User Data Record (UDR) containing information about a subscriber's calls.
 */
@Data
@Builder
public class Udr {

    private Long msisdn;

    private TimeMeter incomingCall;

    private TimeMeter outcomingCall;

}
