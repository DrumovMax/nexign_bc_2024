package com.nexign.bootcamp.emulator.config;

import com.nexign.bootcamp.emulator.component.InitBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CDRConfig {

    @Bean(initMethod = "initCDRFiles")
    public InitBean initCDRFiles () {
        return new InitBean();
    }

}
