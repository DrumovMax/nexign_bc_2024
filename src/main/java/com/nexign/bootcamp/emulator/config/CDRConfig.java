package com.nexign.bootcamp.emulator.config;

import com.nexign.bootcamp.emulator.commands.ShellCommands;
import com.nexign.bootcamp.emulator.component.InitBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CDRConfig {

    @Bean(initMethod = "initSubs")
    public InitBean initSubscriber () {
        return new InitBean();
    }

    @Bean(initMethod = "startEmulate")
    public InitBean emulateSwitchWork () { return new InitBean(); }

    @Bean(initMethod = "subs")
    public ShellCommands initMessage () {
        System.out.println("==============================================");
        System.out.println("|                                            |");
        System.out.println("|   Call command help, to explore commands   |");
        System.out.println("|                                            |");
        System.out.println("==============================================");
        return new ShellCommands();
    }

}

