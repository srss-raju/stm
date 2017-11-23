package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.dto.ProductSearchDTO;

/**
 * 
 * @author shbondada
 *
 */
@Service
public class ProductHierarchyService {

	@Autowired
	ProductMedraHierarchyDAO productMedraHierarchyDAO;

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
		String level = getLevel(medraBrowserDto);

		productHierarchyList = productMedraHierarchyDAO.findAllByActLvel(medraBrowserDto.getSearchValue(), level, medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		return responseMapper(productHierarchyList);
	}

	private List<ProductSearchDTO> responseMapper(List<ProductHierarchyDto> productHierarchyList) {

		List<ProductSearchDTO> productDtoList = new ArrayList<>();

		if (!CollectionUtils.isEmpty(productHierarchyList)) {
			for (ProductHierarchyDto productHierarchyDto : productHierarchyList) {
				
				ProductSearchDTO loneSearchdto = addActLevelOnes(productHierarchyDto);
				productDtoList.add(loneSearchdto);
				ProductSearchDTO ltwoSearchdto = addActLevelTwos(productHierarchyDto);
				productDtoList.add(ltwoSearchdto);
				ProductSearchDTO lthreeSearchdto = addActLevelThrees(productHierarchyDto);
				productDtoList.add(lthreeSearchdto);
				ProductSearchDTO lfourSearchdto = addActLevelFours(productHierarchyDto);
				productDtoList.add(lfourSearchdto);
				ProductSearchDTO lfiveSearchdto = addActLevelFives(productHierarchyDto);
				productDtoList.add(lfiveSearchdto);
				ProductSearchDTO rxNormSearchdto = addRxNorms(productHierarchyDto);
				productDtoList.add(rxNormSearchdto);
			}
		}
		return productDtoList;

	}

	private ProductSearchDTO addActLevelOnes(ProductHierarchyDto productHierarchyDto) {
		ProductSearchDTO levelOneSearchdto = new ProductSearchDTO();
		levelOneSearchdto.setCategory(SmtConstant.ATC_LVL_1.getDescription());
		levelOneSearchdto.setCategoryCode(productHierarchyDto.getActLevelOneCode());
		levelOneSearchdto.setCategoryDesc(productHierarchyDto.getActLevelOneDesc());
		return levelOneSearchdto;
	}

	private ProductSearchDTO addActLevelTwos(ProductHierarchyDto productHierarchyDto) {
		ProductSearchDTO levelTwoSearchdto = new ProductSearchDTO();
		levelTwoSearchdto.setCategory(SmtConstant.ATC_LVL_2.getDescription());
		levelTwoSearchdto.setCategoryCode(productHierarchyDto.getActLevelTwoCode());
		levelTwoSearchdto.setCategoryDesc(productHierarchyDto.getActLevelTwoDesc());
		return levelTwoSearchdto;
	}

	private ProductSearchDTO addActLevelThrees(ProductHierarchyDto productHierarchyDto) {
		ProductSearchDTO levelThreeSearchdto = new ProductSearchDTO();
		levelThreeSearchdto.setCategory(SmtConstant.ATC_LVL_3.getDescription());
		levelThreeSearchdto.setCategoryCode(productHierarchyDto.getActLevelThreeCode());
		levelThreeSearchdto.setCategoryDesc(productHierarchyDto.getActLevelThreeDesc());
		return levelThreeSearchdto;
	}

	private ProductSearchDTO addActLevelFours(ProductHierarchyDto productHierarchyDto) {
		ProductSearchDTO levelFourSearchdto = new ProductSearchDTO();
		levelFourSearchdto.setCategory(SmtConstant.ATC_LVL_4.getDescription());
		levelFourSearchdto.setCategoryCode(productHierarchyDto.getActLevelFourCode());
		levelFourSearchdto.setCategoryDesc(productHierarchyDto.getActLevelFourDesc());
		return levelFourSearchdto;
	}

	private ProductSearchDTO addActLevelFives(ProductHierarchyDto productHierarchyDto) {
		ProductSearchDTO levelFiveSearchdto = new ProductSearchDTO();
		levelFiveSearchdto.setCategory(SmtConstant.ATC_LVL_5.getDescription());
		levelFiveSearchdto.setCategoryCode(productHierarchyDto.getActLevelFiveCode());
		levelFiveSearchdto.setCategoryDesc(productHierarchyDto.getActLevelFiveDesc());
		return levelFiveSearchdto;
	}
	
	private ProductSearchDTO addRxNorms(ProductHierarchyDto productHierarchyDto) {
		ProductSearchDTO rxNormSearchdto = new ProductSearchDTO();
		rxNormSearchdto.setCategory(SmtConstant.RXNORM.getDescription());
		rxNormSearchdto.setCategoryCode(productHierarchyDto.getActRxNormCode());
		rxNormSearchdto.setCategoryDesc(productHierarchyDto.getRxNormDesc());
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

		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelOne(
				medraBrowserDto.getSearchValue(), medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_1.getDescription());
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelTwoName(MedraBrowserDTO medraBrowserDto) {

		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelTwo(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_2.getDescription());
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelThreeName(MedraBrowserDTO medraBrowserDto) {

		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelThree(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_3.getDescription());
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelFourName(MedraBrowserDTO medraBrowserDto) {

		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelFour(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_4.getDescription());
			}
		}
		return productSearchDTOList;
	}

	private List<ProductSearchDTO> searchByLevelFiveName(MedraBrowserDTO medraBrowserDto) {

		List<ProductSearchDTO> productSearchDTOList = productMedraHierarchyDAO.findByActLvelFive(medraBrowserDto.getSearchValue(),
				medraBrowserDto.getScrollOffset(), medraBrowserDto.getScrollCount());
		if (!CollectionUtils.isEmpty(productSearchDTOList)) {
			for (ProductSearchDTO searchDto : productSearchDTOList) {
				searchDto.setCategory(SmtConstant.ATC_LVL_5.getDescription());
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
			productSearchDtoLevelTwoList.add(productSearchDtoLevelTwo);
		}
		List<ProductSearchDTO> productSearchDtoLevelThreeList = searchByLevelThreeName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelThree : productSearchDtoLevelThreeList) {
			productSearchDtoLevelThreeList.add(productSearchDtoLevelThree);
		}
		List<ProductSearchDTO> productSearchDtoLevelFourList = searchByLevelFourName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelFour : productSearchDtoLevelFourList) {
			productSearchDtoLevelFourList.add(productSearchDtoLevelFour);
		}
		List<ProductSearchDTO> productSearchDtoLevelFiveList = searchByLevelFiveName(medraBrowserDto);
		for (ProductSearchDTO productSearchDtoLevelFive : productSearchDtoLevelFiveList) {
			productSearchDtoLevelFourList.add(productSearchDtoLevelFive);
		}

		return productSearchDtoAllList;
	}

	private String getLevel(MedraBrowserDTO medraBrowserDto) {
		String level = null;

		if (null != medraBrowserDto.getSearchLevel()) {
			level = medraBrowserDto.getSearchLevel();
		} else if (null != medraBrowserDto.getSelectLevel()) {
			level = medraBrowserDto.getSelectLevel();
		}
		return level;
	}

}
