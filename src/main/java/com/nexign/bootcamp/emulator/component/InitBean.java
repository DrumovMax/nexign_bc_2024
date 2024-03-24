package com.nexign.bootcamp.emulator.component;

import com.nexign.bootcamp.emulator.service.CDRService;
import com.nexign.bootcamp.emulator.service.SubscriberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InitBean {

    @Resource
    private SubscriberService subscriberService;

    @Resource
    private CDRService cdrService;


    public void initSubs () {
        subscriberService.initSubs();
    }

    public void startEmulate () { cdrService.emulateSwitch(); }

}
