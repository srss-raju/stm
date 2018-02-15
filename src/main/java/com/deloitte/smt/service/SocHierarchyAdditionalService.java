package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignmentCondition;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.DetectionRunRepository;

/**
 * 
 * @author RKB
 *
 */
@Service
public class SocHierarchyAdditionalService {
	
	private static final Logger LOG = Logger.getLogger(SocHierarchyAdditionalService.class);

	@Autowired
	SocMedraHierarchyDAO socMedraHierarchyDAO;
	
	@Autowired
	DetectionRunRepository detectionRunRepository;
	
	@Autowired
	SignalDetectionService signalDetectionService;

	public List<TopicSocAssignmentConfiguration> getConditions(Topic topic){
		List<TopicSocAssignmentConfiguration> conditions = new ArrayList<>();
		StringBuilder ptBuilder = new StringBuilder();
		if(!CollectionUtils.isEmpty(topic.getSocs())){
			getDetectionCondition(topic, ptBuilder);
			for(Soc soc:topic.getSocs()){
				if(!CollectionUtils.isEmpty(soc.getPts())){
					getConditions(conditions, soc, ptBuilder);
				}
			}
		}
		return conditions;
		
	}

	private void getDetectionCondition(Topic topic, StringBuilder ptBuilder) {
		DetectionRun detectionRun = detectionRunRepository.findOne(topic.getRunInstanceId());
		if(detectionRun != null){
			try {
				SignalDetection signalDetection = signalDetectionService.findById(detectionRun.getDetectionId());
				buildPtConditionQuery(ptBuilder, signalDetection);
				
			} catch (ApplicationException e) {
				LOG.error(e);
			}
		}
	}

	private void buildPtConditionQuery(StringBuilder ptBuilder,SignalDetection signalDetection) {
		if(!CollectionUtils.isEmpty(signalDetection.getConditions())){
			for(TopicSocAssignmentConfiguration condition : signalDetection.getConditions()){
				if(!CollectionUtils.isEmpty(condition.getRecordValues())){
					buildPtQuery(ptBuilder, condition);
				}
			}
		}
	}

	private void buildPtQuery(StringBuilder ptBuilder,
			TopicSocAssignmentConfiguration condition) {
		for(TopicAssignmentCondition record : condition.getRecordValues()){
			if("SOC_CODE".equalsIgnoreCase(record.getCategory())){
				ptBuilder.append("SOC_DESC='").append(record.getCategoryDesc()).append("' ");
			}
			if("HLGT_CODE".equalsIgnoreCase(record.getCategory())){
				ptBuilder.append(SmtConstant.AND).append(" HLGT_DESC='").append(record.getCategoryDesc()).append("' ");	
			}
			if("HLT_CODE".equalsIgnoreCase(record.getCategory())){
				ptBuilder.append(SmtConstant.AND).append(" HLT_DESC='").append(record.getCategoryDesc()).append("' ");
			}
			if("LLT_CODE".equalsIgnoreCase(record.getCategory())){
				ptBuilder.append(SmtConstant.AND).append(" LLT_DESC='").append(record.getCategoryDesc()).append("' ");
			}
		}
	}

	private void getConditions(List<TopicSocAssignmentConfiguration> conditions, Soc soc, StringBuilder ptBuilder ) {
		for(Pt pt:soc.getPts()){
			if(ptBuilder.toString().length() != 0){
				ptBuilder.append(SmtConstant.AND);
			}
			ptBuilder.append(" PT_DESC='").append(pt.getPtName()).append("'");
			 List<SocHierarchyDto> ptList = socMedraHierarchyDAO.findActLevelsByPtDesc(ptBuilder.toString());
			 if(!CollectionUtils.isEmpty(ptList)){
				getRecordValues(conditions, ptList);
			 }
		}
	}

	private void getRecordValues(List<TopicSocAssignmentConfiguration> conditions, List<SocHierarchyDto> ptList) {
			SocHierarchyDto record = ptList.get(0);
			TopicSocAssignmentConfiguration topicProductAssignmentConfiguration = new TopicSocAssignmentConfiguration();
			List<TopicAssignmentCondition> recordValues = new ArrayList<>();
			StringBuilder recordKey = new StringBuilder();
			
			if(record.getSoc_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				topicAssignmentCondition.setCategory("SOC_CODE");
				topicAssignmentCondition.setCategoryCode(record.getSoc_code());
				topicAssignmentCondition.setCategoryDesc(record.getSoc_desc());
				topicAssignmentCondition.setCategoryName("SOC");
				recordKey.append(record.getSoc_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getHlgt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("@#");
				topicAssignmentCondition.setCategory("HLGT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getHlgt_code());
				topicAssignmentCondition.setCategoryDesc(record.getHlgt_desc());
				topicAssignmentCondition.setCategoryName("HLGT");
				recordKey.append(record.getHlgt_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getHlt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("@#");
				topicAssignmentCondition.setCategory("HLT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getHlt_code());
				topicAssignmentCondition.setCategoryDesc(record.getHlt_desc());
				topicAssignmentCondition.setCategoryName("HLT");
				recordKey.append(record.getHlt_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getPt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("@#");
				topicAssignmentCondition.setCategory("PT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getPt_code());
				topicAssignmentCondition.setCategoryDesc(record.getPt_desc());
				topicAssignmentCondition.setCategoryName("PT");
				recordKey.append(record.getPt_code());
				recordValues.add(topicAssignmentCondition);
			}
			
			topicProductAssignmentConfiguration.setRecordKey(recordKey.toString());
			topicProductAssignmentConfiguration.setRecordValues(recordValues);
			conditions.add(topicProductAssignmentConfiguration);
	}

}
