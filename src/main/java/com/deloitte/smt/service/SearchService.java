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

    public SearchDto getFilters(){
        SearchDto dto = new SearchDto();
        dto.setIngredients(ingredientRepository.findDistinctIngredientNames());
        dto.setProducts(productRepository.findDistinctProductName());
        dto.setLicenses(licenseRepository.findDistinctLicenseName());
        dto.setSocs(socRepository.findDistinctSocName());
        dto.setHlgts(hlgtRepository.findDistinctHlgtName());
        dto.setHlts(hltRepository.findDistinctHltName());
        dto.setPts(ptRepository.findDistinctPtName());
        return dto;
    }
}
