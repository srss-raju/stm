package com.deloitte.smt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LevelsDTO implements Serializable{
	private static final long serialVersionUID = -2191588040985275529L;
	private String key;
	private String value;
}
