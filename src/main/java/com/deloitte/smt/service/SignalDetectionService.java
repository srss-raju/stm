package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.DetectionAssignees;
import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Smq;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicConditionValues;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.entity.TopicProductValues;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.DetectionAssigneesRepository;
import com.deloitte.smt.repository.IncludeAERepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SmqRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicConditionRepository;
import com.deloitte.smt.repository.TopicConditionValuesRepository;
import com.deloitte.smt.repository.TopicProductRepository;
import com.deloitte.smt.repository.TopicProductValuesRepository;
import com.deloitte.smt.util.SignalUtil;
import com.deloitte.smt.util.SmtResponse;

/**
 * Created by RajeshKumar on 04-04-2017.
 */
@Transactional(rollbackOn = Exception.class)
@Service
public class SignalDetectionService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	MessageSource messageSource;

	@Autowired
	ExceptionBuilder exceptionBuilder;

	@Autowired
	private SocRepository socRepository;
	
	@Autowired
	SmqRepository smqRepository;

	@Autowired
	private PtRepository ptRepository;

	@Autowired
	private SignalDetectionRepository signalDetectionRepository;

	@Autowired
	private IncludeAERepository includeAERepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	TopicConditionRepository topicSocAssignmentConfigurationRepository;
	@Autowired
	TopicProductRepository topicProductAssignmentConfigurationRepository;
	
	@Autowired
	TopicConditionValuesRepository topicAssignmentConditionRepository;
	
	@Autowired
	TopicProductValuesRepository topicAssignmentProductRepository;

		@Autowired
		DetectionAssigneesRepository detectionAssigneesRepository;
	public SignalDetection createOrUpdateSignalDetection(SignalDetection signalDetection) throws ApplicationException {
		try {
			if(signalDetection.getId()==null){
				Long signalDetectionExist = signalDetectionRepository.countByNameIgnoreCase(signalDetection.getName());
				if (signalDetectionExist > 0) {
					throw exceptionBuilder.buildException(ErrorType.DETECTION_NAME_DUPLICATE, null);
				}
			}

			Calendar c = Calendar.getInstance();
			if (signalDetection.getId() == null) {
				signalDetection.setCreatedDate(c.getTime());
				signalDetection.setLastModifiedDate(c.getTime());
			} else {
				signalDetection.setLastModifiedDate(c.getTime());
			}
			signalDetection.setNextRunDate(
					SignalUtil.getNextRunDate(signalDetection.getRunFrequency(), signalDetection.getCreatedDate()));
			SignalDetection clone = signalDetection;
			clone = signalDetectionRepository.save(clone);
			saveProductsAndConditions(signalDetection, clone);
			/*List<TopicSignalDetectionAssignmentAssignees>  detectionAssigneesList = clone.getTopicSignalDetectionAssignmentAssignees();
			if(!CollectionUtils.isEmpty(detectionAssigneesList)){
				for(TopicSignalDetectionAssignmentAssignees assignee:detectionAssigneesList){
					assignee.setDetectionId(clone.getId());
				} 
			}*/
			signalDetection.setId(clone.getId());
			saveSoc(signalDetection);
			saveSmq(signalDetection);
			saveIncludeAE(signalDetection);
			return signalDetection;
		} catch (ApplicationException ex) {
				throw new ApplicationException("Problem Creating Signal Detection",  ex);
		}
	}
	
	private void saveProductsAndConditions(SignalDetection signalDetection, SignalDetection signalDetectionUpdated) {
		saveConditions(signalDetection, signalDetectionUpdated);
		saveProducts(signalDetection, signalDetectionUpdated);
	}
	
	
	private void saveProducts(SignalDetection signalDetection, SignalDetection signalDetectionUpdated) {
		if(!CollectionUtils.isEmpty(signalDetection.getProducts())){
			for(TopicProduct productConfig : signalDetection.getProducts()){
				productConfig.setDetectionId(signalDetectionUpdated.getId());
			}
			List<TopicProduct> updatedProductList = topicProductAssignmentConfigurationRepository.save(signalDetection.getProducts());
			for(TopicProduct updatedProductConfig : updatedProductList){
				saveAssignmentProduct(updatedProductConfig);
			}
		}
	}

	private void saveAssignmentProduct(TopicProduct updatedProductConfig) {
		if(!CollectionUtils.isEmpty(updatedProductConfig.getRecordValues())){
			for(TopicProductValues record : updatedProductConfig.getRecordValues()){
				record.setTopicProductAssignmentConfigurationId(updatedProductConfig.getId());
			}
			topicAssignmentProductRepository.save(updatedProductConfig.getRecordValues());
		}
	}

	private void saveConditions(SignalDetection signalDetection, SignalDetection signalDetectionUpdated) {
		if(!CollectionUtils.isEmpty(signalDetection.getConditions())){
			for(TopicCondition socConfig : signalDetection.getConditions()){
				socConfig.setDetectionId(signalDetectionUpdated.getId());
			}
			List<TopicCondition> updatedConditionList = topicSocAssignmentConfigurationRepository.save(signalDetection.getConditions());
			for(TopicCondition updateConditionConfig : updatedConditionList){
				savecAssignmentCondition(updateConditionConfig);
			}
		}
	}
	
	private void savecAssignmentCondition(TopicCondition updateConditionConfig) {
		if(!CollectionUtils.isEmpty(updateConditionConfig.getRecordValues())){
			for(TopicConditionValues record : updateConditionConfig.getRecordValues()){
				record.setTopicSocAssignmentConfigurationId(updateConditionConfig.getId());
			}
			topicAssignmentConditionRepository.save(updateConditionConfig.getRecordValues());
		}
	}

	/**
	 * @param signalDetection
	 */
	private void saveSoc(SignalDetection signalDetection) {
		List<Soc> socs = signalDetection.getSocs();
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setDetectionId(signalDetection.getId());
			}
			socs = socRepository.save(socs);
			for (Soc soc : socs) {
				savePt(signalDetection, soc);
			}
		}
	}
	
	/**
	 * @param signalDetection
	 */
	private void saveSmq(SignalDetection signalDetection) {
		List<Smq> smqs = signalDetection.getSmqs();
		if (!CollectionUtils.isEmpty(smqs)) {
			for (Smq smq : smqs) {
				smq.setDetectionId(signalDetection.getId());
			}
			smqs = smqRepository.save(smqs);
			for (Smq smq : smqs) {
				saveSmqPt(signalDetection, smq);
			}
		}
	}

	private void saveSmqPt(SignalDetection signalDetection, Smq smq) {
		List<Pt> pts = smq.getPts();
		if (!CollectionUtils.isEmpty(pts)) {
			for (Pt pt : pts) {
				pt.setSmqId(smq.getId());
				pt.setDetectionId(signalDetection.getId());
			}
			ptRepository.save(pts);
		}
	}

	/**
	 * @param signalDetection
	 * @param soc
	 */
	private void savePt(SignalDetection signalDetection, Soc soc) {
		List<Pt> pts = soc.getPts();
		if (!CollectionUtils.isEmpty(pts)) {
			for (Pt pt : pts) {
				pt.setSocId(soc.getId());
				pt.setDetectionId(signalDetection.getId());
			}
			ptRepository.save(pts);
		}
	}
	
	/**
	 * @param signalDetection
	 */
	private void saveIncludeAE(SignalDetection signalDetection) {
		List<IncludeAE> includeAEs = signalDetection.getIncludeAEs();
		if (!CollectionUtils.isEmpty(includeAEs)) {
			for (IncludeAE ae : includeAEs) {
				ae.setDetectionId(signalDetection.getId());
			}
			includeAERepository.deleteByDetectionId(signalDetection.getId());
			includeAERepository.save(includeAEs);
		}
	}


	public void delete(Long signalDetectionId) throws ApplicationException {
		SignalDetection signalDetection = signalDetectionRepository.findOne(signalDetectionId);
		if (signalDetection == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		signalDetectionRepository.delete(signalDetection);
	}
	
	public void deleteByAssigneeId(Long assigneeId) throws ApplicationException {
		DetectionAssignees assignee = detectionAssigneesRepository.findOne(assigneeId);
		if (assignee == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		detectionAssigneesRepository.delete(assignee);
	}

	private void setTopicConditionsAndProducts(SignalDetection signalDetection) {
		List<TopicCondition> socList = topicSocAssignmentConfigurationRepository.findByDetectionId(signalDetection.getId());
		List<TopicProduct> productList = topicProductAssignmentConfigurationRepository.findByDetectionId(signalDetection.getId());
		
		if(!CollectionUtils.isEmpty(socList)){
			for(TopicCondition socConfig:socList){
				socConfig.setRecordValues(topicAssignmentConditionRepository.findByTopicSocAssignmentConfigurationId(socConfig.getId()));
			}
		}
		
		if(!CollectionUtils.isEmpty(productList)){
			for(TopicProduct productConfig:productList){
				productConfig.setRecordValues(topicAssignmentProductRepository.findByTopicProductAssignmentConfigurationId(productConfig.getId()));
			}
		}
		signalDetection.setConditions(socList);
		signalDetection.setProducts(productList);
	}

	private void addOtherInfoToSignalDetection(SignalDetection signalDetection) {
	
		List<Soc> socs;
		socs = socRepository.findByDetectionId(signalDetection.getId());
		setSocValues(socs);
		signalDetection.setSocs(socs);
		
		List<Smq> smqs;
		smqs = smqRepository.findByDetectionId(signalDetection.getId());
		if (!CollectionUtils.isEmpty(smqs)) {
			for (Smq smq : smqs) {
				smq.setPts(ptRepository.findBySmqId(smq.getId()));
			}
		}
		signalDetection.setSmqs(smqs);
	}

	/**
	 * @param socs
	 */
	private void setSocValues(List<Soc> socs) {
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setPts(ptRepository.findBySocId(soc.getId()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public SmtResponse ganttDetections(SmtResponse smtResponse) {
		List<SignalDetection> detections = null;
		if (!CollectionUtils.isEmpty(smtResponse.getResult())) {
			detections = (List<SignalDetection>) smtResponse.getResult();
		}
		if (!CollectionUtils.isEmpty(detections)) {
			for (SignalDetection signalDetection : detections) {
				createGanttSignalDetections(signalDetection);
			}
		}
		return smtResponse;
	}

	private void createGanttSignalDetections(SignalDetection signalDetection) {
		List<Date> nextRunDates = new ArrayList<>();
		Date createdDate = signalDetection.getCreatedDate();
		for (int i = 0; i < Integer.parseInt(signalDetection.getWindowType()); i++) {
			createdDate = SignalUtil.getNextRunDate(signalDetection.getRunFrequency(), createdDate);
			nextRunDates.add(createdDate);
			logger.info("Next Run Date  -->> " + createdDate);
		}
		signalDetection.setNextRunDates(nextRunDates);
	}
	
	public SignalDetection findById(Long id) throws ApplicationException {
		SignalDetection signalDetection = signalDetectionRepository.findOne(id);
		if (null == signalDetection) {
			throw new ApplicationException("Signal Detection not found with given Id :" + id);
		}
		addOtherInfoToSignalDetection(signalDetection);
		setTopicConditionsAndProducts(signalDetection);
		return signalDetection;
	}
	
	
}
