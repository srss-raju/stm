package com.deloitte.smt.service;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.SocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
}
