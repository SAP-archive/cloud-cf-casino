package com.sample.casino;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard; 

@SpringBootApplication
@EnableHystrixDashboard
public class HCPWebApplication extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(HCPWebApplication.class);
	
    @Autowired
    JdbcTemplate jdbcTemplate;
	
    public static void main(String[] args) {
        new SpringApplicationBuilder(HCPWebApplication.class).run(args);
    }
    
    @Override
    public void run(String... args) throws Exception {
    	log.info("Creating tables");
    	
        //jdbcTemplate.execute("DROP TABLE IF EXISTS slotmachines");
    	
    	// create table for stickiness values of slot machines
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS slotmachines(" +
                "id VARCHAR(64), " + 
        		"imageUrl VARCHAR(256), " + 
                "date TIMESTAMP, " + 
                "casinoid VARCHAR(64), " + 
                "sentiment FLOAT CHECK (sentiment>=0.0 AND sentiment<=100.0), " +
                "emotion VARCHAR(32))");

    	log.info("Table slotmachines created.");
    }

}
