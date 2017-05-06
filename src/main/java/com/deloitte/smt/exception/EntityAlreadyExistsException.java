package com.deloitte.smt.exception;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
public class EntityAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -5442576590565685076L;

	public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
