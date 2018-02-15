package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.SocMedraHierarchyDAO;
import com.deloitte.smt.dto.SocHierarchyDto;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignmentCondition;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;

/**
 * 
 * @author RKB
 *
 */
@Service
public class SocHierarchyAdditionalService {

	@Autowired
	SocMedraHierarchyDAO socMedraHierarchyDAO;

	public List<TopicSocAssignmentConfiguration> getConditions(Topic topic){
		List<TopicSocAssignmentConfiguration> conditions = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(topic.getSocs())){
			for(Soc soc:topic.getSocs()){
				if(!CollectionUtils.isEmpty(soc.getPts())){
					getConditions(conditions, soc);
				}
			}
		}
		return conditions;
		
	}

	private void getConditions(List<TopicSocAssignmentConfiguration> conditions, Soc soc) {
		for(Pt pt:soc.getPts()){
			 List<SocHierarchyDto> ptList = socMedraHierarchyDAO.findActLevelsByPtDesc(pt.getPtName());
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
				recordKey.append(record.getSoc_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getHlgt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("#");
				topicAssignmentCondition.setCategory("HLGT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getHlgt_code());
				topicAssignmentCondition.setCategoryDesc(record.getHlgt_desc());
				recordKey.append(record.getHlgt_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getHlt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("#");
				topicAssignmentCondition.setCategory("HLT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getHlt_code());
				topicAssignmentCondition.setCategoryDesc(record.getHlt_desc());
				recordKey.append(record.getHlt_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getPt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("#");
				topicAssignmentCondition.setCategory("PT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getPt_code());
				topicAssignmentCondition.setCategoryDesc(record.getPt_desc());
				recordKey.append(record.getPt_code());
				recordValues.add(topicAssignmentCondition);
			}
			if(record.getLlt_code() != null){
				TopicAssignmentCondition topicAssignmentCondition = new TopicAssignmentCondition();
				recordKey.append("#");
				topicAssignmentCondition.setCategory("LLT_CODE");
				topicAssignmentCondition.setCategoryCode(record.getLlt_code());
				topicAssignmentCondition.setCategoryDesc(record.getLlt_desc());
				recordKey.append(record.getLlt_code());
				recordValues.add(topicAssignmentCondition);
			}
			topicProductAssignmentConfiguration.setRecordKey(recordKey.toString());
			topicProductAssignmentConfiguration.setRecordValues(recordValues);
			conditions.add(topicProductAssignmentConfiguration);
	}

}
