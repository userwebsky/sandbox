package com.example.serverdiscoveryexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServerDiscoveryExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerDiscoveryExampleApplication.class, args);
	}

}
