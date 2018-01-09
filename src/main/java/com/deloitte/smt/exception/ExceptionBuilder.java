package com.deloitte.smt.exception;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;


/**
 * 
 * @author cavula on 20-jul-2017
 *
 */
@Component
public class ExceptionBuilder implements MessageSourceAware{
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	 MessageSource messageSource;
	
	public  ApplicationException buildException(ErrorType errorType,Throwable throwable){
		logger.info(throwable);
		return new ApplicationException(errorType.getCode(),
				messageSource.getMessage(errorType.getLabel(), null,errorType.getDefaultMessage(), null), null);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource=messageSource;
	}
}
