package com.deloitte.smt.exception;

/**
 * 
 * @author cavula on 20-jul-2017
 *
 */
public enum ErrorType {
	
	SIGNAL_NAME_DUPLICATE(100, "error.signal.name.duplicate","Signal with Same Name Existed"),
	ASSESSMENTPLAN_NAME_DUPLICATE(101, "error.assessmentplan.name.duplicate","Assessment Plan with Same Name Existed"),
	RISKPLAN_NAME_DUPLICATE(102,"error.riskplan.name.duplicate","Risk Plan with Same Name Existed"),
	ASSESSMENTACCTION_NAME_DUPLICATE(103, "error.assessmentaction.name.duplicate","Assessment Action with Same Name Existed"),
	RISKPACTION_NAME_DUPLICATE(104,"error.riskaction.task.name.duplicate","Risk Action with Same Name Existed"),
	DETECTION_NAME_DUPLICATE(105,"error.detection.name.duplicate","Detection with Same Name Existed"),
	ASSESSMENT_TASK_NAME_DUPLICATE(106,"error.assessment.task.name.duplicate","Assessment Task with Same Name Existed"),
	RISKTASK_NAME_DUPLICATE(107,"error.risk.task.name.duplicate","Risk Task with Same Name Existed"),
	NO_NAME(108,"error.risk.task.name.noname","Name is not provided");
	
	private final Integer code;
	private final String label;
	private final String defaultMessage;

	public String getDefaultMessage() {
		return defaultMessage;
	}

	private ErrorType(Integer code, String label, String defaultMessage) {
		this.code = code;
		this.label = label;
		this.defaultMessage = defaultMessage;
	}

	public String getLabel() {
		return label;
	}

	public Integer getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + label;
	}
}