package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.dto.ProductSearchDTO;
import com.deloitte.smt.entity.ProductLevels;
import com.deloitte.smt.repository.ProductLevelRepository;

/**
 * 
 * @author shbondada
 *
 */
@Service
public class ProductHierarchyService {
	private static final Logger LOGGER = Logger.getLogger(ProductHierarchyService.class);
	@Autowired
	ProductMedraHierarchyDAO productMedraHierarchyDAO;

	@Autowired
	private ProductLevelRepository productLevelRepository;
	
	/**
	 * This method will fetch all the SOC's and corresponding hlgts hlts pts
	 * llts by soc description
	 * 
	 * @param socCode
	 * @param socName
	 * @return
	 */
	public List<ProductSearchDTO> getHierarchyByCode(MedraBrowserDTO medraBrowserDto) {
		List<ProductHierarchyDto> productHierarchyList;
		productHierarchyList = productMedraHierarchyDAO.findAllByActLvel(medraBrowserDto.getSearchValue(), medraBrowserDto.getSelectLevel(), medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		return responseMapper(productHierarchyList);
	}

	private List<ProductSearchDTO> responseMapper(List<ProductHierarchyDto> productHierarchyList) {
		List<ProductSearchDTO> productDtoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(productHierarchyList)) {
			List<ProductLevels> prl= productLevelRepository.findAll();
			Map<String,String> levelMap = null;
			if(!CollectionUtils.isEmpty(prl))
			{
				levelMap = new HashMap<>();
				for (ProductLevels productLevels : prl) {
					levelMap.put(productLevels.getKey(), productLevels.getValue());
				}
			}
			for (ProductHierarchyDto productHierarchyDto : productHierarchyList) {
				ProductSearchDTO loneSearchdto = addActLevelOnes(productHierarchyDto,levelMap);
				productDtoList.add(loneSearchdto);
				ProductSearchDTO ltwoSearchdto = addActLevelTwos(productHierarchyDto,levelMap);
				productDtoList.add(ltwoSearchdto);
				ProductSearchDTO lthreeSearchdto = addActLevelThrees(productHierarchyDto,levelMap);
				productDtoList.add(lthreeSearchdto);
				ProductSearchDTO lfourSearchdto = addActLevelFours(productHierarchyDto,levelMap);
				productDtoList.add(lfourSearchdto);
				ProductSearchDTO lfiveSearchdto = addActLevelFives(productHierarchyDto,levelMap);
				productDtoList.add(lfiveSearchdto);
				ProductSearchDTO rxNormSearchdto = addRxNorms(productHierarchyDto,levelMap);
				productDtoList.add(rxNormSearchdto);
			}
			
		}
		return productDtoList;

	}

