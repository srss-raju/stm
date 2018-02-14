package com.deloitte.smt.dto;

import java.util.Set;

import lombok.Data;


@Data
public class PtLltDTO {
	private Set<String> pts;
	private Set<String> llts;
}