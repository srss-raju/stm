package com.deloitte.smt.exception;

/**
 * 
 * @author cavula on 20-jul-2017
 *
 */
public enum ErrorType {
	
	SIGNAL_NAME_DUPLICATE(100, "error.signal.name.duplicate","Failed to save Signal due to duplicate name"),
	ASSESSMENTPLAN_NAME_DUPLICATE(101, "error.assessmentplan.name.duplicate","Failed to save Assessment Plan due to duplicate name"),
	RISKPLAN_NAME_DUPLICATE(102,"error.riskplan.name.duplicate","Failed to save Risk Plan due to duplicate name"),
	ASSESSMENTACCTION_NAME_DUPLICATE(103, "error.assessmentaction.name.duplicate","Failed to save Assessment Template due to duplicate name"),
	RISKPACTION_NAME_DUPLICATE(104,"error.riskaction.task.name.duplicate","Failed to save Risk Template due to duplicate name"),
	DETECTION_NAME_DUPLICATE(105,"error.detection.name.duplicate","Failed to save Detection due to duplicate name"),
	ASSESSMENT_TASK_NAME_DUPLICATE(106,"error.assessment.task.name.duplicate","Failed to save Assessment Task due to duplicate name"),
	RISKTASK_NAME_DUPLICATE(107,"error.risk.task.name.duplicate","Failed to save Risk Task due to duplicate name"),
	NO_NAME(108,"error.task.noname","Name is not provided"),
	DUPLICATE_RECORD(109,"error.duplicate.record","Failed to save Template due to duplicate product"),;
	
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