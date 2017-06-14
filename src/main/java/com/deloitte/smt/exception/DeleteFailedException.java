package com.deloitte.smt.exception;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
public class DeleteFailedException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7613234316403537826L;

	public DeleteFailedException(String message) {
        super(message);
    }
}
