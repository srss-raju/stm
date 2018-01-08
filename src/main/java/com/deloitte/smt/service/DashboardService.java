package com.deloitte.smt.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.AlgorithmType;
import com.deloitte.smt.constant.ExecutionType;
import com.deloitte.smt.constant.SignalConfirmationStatus;
import com.deloitte.smt.constant.ValidationOutComesLabelTypes;
import com.deloitte.smt.dto.SignalDetectDTO;
import com.deloitte.smt.dto.SignalStrengthOverTimeDTO;
import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.dto.ValidationOutComesDTO;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.SignalStatisticsRepository;

@Service
public class DashboardService {

	private static final Logger LOG = Logger.getLogger(DashboardService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SignalService signalService;

	@Autowired
	private SignalMatchService signalMatchService;

	@Autowired
	private SignalStatisticsRepository signalStatisticsRepository;

	@SuppressWarnings("unchecked")
	public Map<String, List<SmtComplianceDto>> getSmtComplianceDetails() {
		LOG.info("Method Start getSmtComplianceDetails");
		Map<String, List<SmtComplianceDto>> smtComplianceMap = new HashMap<>();
		Query signalQuery = entityManager.createNativeQuery(
				"select case when current_timestamp > due_date then 'LATE' else 'ONTIME' end as STATUS,COUNT(*) from sm_topic where due_date is not null GROUP BY STATUS");
		List<Object[]> signals = signalQuery.getResultList();
		complianceResponse(smtComplianceMap, signals, "Signals");

		Query assessmentQuery = entityManager.createNativeQuery(
				"select case when current_timestamp > assessment_due_date then 'LATE' else 'ONTIME' end as STATUS,COUNT(*) from sm_assessment_plan where assessment_due_date is not null GROUP BY STATUS");
		List<Object[]> assessments = assessmentQuery.getResultList();
		complianceResponse(smtComplianceMap, assessments, "Assessment Plans");

		Query riskQuery = entityManager.createNativeQuery(
				"select case when current_timestamp > risk_due_date then 'LATE' else 'ONTIME' end as RISKSTATUS,COUNT(*) from sm_risk_plan where risk_due_date is not null GROUP BY RISKSTATUS");
		List<Object[]> risks = riskQuery.getResultList();
		complianceResponse(smtComplianceMap, risks, "Risk Plans");

		return smtComplianceMap;
	}

	/**
	 * @param smtComplianceMap
	 * @param authors
	 */
	public void complianceResponse(Map<String, List<SmtComplianceDto>> smtComplianceMap, List<Object[]> results,
			String type) {
		List<SmtComplianceDto> smtComplianceList = new ArrayList<>();
		if (results != null) {
			smtComplianceList = new ArrayList<>();
			for (Object[] row : results) {
				SmtComplianceDto dto = new SmtComplianceDto();
				dto.setStatus((String) row[0]);
				dto.setCount(((BigInteger) row[1]).longValue());
				smtComplianceList.add(dto);
			}
			smtComplianceMap.put(type, smtComplianceList);
		}

		Set<String> addedExecutionTypes = smtComplianceList.stream().map(SmtComplianceDto::getStatus)
				.collect(Collectors.toSet());

		List<String> unAddedTypes = ExecutionType.getExecutionTypes().stream()
				.filter(type1 -> !addedExecutionTypes.contains(type1)).collect(Collectors.toList());

		for (String unAddedType : unAddedTypes) {
			SmtComplianceDto dto = new SmtComplianceDto();
			dto.setCount(0l);
			dto.setStatus(unAddedType);
			smtComplianceList.add(dto);
		}

	}

	

	

	


	public List<ValidationOutComesDTO> generateDataForValidateOutcomesChart() {
		List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();

		Query validatedSignalsWithOutRisk = entityManager.createNativeQuery(
				"select count(*) from sm_assessment_plan a  inner join sm_topic t on a.id=t.assessment_plan_id where t.created_date >= (now() - interval '1 month') and a.risk_plan_id is null and t.signal_confirmation='"
						+ SignalConfirmationStatus.VALIDATED_SIGNAL.getName() + "'");
		Object validatedSignalsWithOutRiskResults = validatedSignalsWithOutRisk.getSingleResult();
		valiatedSignalWithoutRisk(validateOutComesList, validatedSignalsWithOutRiskResults);

		Query validatedSignalsWithRisk = entityManager.createNativeQuery(
				"select count(*) from sm_assessment_plan a  inner join sm_topic t on a.id=t.assessment_plan_id where t.created_date >= (now() - interval '1 month') and ( a.risk_plan_id>0 and t.signal_confirmation='"
						+ SignalConfirmationStatus.VALIDATED_SIGNAL.getName() + "')");
		Object validatedSignalsWithRiskResults = validatedSignalsWithRisk.getSingleResult();
		validatedSignalWithRisk(validateOutComesList, validatedSignalsWithRiskResults);

		Query nonSignals = entityManager.createNativeQuery(
				"select count(*) from sm_topic t where t.created_date >= (now() - interval '1 month') and (t.signal_confirmation='"
						+ SignalConfirmationStatus.NON_SIGNAL + "' or t.signal_confirmation='"
						+ SignalConfirmationStatus.NOT_YET_DETERMINED + "')");
		Object nonSignalsResults = nonSignals.getSingleResult();
		nonSignal(validateOutComesList, nonSignalsResults);

		Query continueToMonitor = entityManager.createNativeQuery(
				"select count(*) from sm_topic t where  t.created_date >= (now() - interval '1 month') and t.signal_confirmation='"
						+ SignalConfirmationStatus.CONTINUE_TO_MONITOR + "'");
		Object continueToMonitorResults = continueToMonitor.getSingleResult();
		continueToMontior(validateOutComesList, continueToMonitorResults);

		return validateOutComesList;
	}

	/**
	 * @param validateOutComesList
	 * @param continueToMonitorResults
	 */
	public void continueToMontior(List<ValidationOutComesDTO> validateOutComesList, Object continueToMonitorResults) {
		ValidationOutComesDTO validationOutComeDTO4 = new ValidationOutComesDTO();
		validationOutComeDTO4.setLabel(ValidationOutComesLabelTypes.CONTINUE_TO_MONTOR);
		validationOutComeDTO4.setCount(((BigInteger) continueToMonitorResults).longValue());
		validationOutComeDTO4.setColor("#017CAB");
		validateOutComesList.add(validationOutComeDTO4);
	}

	/**
	 * @param validateOutComesList
	 * @param nonSignalsResults
	 */
	public void nonSignal(List<ValidationOutComesDTO> validateOutComesList, Object nonSignalsResults) {
		ValidationOutComesDTO validationOutComeDTO3 = new ValidationOutComesDTO();
		validationOutComeDTO3.setLabel(ValidationOutComesLabelTypes.NON_SIGNAL);
		validationOutComeDTO3.setCount(((BigInteger) nonSignalsResults).longValue());
		validationOutComeDTO3.setColor("#18A634");
		validateOutComesList.add(validationOutComeDTO3);
	}

	/**
	 * @param validateOutComesList
	 * @param validatedSignalsWithRiskResults
	 */
	public void validatedSignalWithRisk(List<ValidationOutComesDTO> validateOutComesList,
			Object validatedSignalsWithRiskResults) {
		ValidationOutComesDTO validationOutComeDTO2 = new ValidationOutComesDTO();
		validationOutComeDTO2.setLabel(ValidationOutComesLabelTypes.VALIDATED_SIGNAL_WITH_RISK);
		validationOutComeDTO2.setCount(((BigInteger) validatedSignalsWithRiskResults).longValue());
		validationOutComeDTO2.setColor("#F69632");
		validateOutComesList.add(validationOutComeDTO2);
	}

	/**
	 * @param validateOutComesList
	 * @param validatedSignalsWithOutRiskResults
	 */
	public void valiatedSignalWithoutRisk(List<ValidationOutComesDTO> validateOutComesList,
			Object validatedSignalsWithOutRiskResults) {
		ValidationOutComesDTO validationOutComeDTO1 = new ValidationOutComesDTO();
		validationOutComeDTO1.setLabel(ValidationOutComesLabelTypes.VALIDATED_SIGNAL_WITHOUT_RISK);
		validationOutComeDTO1.setCount(((BigInteger) validatedSignalsWithOutRiskResults).longValue());
		validationOutComeDTO1.setColor("#E55757");
		validateOutComesList.add(validationOutComeDTO1);
	}

	@SuppressWarnings("unchecked")
	public List<SignalDetectDTO> getDetectedSignalDetails() {
		LOG.info("Method Start getDetectedSignalDetails");
		Query signalQuery = entityManager.createNativeQuery(
				"select to_timestamp(to_char(created_date,'Mon-yy'),'Mon-yy') \\:\\: timestamp without time zone cd, sum(case when signal_status='New' then 1 else 0 end) as signalcount,sum(case when signal_status<>'New' then 1 else 0 end) as recurringcount,count(signal_status) as totalsignalcount,sum(cases_count) as casesCount from sm_topic group by cd order by to_timestamp(to_char(created_date,'Mon-yy'),'Mon-yy') \\:\\: timestamp without time zone");
		List<Object[]> signals = signalQuery.getResultList();
		return detectedSignals(signals);
	}

	/**
	 * @param signals
	 * @return
	 */
	public List<SignalDetectDTO> detectedSignals(List<Object[]> signals) {
		List<SignalDetectDTO> signalDetectDTOs =  new ArrayList<>();
		
		if (!CollectionUtils.isEmpty(signals)) {
			
		
			for (Object[] signal : signals) {
				SignalDetectDTO dto = new SignalDetectDTO();
				dto.setMonth((Timestamp) signal[0]);
				dto.setSignalCount(((BigInteger) signal[1]).longValue());
				dto.setRecurringCount(((BigInteger) signal[2]).longValue());
				dto.setTotalSignalCount(((BigInteger) signal[3]).longValue());
				if(null!=signal[4]){
				dto.setCasesCount(((BigDecimal) signal[4]).longValue());
				}
				signalDetectDTOs.add(dto);
			}
		}
		return signalDetectDTOs;
	}

	public Map<String, Object> getSignalStrength(List<Long> topicList) throws ApplicationException {
		Map<String, Object> dataMap = new HashMap<>();
		AlgorithmType[] algorithmTypes = AlgorithmType.values();
		dataMap.put("algorithms", algorithmTypes);

		Calendar calendarYearOld = Calendar.getInstance();
		calendarYearOld.add(Calendar.YEAR, -1);

		Calendar calendarNow = Calendar.getInstance();
		Map<Long, List<SignalStrengthOverTimeDTO>> map = new HashMap<>();

		for (Long topicId : topicList) {
				Topic topic = signalService.findById(topicId);

				List<Topic> matchingSignals = signalMatchService.getMatchingSignals(topic);
				matchingSignals.add(topic);

				// Accept only one year old data
				Map<Long, Topic> matchingSignalsMap = matchingSignals.stream()
						.filter(item -> calendarNow.getTimeInMillis()
								- item.getCreatedDate().getTime() <= calendarYearOld.getTimeInMillis())
						.collect(Collectors.toMap(x -> x.getId(), x -> x));

				List<SignalStatistics> signalStatistics = signalStatisticsRepository
						.findStatisticsByTopicsIds(matchingSignalsMap.keySet());

				List<SignalStrengthOverTimeDTO> dtoList = new ArrayList<>();

				// Prepare map by Key Month
				for (SignalStatistics signalStatistics2 : signalStatistics) {
					SignalStrengthOverTimeDTO dto = new SignalStrengthOverTimeDTO();
					dto.setTopicId(signalStatistics2.getTopic().getId());
					dto.setAlgorithm(signalStatistics2.getAlgorithm());
					dto.setLb(signalStatistics2.getLb());
					dto.setUb(signalStatistics2.getUb());
					dto.setSignalStatus(signalStatistics2.getTopic().getSignalStatus());
					dto.setTimestamp(signalStatistics2.getTopic().getCreatedDate().getTime());
					dtoList.add(dto);
				}

				map.put(topicId, dtoList);
		}

		dataMap.put("chartData", map);
		return dataMap;

	}

}
