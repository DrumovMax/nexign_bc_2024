package com.nexign.bootcamp.emulator.config;

import com.nexign.bootcamp.emulator.commands.ShellCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class for setting up and initializing beans related to UDR (User Data Record) generation and reporting.
 */
@Configuration
public class UdrConfig {

    /**
     * Bean for initializing the generation of UDR reports.
     *
     * @return an instance of {@link ShellCommands} configured to handle UDR report generation
     */
    @Bean(initMethod = "generateReport")
    public ShellCommands initGenerateReport () {
        return new ShellCommands();
    }

}

