package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.AssessmentTaskStatusType;
import com.deloitte.smt.constant.RiskTaskStatusType;
import com.deloitte.smt.constant.RunFrequency;
import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.entity.Filters;
import com.deloitte.smt.entity.FiltersStatus;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.FilterRepository;
import com.deloitte.smt.repository.FilterStatusRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.repository.TopicSignalDetectionAssignmentAssigneesRepository;

@Service
public class FiltersService {
	private static final Logger LOGGER = Logger.getLogger(FiltersService.class);
	@Autowired
	private FilterRepository filterRepository;
	
	@Autowired
	private FilterStatusRepository filterStatusRepository;

	@Autowired
	private IngredientRepository ingredientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private SocRepository socRepository;
    @Autowired
    private HlgtRepository hlgtRepository;
    @Autowired
    private HltRepository hltRepository;
    @Autowired
    private PtRepository ptRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private AssessmentPlanRepository assessmentPlanRepository;
    @Autowired
    private TopicSignalDetectionAssignmentAssigneesRepository topicSignalDetectionAssignmentAssigneesRepository;
	public List<FilterDTO> getFiltersByType(String type){
		List<FilterDTO> filterList = null;
		List<?> filterVals=null;
		try {
			List<String> typeList = Arrays.asList("generic",type);
			LOGGER.info("typeList.."+typeList);
			List<Filters> listfi = filterRepository.findByFilterTypes(typeList);
			if(!CollectionUtils.isEmpty(listfi))
			{
				filterList =  new ArrayList<>();
				for (Filters filters : listfi) {
					FilterDTO dto = new FilterDTO();
					dto.setFilterKey(filters.getKey());
					dto.setFilterName(filters.getName());
					filterVals = getAllFilters(filters.getName(),filters);
					dto.setFilterValues(filterVals);
					filterList.add(dto);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filterList;
	}

	private List<?> getAllFilters(String type, Filters filters) {
		LOGGER.info("type..." + type);
		List<?> filterVals = null;
		switch (type) {
		case "Status":
			filterVals = getStatuses(filters);
			break;
		case "Ingredient":
			filterVals = ingredientRepository.findDistinctIngredientNamesForSignal();
			break;
		case "Product":
			filterVals = productRepository.findDistinctProductNameForSignal();
			break;
		case "License":
			filterVals = licenseRepository.findDistinctLicenseNameForSignal();
			break;
		case "SOC":
			filterVals = socRepository.findDistinctSocNameForSignal();
			break;
		case "HLGT":
			filterVals = hlgtRepository.findDistinctHlgtNameForSignal();
			break;
		case "HLT":
			filterVals = hltRepository.findDistinctHltNameForSignal();
			break;
		case "PT":
			filterVals = ptRepository.findDistinctPtNameForSignal();
			break;
		case "Owner":
			filterVals = topicRepository.findDistinctOwnerNames();
			break;
		case "Assigned To":
			filterVals =  topicSignalDetectionAssignmentAssigneesRepository.getDetectionAssignedUsers();
			break;
		case "Date Range":
			break;
		case "Signal Confirmation":
			filterVals = topicRepository.findDistinctSignalConfirmationNames();
			break;
		case "Signal Source":
			filterVals = topicRepository.getSourceNames();
			break;
		case "Assessment Task Status":
			filterVals = AssessmentTaskStatusType.getAll();
			break;
		case "Final Disposition":
			filterVals = assessmentPlanRepository.getAssessmentRiskStatus();
			break;
		case "Risk Plan Action Status":
			filterVals = RiskTaskStatusType.getAll();
			break;
		case "Frequency":
			filterVals = RunFrequency.getAll();
			break;
		default:
			break;
		}
		return filterVals;
	}

	private List<String> getStatuses(Filters filters) {
		//List<FiltersStatus> statusList = filterStatusRepository.findByFiltersStatus();
		Set<FiltersStatus> statusList  =filters.getFiltersStatus();
		List<String> statuslist=null;
		if(!CollectionUtils.isEmpty(statusList))
		{
			statuslist = new ArrayList<>();
			for (FiltersStatus filtersStatus : statusList) {
				statuslist.add(filtersStatus.getName());
			}
		}
		return statuslist;
	}

}
