package com.deloitte.smt.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.SmtComplianceDto;

@Service
public class ChartsService {
	
	private static final Logger LOG = Logger.getLogger(ChartsService.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public Map<String, List<SmtComplianceDto>> getSmtComplianceDetails(){
		LOG.info("Method Start getSmtComplianceDetails");
		Map<String, List<SmtComplianceDto>> smtComplianceMap = new HashMap<>();
		Query signalQuery = entityManager.createNativeQuery("select case when current_timestamp > due_date then 'LATE' else 'ONTIME' end as STATUS,COUNT(*) from sm_topic where due_date is not null GROUP BY STATUS");
		List<Object[]> signals = signalQuery.getResultList();
		complianceResponse(smtComplianceMap, signals, "Signals");
		
		Query assessmentQuery = entityManager.createNativeQuery("select case when current_timestamp > assessment_due_date then 'LATE' else 'ONTIME' end as STATUS,COUNT(*) from sm_assessment_plan where assessment_due_date is not null GROUP BY STATUS");
		List<Object[]> assessments = assessmentQuery.getResultList();
		complianceResponse(smtComplianceMap, assessments, "Assessment Plans");
		
		Query riskQuery = entityManager.createNativeQuery("select case when current_timestamp > risk_due_date then 'LATE' else 'ONTIME' end as RISKSTATUS,COUNT(*) from sm_risk_plan where risk_due_date is not null GROUP BY RISKSTATUS");
		List<Object[]> risks = riskQuery.getResultList();
		complianceResponse(smtComplianceMap, risks, "Risk Plans");
		
		return smtComplianceMap;
	}

	/**
	 * @param smtComplianceMap
	 * @param authors
	 */
	private void complianceResponse(Map<String, List<SmtComplianceDto>> smtComplianceMap, List<Object[]> authors, String type) {
		List<SmtComplianceDto> smtComplianceList;
		if(!CollectionUtils.isEmpty(authors)){
			smtComplianceList = new ArrayList<>();
			for(Object[] row : authors){
				SmtComplianceDto dto = new SmtComplianceDto();
				dto.setCount(((BigInteger)row[1]).longValue());
				dto.setStatus((String)row[0]);
				smtComplianceList.add(dto);
			}
			smtComplianceMap.put(type, smtComplianceList);
		}
	}

}
