package com.nexign.bootcamp.emulator.config;

import com.nexign.bootcamp.emulator.component.InitBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean(initMethod = "initSubs")
    public InitBean initSubscriber () {
        return new InitBean();
    }

    @Bean(initMethod = "startEmulate")
    public InitBean emulateSwitchWork () {
        return new InitBean();
    }

}
