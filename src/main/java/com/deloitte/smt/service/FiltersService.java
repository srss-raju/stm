package com.deloitte.smt.service;

import java.util.List;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.util.ServerResponseObject;

public interface FiltersService {

	List<FilterDTO> getFiltersByType(String type);

	ServerResponseObject getSignalDataByFilter(String type, SearchCriteriaDTO searchCriteria);

}
