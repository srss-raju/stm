package com.deloitte.smt;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;

import com.deloitte.smt.exception.ApplicationException;


@SpringBootApplication
public class SignalManagementApplication {

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
