package com.deloitte.smt.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.AssessmentTaskStatusType;
import com.deloitte.smt.constant.RiskTaskStatusType;
import com.deloitte.smt.constant.RunFrequency;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicRepository;

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

    public SearchDto getFiltersForSignal(){
        SearchDto dto = new SearchDto();
        dto.setStatuses(Arrays.asList("New", SmtConstant.IN_PROGRESS.getDescription(), SmtConstant.COMPLETED.getDescription(), "Overdue"));
        dto.setIngredients(ingredientRepository.findDistinctIngredientNamesForSignal());
        dto.setProducts(productRepository.findDistinctProductNameForSignal());
        dto.setLicenses(licenseRepository.findDistinctLicenseNameForSignal());
        dto.setSocs(socRepository.findDistinctSocNameForSignal());
        dto.setHlgts(hlgtRepository.findDistinctHlgtNameForSignal());
        dto.setHlts(hltRepository.findDistinctHltNameForSignal());
        dto.setPts(ptRepository.findDistinctPtNameForSignal());
        dto.setSignalNames(topicRepository.findDistinctSignalName());
        dto.setSignalConfirmations(topicRepository.findDistinctSignalConfirmationNames());
        dto.addAssignees(topicRepository.getAssignedUsers());
        
        return dto;
    }

    public SearchDto getFiltersForSignalDetection(){
        SearchDto dto = new SearchDto();
        dto.setStatuses(Arrays.asList("New", SmtConstant.IN_PROGRESS.getDescription(), SmtConstant.COMPLETED.getDescription()));
        dto.setIngredients(ingredientRepository.findDistinctIngredientNamesForSignalDetection());
        dto.setProducts(productRepository.findDistinctProductNameForSignalDetection());
        dto.setLicenses(licenseRepository.findDistinctLicenseNameForSignalDetection());
        dto.setSocs(socRepository.findDistinctSocNameForSignalDetection());
        dto.setHlgts(hlgtRepository.findDistinctHlgtNameForSignalDetection());
        dto.setHlts(hltRepository.findDistinctHltNameForSignalDetection());
        dto.setPts(ptRepository.findDistinctPtNameForSignalDetection());
        dto.setFrequency(RunFrequency.getAll());
        return dto;
    }

    public SearchDto getAllFiltersForAssessmentPlan() {
        Set<Long> topicIds = assessmentPlanRepository.findAllSignalIds();
        SearchDto searchDto= getFilters(topicIds);
        searchDto.addAssignees(assessmentPlanRepository.getAssignedUsers());
        searchDto.setAssessmentTaskStatus(AssessmentTaskStatusType.getAll());
        return searchDto;
    }

    public SearchDto getAllFiltersForRiskPlan() {
        Set<Long> topicIds = riskPlanRepository.findAllSignalIds();
        SearchDto searchDto= getFilters(topicIds);
        searchDto.addAssignees(riskPlanRepository.getAssignedUsers());
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
}
