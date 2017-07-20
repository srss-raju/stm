package com.deloitte.smt.exception;

/**
 * Created by cavula on 22-05-2017.
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = -7514043640207755571L;

	private Exception exception;
	private Integer errorCode;

	
	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Integer errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}


}
