package com.nexign.bootcamp.emulator.component;

import com.nexign.bootcamp.emulator.service.CDRService;
import com.nexign.bootcamp.emulator.service.SubscriberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.text.ParseException;


@Component
public class InitBean {

    @Resource
    private SubscriberService subscriberService;

    @Resource
    private CDRService cdrService;


    public void initSubs () {
        subscriberService.initSubs();
    }

    public void initCDRFiles() { cdrService.initCDRFiles(); }

    public void startEmulate () throws ParseException { cdrService.emulateSwitch(); }

}