	private ProductSearchDTO addActLevelOnes(ProductHierarchyDto productHierarchyDto, Map<String, String> levelMap) {
		ProductSearchDTO levelOneSearchdto = new ProductSearchDTO();
		levelOneSearchdto.setCategory(SmtConstant.ATC_LVL_1.getDescription());
		levelOneSearchdto.setCategoryCode(productHierarchyDto.getActLevelOneCode());
		levelOneSearchdto.setCategoryDesc(productHierarchyDto.getActLevelOneDesc());
		levelOneSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.ATC_LVL_1.getDescription())?levelMap.get(SmtConstant.ATC_LVL_1.getDescription()):null);
		return levelOneSearchdto;
	}

	private ProductSearchDTO addActLevelTwos(ProductHierarchyDto productHierarchyDto, Map<String, String> levelMap) {
		ProductSearchDTO levelTwoSearchdto = new ProductSearchDTO();
		levelTwoSearchdto.setCategory(SmtConstant.ATC_LVL_2.getDescription());
		levelTwoSearchdto.setCategoryCode(productHierarchyDto.getActLevelTwoCode());
		levelTwoSearchdto.setCategoryDesc(productHierarchyDto.getActLevelTwoDesc());
		levelTwoSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.ATC_LVL_2.getDescription())?levelMap.get(SmtConstant.ATC_LVL_2.getDescription()):null);
		return levelTwoSearchdto;
	}

	private ProductSearchDTO addActLevelThrees(ProductHierarchyDto productHierarchyDto, Map<String, String> levelMap) {
		ProductSearchDTO levelThreeSearchdto = new ProductSearchDTO();
		levelThreeSearchdto.setCategory(SmtConstant.ATC_LVL_3.getDescription());
		levelThreeSearchdto.setCategoryCode(productHierarchyDto.getActLevelThreeCode());
		levelThreeSearchdto.setCategoryDesc(productHierarchyDto.getActLevelThreeDesc());
		levelThreeSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.ATC_LVL_3.getDescription())?levelMap.get(SmtConstant.ATC_LVL_3.getDescription()):null);
		return levelThreeSearchdto;
	}

	private ProductSearchDTO addActLevelFours(ProductHierarchyDto productHierarchyDto, Map<String, String> levelMap) {
		ProductSearchDTO levelFourSearchdto = new ProductSearchDTO();
		levelFourSearchdto.setCategory(SmtConstant.ATC_LVL_4.getDescription());
		levelFourSearchdto.setCategoryCode(productHierarchyDto.getActLevelFourCode());
		levelFourSearchdto.setCategoryDesc(productHierarchyDto.getActLevelFourDesc());
		levelFourSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.ATC_LVL_4.getDescription())?levelMap.get(SmtConstant.ATC_LVL_4.getDescription()):null);
		return levelFourSearchdto;
	}

	private ProductSearchDTO addActLevelFives(ProductHierarchyDto productHierarchyDto, Map<String, String> levelMap) {
		ProductSearchDTO levelFiveSearchdto = new ProductSearchDTO();
		levelFiveSearchdto.setCategory(SmtConstant.ATC_LVL_5.getDescription());
		levelFiveSearchdto.setCategoryCode(productHierarchyDto.getActLevelFiveCode());
		levelFiveSearchdto.setCategoryDesc(productHierarchyDto.getActLevelFiveDesc());
		levelFiveSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.ATC_LVL_5.getDescription())?levelMap.get(SmtConstant.ATC_LVL_5.getDescription()):null);
		return levelFiveSearchdto;
	}
	
	private ProductSearchDTO addRxNorms(ProductHierarchyDto productHierarchyDto, Map<String, String> levelMap) {
		ProductSearchDTO rxNormSearchdto = new ProductSearchDTO();
		rxNormSearchdto.setCategory(SmtConstant.RXNORM.getDescription());
		rxNormSearchdto.setCategoryCode(productHierarchyDto.getActRxNormCode());
		rxNormSearchdto.setCategoryDesc(productHierarchyDto.getRxNormDesc());
		rxNormSearchdto.setCategoryName(levelMap.containsKey(SmtConstant.RXNORM.getDescription())?levelMap.get(SmtConstant.RXNORM.getDescription()):null);
		return rxNormSearchdto;
	}

	/**
	 * This method is invoked medra browser search by keyword
	 * 
	 * @param level
	 * @param searchText
	 * @return
	 */
	public List<ProductSearchDTO> getDetailsBySearchText(MedraBrowserDTO medraBrowserDto) {
		List<ProductSearchDTO> productSearchDtoList = new ArrayList<>();
		
		String level;
		if(null==medraBrowserDto.getScrollColumn()){
			level=medraBrowserDto.getSearchLevel();
		}
		else{
			level=medraBrowserDto.getScrollColumn();
		}
			
		if (null != level) {
			switch (level) {

			case "ALL":
				productSearchDtoList = searchAllByName(medraBrowserDto);
				break;

			case "ATC_LVL_1":
				productSearchDtoList = searchByLevelOneName(medraBrowserDto);
				break;

			case "ATC_LVL_2":
				productSearchDtoList = searchByLevelTwoName(medraBrowserDto);
				break;

			case "ATC_LVL_3":
				productSearchDtoList = searchByLevelThreeName(medraBrowserDto);
				break;

			case "ATC_LVL_4":
				productSearchDtoList = searchByLevelFourName(medraBrowserDto);
				break;

			case "ATC_LVL_5":
				productSearchDtoList = searchByLevelFiveName(medraBrowserDto);
				break;

			default:
				productSearchDtoList = searchByLevelOneName(medraBrowserDto);
				break;
			}
		}
		return productSearchDtoList;
	}

	private List<ProductSearchDTO> searchByLevelOneName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getProductByKeyName(SmtConstant.ATC_LVL_1.getDescription());
		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelOne(
				medraBrowserDto.getSearchValue(), medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_1.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return productSearchDTOList;
	}

	private String getProductByKeyName(String keyName) {
		ProductLevels prl= productLevelRepository.findByKey(keyName);
		if(prl!=null)
			return prl.getValue();
		else
			return null;
	}

	private List<ProductSearchDTO> searchByLevelTwoName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getProductByKeyName(SmtConstant.ATC_LVL_2.getDescription());
		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelTwo(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_2.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelThreeName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getProductByKeyName(SmtConstant.ATC_LVL_3.getDescription());
		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelThree(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_3.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelFourName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getProductByKeyName(SmtConstant.ATC_LVL_4.getDescription());
		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelFour(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_4.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelFiveName(MedraBrowserDTO medraBrowserDto) {
		String prlValue = getProductByKeyName(SmtConstant.ATC_LVL_5.getDescription());
		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelFive(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_5.getDescription());
				searchDto.setCategoryName(prlValue);
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchAllByName(MedraBrowserDTO medraBrowserDto) {
		List<ProductSearchDTO> productSearchDtoAllList = new ArrayList<>();
		List<ProductSearchDTO> productSearchDtoLevelOneList = searchByLevelOneName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelOne : productSearchDtoLevelOneList) {
			productSearchDtoAllList.add(productSearchDtoLevelOne);
		}
		List<ProductSearchDTO> productSearchDtoLevelTwoList = searchByLevelTwoName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelTwo : productSearchDtoLevelTwoList) {
			productSearchDtoAllList.add(productSearchDtoLevelTwo);
		}
		List<ProductSearchDTO> productSearchDtoLevelThreeList = searchByLevelThreeName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelThree : productSearchDtoLevelThreeList) {
			productSearchDtoAllList.add(productSearchDtoLevelThree);
		}
		List<ProductSearchDTO> productSearchDtoLevelFourList = searchByLevelFourName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelFour : productSearchDtoLevelFourList) {
			productSearchDtoAllList.add(productSearchDtoLevelFour);
		}
		List<ProductSearchDTO> productSearchDtoLevelFiveList = searchByLevelFiveName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelFive : productSearchDtoLevelFiveList) {
			productSearchDtoAllList.add(productSearchDtoLevelFive);
		}

		return productSearchDtoAllList;
	}

}
