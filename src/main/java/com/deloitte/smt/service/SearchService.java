package com.deloitte.smt.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.AssessmentTaskStatusType;
import com.deloitte.smt.constant.RiskTaskStatusType;
import com.deloitte.smt.constant.RunFrequency;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicAssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalDetectionAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalValidationAssignmentAssigneesRepository;

/**
 * Created by myelleswarapu on 27-04-2017.
 */
@Service
public class SearchService {

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    LicenseRepository licenseRepository;
    @Autowired
    SocRepository socRepository;
    @Autowired
    HlgtRepository hlgtRepository;
    @Autowired
    HltRepository hltRepository;
    @Autowired
    PtRepository ptRepository;
    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;
    @Autowired
    RiskPlanRepository riskPlanRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    TopicSignalValidationAssignmentAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
    @Autowired
    TopicAssessmentAssignmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
    @Autowired
    TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
    @Autowired
    TopicSignalDetectionAssignmentAssigneesRepository topicSignalDetectionAssignmentAssigneesRepository;
    @Autowired
    SignalDetectionRepository signalDetectionRepository;

    public SearchDto getFiltersForSignal(String owner,Long userGroupKey){
        SearchDto dto = new SearchDto();
        dto.setStatuses(Arrays.asList("New", SmtConstant.IN_PROGRESS.getDescription(), SmtConstant.COMPLETED.getDescription(), "Overdue"));
        dto.setIngredients(ingredientRepository.findDistinctIngredientNamesForSignal(owner,userGroupKey));
        dto.setProducts(productRepository.findDistinctProductNameForSignal(owner,userGroupKey));
        dto.setLicenses(licenseRepository.findDistinctLicenseNameForSignal(owner,userGroupKey));
        dto.setSocs(socRepository.findDistinctSocNameForSignal(owner,userGroupKey));
        dto.setHlgts(hlgtRepository.findDistinctHlgtNameForSignal(owner,userGroupKey));
        dto.setHlts(hltRepository.findDistinctHltNameForSignal(owner,userGroupKey));
        dto.setPts(ptRepository.findDistinctPtNameForSignal(owner,userGroupKey));
        dto.setSignalNames(topicRepository.findDistinctSignalName(owner,userGroupKey));
        dto.setSignalConfirmations(topicRepository.findDistinctSignalConfirmationNames());
        dto.setAssignees(topicSignalValidationAssignmentAssigneesRepository.getSignalAssignedUsers());
        dto.setUserKeys(topicSignalValidationAssignmentAssigneesRepository.getAssignedUsers());
        dto.setUserGroupKeys(topicSignalValidationAssignmentAssigneesRepository.getAssignedGroups());
        dto.setSources(topicRepository.getSourceNames());
        dto.setOwners(topicRepository.findDistinctOwnerNames());
        return dto;
    }

    public SearchDto getFiltersForSignalDetection(String owner,Long userGroupKey){
        SearchDto dto = new SearchDto();
        dto.setStatuses(Arrays.asList("New", SmtConstant.IN_PROGRESS.getDescription(), SmtConstant.COMPLETED.getDescription()));
        dto.setIngredients(ingredientRepository.findDistinctIngredientNamesForSignalDetection(owner,userGroupKey));
        dto.setProducts(productRepository.findDistinctProductNameForSignalDetection(owner,userGroupKey));
        dto.setLicenses(licenseRepository.findDistinctLicenseNameForSignalDetection(owner,userGroupKey));
        dto.setSocs(socRepository.findDistinctSocNameForSignalDetection(owner,userGroupKey));
        dto.setHlgts(hlgtRepository.findDistinctHlgtNameForSignalDetection(owner,userGroupKey));
        dto.setHlts(hltRepository.findDistinctHltNameForSignalDetection(owner,userGroupKey));
        dto.setPts(ptRepository.findDistinctPtNameForSignalDetection(owner,userGroupKey));
        dto.setAssignees(topicSignalDetectionAssignmentAssigneesRepository.getDetectionAssignedUsers());
        dto.setUserKeys(topicSignalDetectionAssignmentAssigneesRepository.getAssignedUsers());
        dto.setUserGroupKeys(topicSignalDetectionAssignmentAssigneesRepository.getAssignedGroups());
        dto.setFrequency(RunFrequency.getAll());
        dto.setOwners(signalDetectionRepository.findDistinctOwnerOnDetection());
        return dto;
    }

    public SearchDto getAllFiltersForAssessmentPlan(String owner,Long userGroupKey){
        Set<Long> topicIds = assessmentPlanRepository.findAllSignalIds(owner,userGroupKey);
        SearchDto searchDto= getFilters(topicIds);
        searchDto.setAssignees(topicAssessmentAssignmentAssigneesRepository.getAssessmentAssignedUsers());
        searchDto.setUserKeys(topicAssessmentAssignmentAssigneesRepository.getAssignedUsers());
        searchDto.setUserGroupKeys(topicAssessmentAssignmentAssigneesRepository.getAssignedGroups());
        searchDto.setAssessmentTaskStatus(AssessmentTaskStatusType.getAll());
        searchDto.setFinalDispositions(assessmentPlanRepository.getAssessmentRiskStatus());
        searchDto.setOwners(assessmentPlanRepository.findOwnersOnAssessmentPlan());
        return searchDto;
    }

