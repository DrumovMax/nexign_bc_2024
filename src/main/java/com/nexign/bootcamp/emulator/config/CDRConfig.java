package com.nexign.bootcamp.emulator.config;

import com.nexign.bootcamp.emulator.commands.ShellCommands;
import com.nexign.bootcamp.emulator.component.InitBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up and initializing beans related to CDR (Call Detail Records) emulation.
 */
@Configuration
public class CDRConfig {

    /**
     * Bean for initializing subscriber data.
     *
     * @return an instance of {@link InitBean} configured to initialize subscriber data
     */
    @Bean(initMethod = "initSubs")
    public InitBean initSubscriber () {
        return new InitBean();
    }

    /**
     * Bean for starting the emulation of CDR switches.
     *
     * @return an instance of {@link InitBean} configured to start CDR emulation
     */
    @Bean(initMethod = "startEmulate")
    public InitBean emulateSwitchWork () { return new InitBean(); }

    /**
     * Bean for initializing shell commands.
     *
     * @return an instance of {@link ShellCommands} configured to handle shell commands
     */
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

