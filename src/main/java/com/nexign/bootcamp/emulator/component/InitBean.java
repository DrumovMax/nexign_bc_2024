package com.nexign.bootcamp.emulator.component;

import com.nexign.bootcamp.emulator.service.CDRService;
import com.nexign.bootcamp.emulator.service.SubscriberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Component responsible for initializing subscriber data and starting CDR emulation.
 */
@Component
public class InitBean {

    /**
     * Service for initializing subscriber data.
     */
    @Resource
    private SubscriberService subscriberService;

    /**
     * Service for emulating CDR switches.
     */
    @Resource
    private CDRService cdrService;

    /**
     * Initializes subscriber data.
     */
    public void initSubs () {
        subscriberService.initSubs();
    }

    /**
     * Starts the emulation of CDR switches.
     */
    public void startEmulate () { cdrService.emulateSwitch(); }

}
