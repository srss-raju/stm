package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.dao.ProductMedraHierarchyDAO;
import com.deloitte.smt.dto.ProductHierarchyDto;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignmentProduct;
import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;

/**
 * 
 * @author RKB
 *
 */
@Service
public class ProductHierarchyAdditionalService {
	private static final Logger LOGGER = Logger.getLogger(ProductHierarchyAdditionalService.class);
	@Autowired
	ProductMedraHierarchyDAO productMedraHierarchyDAO;

	

	public List<TopicProductAssignmentConfiguration> getProducts(Topic topic) {
		LOGGER.info("--Get Products Call--");
		List<ProductHierarchyDto> actLevels = productMedraHierarchyDAO.findActLevelsByProductKey(topic.getProductKey());
		List<TopicProductAssignmentConfiguration> products = null;
		if(!CollectionUtils.isEmpty(actLevels)){
			products = new ArrayList<>();
			ProductHierarchyDto record = actLevels.get(0);
			TopicProductAssignmentConfiguration topicProductAssignmentConfiguration = new TopicProductAssignmentConfiguration();
			List<TopicAssignmentProduct> recordValues = new ArrayList<>();
			StringBuilder recordKey = new StringBuilder();
			getRecordValues(record, recordValues, recordKey);
			topicProductAssignmentConfiguration.setRecordKey(recordKey.toString());
			topicProductAssignmentConfiguration.setRecordValues(recordValues);
			products.add(topicProductAssignmentConfiguration);
			setIngredient(topic, record);
		}
		return products;
	}



	private void setIngredient(Topic topic, ProductHierarchyDto record) {
		if(record.getFamilyDescription() != null){
			List<Ingredient> ingredients = new ArrayList<>();
			Ingredient ingredient = new Ingredient();
			ingredient.setIngredientName(record.getFamilyDescription());
			ingredients.add(ingredient);
			topic.setIngredients(ingredients);
		}
	}



	private void getRecordValues(ProductHierarchyDto record, List<TopicAssignmentProduct> recordValues, StringBuilder recordKey) {
		if(record.getActLevelOneCode() != null){
			TopicAssignmentProduct topicAssignmentProduct = new TopicAssignmentProduct();
			topicAssignmentProduct.setCategory("ATC_LVL_1");
			topicAssignmentProduct.setCategoryCode(record.getActLevelOneCode());
			topicAssignmentProduct.setCategoryDesc(record.getActLevelOneDesc());
			topicAssignmentProduct.setCategoryName("ANATOMICAL MAINGROUP");
			recordKey.append(record.getActLevelOneCode());
			recordValues.add(topicAssignmentProduct);
		}
		if(record.getActLevelTwoCode() != null){
			TopicAssignmentProduct topicAssignmentProduct = new TopicAssignmentProduct();
			recordKey.append("@#");
			recordKey.append(record.getActLevelTwoCode());
			topicAssignmentProduct.setCategory("ATC_LVL_2");
			topicAssignmentProduct.setCategoryCode(record.getActLevelTwoCode());
			topicAssignmentProduct.setCategoryDesc(record.getActLevelTwoDesc());
			topicAssignmentProduct.setCategoryName("THERAPEUTIC MAINGROUP");
			recordValues.add(topicAssignmentProduct);
		}
		if(record.getActLevelThreeCode() != null){
			TopicAssignmentProduct topicAssignmentProduct = new TopicAssignmentProduct();
			recordKey.append("@#");
			recordKey.append(record.getActLevelThreeCode());
			topicAssignmentProduct.setCategory("ATC_LVL_3");
			topicAssignmentProduct.setCategoryCode(record.getActLevelThreeCode());
			topicAssignmentProduct.setCategoryDesc(record.getActLevelThreeDesc());
			topicAssignmentProduct.setCategoryName("THERAPEUTIC PHARMACOLOGICAL SUBGROUP");
			recordValues.add(topicAssignmentProduct);
		} 
		if(record.getActLevelFourCode() != null){
			TopicAssignmentProduct topicAssignmentProduct = new TopicAssignmentProduct();
			recordKey.append("@#");
			recordKey.append(record.getActLevelFourCode());
			topicAssignmentProduct.setCategory("ATC_LVL_4");
			topicAssignmentProduct.setCategoryCode(record.getActLevelFourCode());
			topicAssignmentProduct.setCategoryDesc(record.getActLevelFourDesc());
			topicAssignmentProduct.setCategoryName("CHEMICAL THERAPEUTIC PHARMACOLOGICAL SUBGROUP");
			recordValues.add(topicAssignmentProduct);
		}
		if(record.getActLevelFiveCode() != null){
			TopicAssignmentProduct topicAssignmentProduct = new TopicAssignmentProduct();
			recordKey.append("@#");
			recordKey.append(record.getActLevelFiveCode());
			topicAssignmentProduct.setCategory("ATC_LVL_5");
			topicAssignmentProduct.setCategoryCode(record.getActLevelFiveCode());
			topicAssignmentProduct.setCategoryDesc(record.getActLevelFiveDesc());
			topicAssignmentProduct.setCategoryName("CHEMICAL SUBSTANCE");
			recordValues.add(topicAssignmentProduct);
		}
	}
}
