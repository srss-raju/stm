package com.deloitte.smt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootApplication
public class SignalManagementApplication {

	@Autowired
	ProcessEngineConfigurationImpl processEnginerConfiguration;
	
	@PostConstruct
	public void customizeProcessEngineConfiguration(){
		processEnginerConfiguration.setDbHistoryUsed(false);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SignalManagementApplication.class, args);
	}
}
