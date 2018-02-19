package com.deloitte.smt.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.service.FiltersService;
import com.deloitte.smt.util.ServerResponseObject;

@Service
public class FiltersServiceImpl implements FiltersService {

	@Override
	public List<FilterDTO> getFiltersByType(String type) {
		List<FilterDTO> list = new ArrayList<>();
		FilterDTO dto = new FilterDTO();
		list.add(dto);
		return list;
	}

	@Override
	public ServerResponseObject getSignalDataByFilter(String type, SearchCriteriaDTO searchCriteria) {
		ServerResponseObject object = new ServerResponseObject();
		object.setMessage("Success");
		return object;
	}

	

}
