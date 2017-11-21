package com.deloitte.smt.exception;


import org.apache.log4j.Logger;
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
	
	private static final Logger LOG = Logger.getLogger(ExceptionBuilder.class);
	
	 MessageSource messageSource;
	
	public  ApplicationException buildException(ErrorType errorType,Throwable throwable){
		LOG.info(throwable);
		return new ApplicationException(errorType.getCode(),
				messageSource.getMessage(errorType.getLabel(), null,errorType.getDefaultMessage(), null), null);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource=messageSource;
	}
}
