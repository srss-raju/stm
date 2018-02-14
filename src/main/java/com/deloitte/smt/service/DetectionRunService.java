package com.deloitte.smt.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.deloitte.smt.dto.DetectionRunDTO;
import com.deloitte.smt.dto.DetectionRunResponseDTO;
import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.DetectionRunRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by rajesh on 05-05-2017.
 */
@Service
public class DetectionRunService {
	
	private static final Logger LOG = Logger.getLogger(DetectionRunService.class);
	
	@Value("${aws.detectionrun.service}")
	String detectionRunServiceURL;

    @Autowired
    DetectionRunRepository detectionRunRepository;
    
    @Autowired
    SignalDetectionService signalDetectionService;
    
    @Autowired
    ProductHierarchyService productHierarchyService;
    
    @Autowired
    SocHierarchyService socHierarchyService;
    
    public DetectionRunResponseDTO insert(DetectionRun detectionRun) {
    	DetectionRunResponseDTO response = null;
    	DetectionRunDTO dto = new DetectionRunDTO();
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
   
        RestTemplate restTemplate = new RestTemplate();
   
        // Data attached to the request.
        
    	boolean productFlag = false;
    	boolean eventFlag = false;
    	detectionRun.setCreatedDate(new Date());
    	detectionRun.setRunDate(new Date());
    	detectionRun.setLastModifiedDate(new Date());
    	DetectionRun detectionRunUpdated = detectionRunRepository.save(detectionRun);
    	try {
    		SignalDetection signalDetection = signalDetectionService.findById(detectionRun.getDetectionId());
    		
    		if(!CollectionUtils.isEmpty(signalDetection.getConditions())){
    			String eventKey;
    			for(TopicSocAssignmentConfiguration condition:signalDetection.getConditions()){
    				eventKey = socHierarchyService.getEventKeyOfSelectedLevel(condition.getRecordValues(),dto);
    				if(!eventFlag){
    					eventFlag = true;
    					dto.setPrimaryEventKey(eventKey);
    				}else{
    					dto.setSecondaryEventKey(eventKey);
    				}
    			}
    		}
    		
    		if(!CollectionUtils.isEmpty(signalDetection.getProducts())){
    			String productKey;
    			String ingredient;
    			for(TopicProductAssignmentConfiguration product:signalDetection.getProducts()){
    				productKey = productHierarchyService.getProductKeyOfActLevel(product.getRecordValues(), dto);
    				ingredient = productHierarchyService.getIngredientOfActLevel(product.getRecordValues());
    				if(!productFlag){
    					productFlag = true;
    					dto.setPrimaryProductKey(productKey);
    					dto.setPrimaryProductIngredientKey(ingredient);
    				}else{
    					dto.setSecondaryProductKey(productKey);
    					dto.setSecondaryProductIngredientKey(ingredient);
    				}
    			}
    		}
    		dto.setRunInstanceId(detectionRunUpdated.getId());
    		dto.setQueries(signalDetection.getQueries());
    		dto.setStratifications(signalDetection.getStratifications());
    		
    		ObjectMapper mapper = new ObjectMapper();
    		String jsonInString = null;
			try {
				jsonInString = mapper.writeValueAsString(dto);
			} catch (JsonProcessingException e) {
				LOG.error(e);
			}
    		
    		
    		HttpEntity<String> requestBody = new HttpEntity<>(jsonInString, headers);
    		response = restTemplate.postForObject(detectionRunServiceURL, requestBody, DetectionRunResponseDTO.class);
            // Send request with POST method.
    		LOG.info("Response === "+ response);
		} catch (ApplicationException e) {
			LOG.error(e);
		}
    	response.setRunInstanceId(detectionRunUpdated.getId());
        return response;
    }
    
    public DetectionRun update(DetectionRun detectionRun) throws ApplicationException {
        if(detectionRun.getId() == null) {
            throw new ApplicationException("Required field Id is no present in the given request.");
        }
        return detectionRunRepository.save(detectionRun);
    }

    public DetectionRun findById(Long detectionRunId) throws ApplicationException {
    	DetectionRun detectionRun = detectionRunRepository.findOne(detectionRunId);
        if(detectionRun == null) {
            throw new ApplicationException("Detection Run not found with the given Id : "+detectionRunId);
        }
        return detectionRun;
    }

    public List<DetectionRun> findAll() {
        return detectionRunRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }

    public List<DetectionRun> findByDetectionId(Long detectionId) {
        return detectionRunRepository.findByDetectionId(detectionId, new Sort(Sort.Direction.DESC, "createdDate"));
    }

	public void delete(Long detectionRunId) throws ApplicationException {
		DetectionRun detectionRun = detectionRunRepository.findOne(detectionRunId);
        if(detectionRun == null) {
            throw new ApplicationException("Detection Run not found with the given Id : "+detectionRunId);
        }else{
        	detectionRunRepository.delete(detectionRun);
        }
		
	}
}
