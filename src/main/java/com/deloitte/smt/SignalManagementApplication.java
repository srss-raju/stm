package com.deloitte.smt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;

import com.deloitte.smt.exception.ApplicationException;

import java.util.Map;

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
	
	
	@Bean
	public ErrorAttributes errorAttributes() {
	    return new DefaultErrorAttributes() {
	    	@Override
	        public Map<String, Object> getErrorAttributes(
	                RequestAttributes requestAttributes,
	                boolean includeStackTrace) {
	            Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
	            Throwable error = getError(requestAttributes);
	            if (error instanceof ApplicationException) {
	                errorAttributes.put("errorCode", ((ApplicationException)error).getErrorCode());
	            }
	            return errorAttributes;
	        }

	    };
	}
	
}
