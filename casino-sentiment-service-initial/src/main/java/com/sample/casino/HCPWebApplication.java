package com.sample.casino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class HCPWebApplication extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(HCPWebApplication.class);


    public static void main(String[] args) {
        new SpringApplicationBuilder(HCPWebApplication.class).run(args);
    }

    //@Override
    public void run(String... args) throws Exception {
    	log.info("Creating tables");

			// PLACEHOLDER CODE SNIPPET 1

    	log.info("Table slotmachines created.");
    }

}
