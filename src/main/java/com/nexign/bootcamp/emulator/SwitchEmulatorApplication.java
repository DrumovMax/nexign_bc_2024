package com.nexign.bootcamp.emulator;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SwitchEmulatorApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SwitchEmulatorApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

}
