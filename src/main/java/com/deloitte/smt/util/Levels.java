package com.deloitte.smt.util;

import java.io.Serializable;

import lombok.Data;

@Data
public class Levels implements Serializable{
	private static final long serialVersionUID = -2191588040985275529L;
	private String key;
	private String value;
}