    public SearchDto getAllFiltersForRiskPlan(String owner,Long userGroupKey) {
        Set<Long> topicIds = riskPlanRepository.findAllSignalIds(owner,userGroupKey);
        SearchDto searchDto= getFilters(topicIds);
        searchDto.setAssignees(topicRiskPlanAssignmentAssigneesRepository.getRiskAssignedUsers());
        searchDto.setUserKeys(topicRiskPlanAssignmentAssigneesRepository.getAssignedUsers());
        searchDto.setUserGroupKeys(topicRiskPlanAssignmentAssigneesRepository.getAssignedGroups());
        searchDto.setOwners(riskPlanRepository.findOwnersOnRiskPlan());
        searchDto.setRiskTaskStatus(RiskTaskStatusType.getAll());
        return searchDto;
    }

    public List<String> getIngredients() {
        return ingredientRepository.findDistinctIngredientNames();
    }
    
    private SearchDto getFilters(Set<Long> topicIds) {
        SearchDto searchDto = new SearchDto();
        searchDto.setStatuses(Arrays.asList("New", SmtConstant.IN_PROGRESS.getDescription(), SmtConstant.COMPLETED.getDescription()));
        if(!CollectionUtils.isEmpty(topicIds)) {
            searchDto.setProducts(productRepository.findDistinctProductNamesTopicIdsIn(topicIds));
            searchDto.setIngredients(ingredientRepository.findDistinctIngredientNamesTopicIdsIn(topicIds));
            searchDto.setLicenses(licenseRepository.findDistinctLicenseNamesTopicIdsIn(topicIds));
            searchDto.setSocs(socRepository.findDistinctSocNamesTopicIdsIn(topicIds));
            searchDto.setHlgts(hlgtRepository.findDistinctHlgtNamesTopicIdsIn(topicIds));
            searchDto.setHlts(hltRepository.findDistinctHltNamesTopicIdsIn(topicIds));
            searchDto.setPts(ptRepository.findDistinctPtNamesTopicIdsIn(topicIds));
           
        }
        return searchDto;
    }
    
    
    public boolean getSignalIdsForSearch(SearchDto searchDto, List<Long> topicIds, boolean searchAll) {
    	boolean searchAllFlag = searchAll;
    	searchAllFlag = addLicenses(searchDto, topicIds, searchAllFlag);
    	searchAllFlag = addIngredients(searchDto, topicIds, searchAllFlag);
    	searchAllFlag = addProducts(searchDto, topicIds, searchAllFlag);
        Set<Long> allItems = new HashSet<>();
        Set<Long> duplicates = topicIds.parallelStream()
                .filter(n -> !allItems.add(n))
                .collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(duplicates)) {
            topicIds.clear();
            topicIds.addAll(duplicates);
        }
        return searchAllFlag;
    }

	/**
	 * @param searchDto
	 * @param topicIds
	 * @param searchAll
	 * @return
	 */
	private boolean addProducts(SearchDto searchDto, List<Long> topicIds, boolean searchAll) {
		boolean searchAllFlag = searchAll;
		if(searchDto!=null && !CollectionUtils.isEmpty(searchDto.getProducts())) {
			searchAllFlag = false;
            List<Product> products = productRepository.findAllByProductNameIn(searchDto.getProducts());
            products.parallelStream().forEach(product -> {
                if(product.getTopicId() != null) {
                    topicIds.add(product.getTopicId());
                }
            });
        }
		return searchAllFlag;
	}

	/**
	 * @param searchDto
	 * @param topicIds
	 * @param searchAll
	 * @return
	 */
	private boolean addIngredients(SearchDto searchDto, List<Long> topicIds, boolean searchAll) {
		boolean searchAllFlag = searchAll;
		if(searchDto!=null && !CollectionUtils.isEmpty(searchDto.getIngredients())) {
			searchAllFlag = false;
            List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameIn(searchDto.getIngredients());
            ingredients.parallelStream().forEach(ingredient -> {
                if(ingredient.getTopicId() != null) {
                    topicIds.add(ingredient.getTopicId());
                }
            });
        }
		return searchAllFlag;
	}
	
	/**
	 * @param searchDto
	 * @param topicIds
	 * @param searchAll
	 * @return
	 */
	private boolean addLicenses(SearchDto searchDto, List<Long> topicIds, boolean searchAll) {
		boolean searchAllFlag = searchAll;
		if(searchDto!=null && !CollectionUtils.isEmpty(searchDto.getLicenses())) {
			searchAllFlag = false;
            List<License> licenses = licenseRepository.findAllByLicenseNameIn(searchDto.getLicenses());
            licenses.parallelStream().forEach(license -> {
                if(license.getTopicId() != null) {
                    topicIds.add(license.getTopicId());
                }
            });
        }
		return searchAllFlag;
	}
}
