package com.fundatech.shareway.drivermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DriverManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverManagementApplication.class, args);
	}

}
