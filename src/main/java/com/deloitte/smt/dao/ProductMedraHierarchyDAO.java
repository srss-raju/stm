package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.dto.ProductSearchDTO;

public interface ProductMedraHierarchyDAO {
	
	List<ProductHierarchyDto> findAllByActLvel(String code,String columnName,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelOne(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelTwo(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelThree(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelFour(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelFive(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByRxNorm(String searchText,int scrollOffset,int scrollCount);

	List<Integer> getProducEventKeys(String query);

	List<ProductHierarchyDto> findActLevelsByIngredient(List<String> ingredientNames);


}
