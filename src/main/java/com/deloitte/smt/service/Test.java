package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.deloitte.smt.dto.DetectionRunDTO;
import com.deloitte.smt.dto.DetectionRunResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	
	public static void main(String aa[]) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
   
        RestTemplate restTemplate = new RestTemplate();
        
        List<String> pkeys = new ArrayList<>();
        pkeys.add("242967");
        pkeys.add("226838");
        List<String> events = new ArrayList<>();
        events.add("31272886");
        events.add("31231711");
        DetectionRunResponseDTO response = null;
    	DetectionRunDTO dto = new DetectionRunDTO();
    	dto.setRunInstanceId(1l);
    	dto.setPrimaryProductKey(pkeys);
    	dto.setPrimaryEventKey(events);
    	
    	ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
		try {
			jsonInString = mapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		HttpEntity<String> requestBody = new HttpEntity<>(jsonInString, headers);
		response = restTemplate.postForObject("http://10.145.56.77:5004/api/start", requestBody, DetectionRunResponseDTO.class);
        // Send request with POST method.
		System.out.println(response);
	}

}
