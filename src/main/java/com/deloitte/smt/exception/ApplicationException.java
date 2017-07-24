package com.deloitte.smt.exception;

/**
 * Created by cavula on 22-05-2017.
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = -7514043640207755571L;

	private final Integer errorCode;

	
	public ApplicationException(String message) {
		super(message);
		errorCode=null;
	}
	
	public ApplicationException(String message,ApplicationException exception) {
		super(message,exception);
		errorCode=exception.getErrorCode();
	}
	
	public ApplicationException(String message,Throwable exception) {
		super(message,exception);
		errorCode=null;
	}

	public ApplicationException(Integer errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}


	public Integer getErrorCode() {
		return errorCode;
	}

}
