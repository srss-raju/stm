package com.deloitte.smt.dao;

import java.util.List;

import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.dto.ProductSearchDTO;

public interface ProductMedraHierarchyDAO {
	
	List<ProductHierarchyDto> findAllByActLvel(List<String> codes,String columnName,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelOne(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelTwo(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelThree(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelFour(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByActLvelFive(String searchText,int scrollOffset,int scrollCount);
	
	List<ProductSearchDTO> findByRxNorm(String searchText,int scrollOffset,int scrollCount);

	List<Integer> getProducEventKeys(String query);

	List<ProductHierarchyDto> findActLevelsByIngredient(List<String> ingredientNames);

	List<String> getProductKey(String query);

	List<ProductHierarchyDto> findActLevelsByProductKey(String productKey);
	
	List<String> findAtcDescByAtcLevelCode(MedraBrowserDTO medraBrowserDto);

	List<String> findAtcCodesByAtcLevelDesc(List<String> desc, String columnName, String codeColumnName);

	List<String> findProductByAtcLevels(String query);

}
