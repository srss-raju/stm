package com.deloitte.smt.util;

import java.io.Serializable;

import lombok.Data;
@Data
public class ServerResponseObject implements Serializable{
	private static final long serialVersionUID = 5562360628214122761L;
	private String status;
	private String message;
	private transient  Object response;
}