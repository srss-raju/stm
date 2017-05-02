package com.deloitte.smt.service;

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
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        dto.setStatuses(Arrays.asList("New", "In Progress", "Completed"));
        dto.setIngredients(ingredientRepository.findDistinctIngredientNamesForSignal());
        dto.setProducts(productRepository.findDistinctProductNameForSignal());
        dto.setLicenses(licenseRepository.findDistinctLicenseNameForSignal());
        dto.setSocs(socRepository.findDistinctSocNameForSignal());
        dto.setHlgts(hlgtRepository.findDistinctHlgtNameForSignal());
        dto.setHlts(hltRepository.findDistinctHltNameForSignal());
        dto.setPts(ptRepository.findDistinctPtNameForSignal());
        dto.setSignalNames(topicRepository.findDistinctSignalName());
        dto.setSignalConfirmations(topicRepository.findDistinctSignalConfirmationNames());
        return dto;
    }

    public SearchDto getFiltersForSignalDetection(){
        SearchDto dto = new SearchDto();
        dto.setStatuses(Arrays.asList("New", "In Progress", "Completed"));
        dto.setIngredients(ingredientRepository.findDistinctIngredientNamesForSignalDetection());
        dto.setProducts(productRepository.findDistinctProductNameForSignalDetection());
        dto.setLicenses(licenseRepository.findDistinctLicenseNameForSignalDetection());
        dto.setSocs(socRepository.findDistinctSocNameForSignalDetection());
        dto.setHlgts(hlgtRepository.findDistinctHlgtNameForSignalDetection());
        dto.setHlts(hltRepository.findDistinctHltNameForSignalDetection());
        dto.setPts(ptRepository.findDistinctPtNameForSignalDetection());
        return dto;
    }

    public SearchDto getAllFiltersForAssessmentPlan() {
        Set<Long> topicIds = assessmentPlanRepository.findAllSignalIds();
        return getFilters(topicIds);
    }

    public SearchDto getAllFiltersForRiskPlan() {
        Set<Long> topicIds = riskPlanRepository.findAllSignalIds();
        return getFilters(topicIds);
    }

    public List<String> getIngredients() {
        return ingredientRepository.findDistinctIngredientNames();
    }

    private SearchDto getFilters(Set<Long> topicIds) {
        SearchDto searchDto = new SearchDto();
        searchDto.setStatuses(Arrays.asList("New", "In Progress", "Completed"));
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

    public boolean getSignalIdsForSearch(SearchDto searchDto, Set<Long> topicIds, boolean searchAll) {
        if(!CollectionUtils.isEmpty(searchDto.getLicenses())) {
            searchAll = false;
            List<License> licenses = licenseRepository.findAllByLicenseNameIn(searchDto.getLicenses());
            licenses.parallelStream().forEach(license -> {
                if(license.getTopicId() != null) {
                    topicIds.add(license.getTopicId());
                }
            });
        }
        if(!CollectionUtils.isEmpty(searchDto.getIngredients())) {
            searchAll = false;
            List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameIn(searchDto.getIngredients());
            ingredients.parallelStream().forEach(ingredient -> {
                if(ingredient.getTopicId() != null) {
                    topicIds.add(ingredient.getTopicId());
                }
            });
        }
        if(!CollectionUtils.isEmpty(searchDto.getProducts())) {
            searchAll = false;
            List<Product> products = productRepository.findAllByProductNameIn(searchDto.getProducts());
            products.parallelStream().forEach(product -> {
                if(product.getTopicId() != null) {
                    topicIds.add(product.getTopicId());
                }
            });
        }
        return searchAll;
    }
}
