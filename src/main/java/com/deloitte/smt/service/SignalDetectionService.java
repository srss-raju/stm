package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.DateKeyType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.DenominatorForPoisson;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.QueryBuilder;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Smq;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.TopicSignalDetectionAssignmentAssignees;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.DenominatorForPoissonRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IncludeAERepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.QueryBuilderRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SmqRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicSignalDetectionAssignmentAssigneesRepository;
import com.deloitte.smt.util.SearchFilters;
import com.deloitte.smt.util.SignalUtil;
import com.deloitte.smt.util.SmtResponse;

/**
 * Created by RajeshKumar on 04-04-2017.
 */
@Transactional(rollbackOn = Exception.class)
@Service
public class SignalDetectionService {

	private static final Logger LOG = Logger.getLogger(SignalDetectionService.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	ExceptionBuilder exceptionBuilder;

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	private SocRepository socRepository;
	
	@Autowired
	SmqRepository smqRepository;

	@Autowired
	private HlgtRepository hlgtRepository;

	@Autowired
	private HltRepository hltRepository;

	@Autowired
	private PtRepository ptRepository;

	@Autowired
	private SignalDetectionRepository signalDetectionRepository;

	@Autowired
	private DenominatorForPoissonRepository denominatorForPoissonRepository;

	@Autowired
	private IncludeAERepository includeAERepository;

	@Autowired
	private QueryBuilderRepository queryBuilderRepository;
	
	@Autowired
	private TopicSignalDetectionAssignmentAssigneesRepository topicSignalDetectionAssignmentAssigneesRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SearchFilters searchFilters;

	public SignalDetection createOrUpdateSignalDetection(SignalDetection signalDetection) throws ApplicationException {
		try {
			if(signalDetection.getId()==null){
				Long signalDetectionExist = signalDetectionRepository.countByNameIgnoreCase(signalDetection.getName());
				if (signalDetectionExist > 0) {
					throw exceptionBuilder.buildException(ErrorType.DETECTION_NAME_DUPLICATE);
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
			List<TopicSignalDetectionAssignmentAssignees>  detectionAssigneesList = clone.getTopicSignalDetectionAssignmentAssignees();
			if(!CollectionUtils.isEmpty(detectionAssigneesList)){
				for(TopicSignalDetectionAssignmentAssignees assignee:detectionAssigneesList){
					assignee.setDetectionId(clone.getId());
				} 
			}
			signalDetection.setId(clone.getId());
			List<Ingredient> ingredients = signalDetection.getIngredients();
			if (!CollectionUtils.isEmpty(ingredients)) {
				for (Ingredient ingredient : ingredients) {
					ingredient.setDetectionId(signalDetection.getId());
					//ingredientRepository.deleteByDetectionId(signalDetection.getId());
				}
				ingredients = ingredientRepository.save(ingredients);

				saveProduct(signalDetection, ingredients);
				saveLicense(signalDetection, ingredients);
			}

			saveSoc(signalDetection);
			saveSmq(signalDetection);
			saveIncludeAE(signalDetection);
			saveDenominatorForPoisson(signalDetection);
			saveQueryBuilder(signalDetection);
			return signalDetection;
		} catch (ApplicationException ex) {
				throw new ApplicationException("Problem Creating Signal Detection",  ex);
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
				saveHlgt(signalDetection, soc);
				saveHlt(signalDetection, soc);
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
	 * @param soc
	 */
	private void saveHlt(SignalDetection signalDetection, Soc soc) {
		List<Hlt> hlts = soc.getHlts();
		if (!CollectionUtils.isEmpty(hlts)) {
			for (Hlt hlt : hlts) {
				hlt.setSocId(soc.getId());
				hlt.setDetectionId(signalDetection.getId());
			}
			hltRepository.save(hlts);
		}
	}

	/**
	 * @param signalDetection
	 * @param soc
	 */
	private void saveHlgt(SignalDetection signalDetection, Soc soc) {
		List<Hlgt> hlgts = soc.getHlgts();
		if (!CollectionUtils.isEmpty(hlgts)) {
			for (Hlgt hlgt : hlgts) {
				hlgt.setSocId(soc.getId());
				hlgt.setDetectionId(signalDetection.getId());
			}
			hlgtRepository.save(hlgts);
		}
	}

	/**
	 * @param signalDetection
	 * @param ingredient
	 */
	private void saveProduct(SignalDetection signalDetection, List<Ingredient> ingredients) {
		List<Product> products = null;
		if (!CollectionUtils.isEmpty(ingredients)) {
			for (Ingredient ingredient : ingredients) {
				products = ingredient.getProducts();
				if (!CollectionUtils.isEmpty(products)) {
					for (Product singleProduct : products) {
						singleProduct.setIngredientId(ingredient.getId());
						singleProduct.setDetectionId(signalDetection.getId());
						//productRepository.deleteByIngredientId(ingredient.getId());
					}
					productRepository.save(products);
				}
			}
		}
	}

	/**
	 * @param signalDetection
	 * @param ingredient
	 */
	private void saveLicense(SignalDetection signalDetection, List<Ingredient> ingredients) {
		if (!CollectionUtils.isEmpty(ingredients)) {
			for (Ingredient ingredient : ingredients) {

				List<License> licenses = ingredient.getLicenses();
				if (!CollectionUtils.isEmpty(licenses)) {
					for (License singleLicense : licenses) {
						singleLicense.setIngredientId(ingredient.getId());
						singleLicense.setDetectionId(signalDetection.getId());
					}
				//	licenseRepository.deleteByDetectionId(signalDetection.getId());
					licenseRepository.save(licenses);
				}
			}
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

	/**
	 * @param signalDetection
	 */
	private void saveDenominatorForPoisson(SignalDetection signalDetection) {
		List<DenominatorForPoisson> denominatorForPoissons = signalDetection.getDenominatorForPoisson();
		if (!CollectionUtils.isEmpty(denominatorForPoissons)) {
			for (DenominatorForPoisson dfp : denominatorForPoissons) {
				dfp.setDetectionId(signalDetection.getId());
			}
			denominatorForPoissonRepository.deleteByDetectionId(signalDetection.getId());
			denominatorForPoissonRepository.save(denominatorForPoissons);
		}
	}

	/**
	 * @param signalDetection
	 */
	private void saveQueryBuilder(SignalDetection signalDetection) {
		List<QueryBuilder> queryBuilder = signalDetection.getQueryBuilder();
		if (!CollectionUtils.isEmpty(queryBuilder)) {
			for (QueryBuilder query : queryBuilder) {
				query.setDetectionId(signalDetection.getId());
			}
			queryBuilderRepository.deleteByDetectionId(signalDetection.getId());
			queryBuilderRepository.save(queryBuilder);
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
		TopicSignalDetectionAssignmentAssignees assignee = topicSignalDetectionAssignmentAssigneesRepository.findOne(assigneeId);
		if (assignee == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		topicSignalDetectionAssignmentAssigneesRepository.delete(assignee);
	}

	public SignalDetection findById(Long id) throws ApplicationException {
		SignalDetection signalDetection = signalDetectionRepository.findOne(id);
		if (null == signalDetection) {
			throw new ApplicationException("Signal Detection not found with given Id :" + id);
		}
		addOtherInfoToSignalDetection(signalDetection);
		signalDetection.setTopicSignalDetectionAssignmentAssignees(topicSignalDetectionAssignmentAssigneesRepository.findByDetectionId(id));
		return signalDetection;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SmtResponse findAllForSearch(SearchDto searchDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();

		Root<SignalDetection> rootSignalDetection = criteriaQuery.from(SignalDetection.class);
		Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees = rootSignalDetection.join("topicSignalDetectionAssignmentAssignees", JoinType.LEFT); //left outer join

		if (null != searchDto) {
			Root<Ingredient> rootIngredient = criteriaQuery.from(Ingredient.class);
			List<Predicate> predicates = new ArrayList<>(10);
			predicates.add(criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootIngredient.get(SmtConstant.DETECTION_ID.getDescription())));

			addDescription(searchDto, criteriaBuilder, rootSignalDetection, predicates);
			addFrequency(searchDto, criteriaBuilder, rootSignalDetection, predicates);
			addIngredients(searchDto, criteriaBuilder, rootIngredient, predicates);
			addProducts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addLicenses(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addSocs(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addHlts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addHlgts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addPts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addCreatedOrLastRunDate(searchDto, criteriaBuilder, rootSignalDetection, predicates);
			/**TopicSignalValidationAssignmentAssignees **/
			addSearchKeys(searchDto, criteriaBuilder, joinDetectionAssignees,rootSignalDetection,predicates);
			addUserKey(searchDto, criteriaBuilder, joinDetectionAssignees,predicates);
			addOwners(searchDto, criteriaBuilder, rootSignalDetection,predicates);
			addOwnersUserGroupKeys(searchDto, criteriaBuilder, joinDetectionAssignees,rootSignalDetection,predicates);
			addOwnersUserKeys(searchDto, criteriaBuilder, joinDetectionAssignees,rootSignalDetection,predicates);
			addUserGroupKeysUserKeys(searchDto, criteriaBuilder, joinDetectionAssignees,predicates);
			addUserGroupKeys(searchDto, criteriaBuilder, joinDetectionAssignees,predicates);

			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			criteriaQuery.multiselect(rootSignalDetection).where(andPredicate)
					.orderBy(criteriaBuilder.desc(rootSignalDetection.get(SmtConstant.CREATED_DATE.getDescription()))).distinct(true);

		} else {
			criteriaQuery.multiselect(rootSignalDetection)
					.orderBy(criteriaBuilder.desc(rootSignalDetection.get(SmtConstant.CREATED_DATE.getDescription()))).distinct(true);
		}
		SmtResponse smtResponse=new SmtResponse();
		TypedQuery<SignalDetection> q = entityManager.createQuery(criteriaQuery);
		if (!CollectionUtils.isEmpty(q.getResultList())) {
			smtResponse.setTotalRecords(q.getResultList().size());
		}
		if(searchDto!= null && searchDto.getFetchSize() !=0 ){
			q.setFirstResult(searchDto.getFromRecord());
			q.setMaxResults(searchDto.getFetchSize());
			smtResponse.setFetchSize(searchDto.getFetchSize());
			smtResponse.setFromRecord(searchDto.getFromRecord());
		}
		smtResponse.setResult(q.getResultList());
		
		if (!CollectionUtils.isEmpty(smtResponse.getResult())) {
			List<SignalDetection> result = (List<SignalDetection>) smtResponse.getResult();
			for (SignalDetection signalDetection : result) {
				signalDetection.setDenominatorForPoisson(
						denominatorForPoissonRepository.findByDetectionId(signalDetection.getId()));
			}
		}
		return smtResponse;
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootSignalDetection
	 * @param predicates
	 */
	private void addCreatedOrLastRunDate(SearchDto searchDto, CriteriaBuilder criteriaBuilder,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (DateKeyType.searchIn(searchDto.getDateKey())) {
			if (searchDto.getDateKey().equalsIgnoreCase(DateKeyType.CREATED.name())) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(
						rootSignalDetection.get(SmtConstant.CREATED_DATE.getDescription()), searchDto.getStartDate()));

				if (null != searchDto.getEndDate()) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(
							rootSignalDetection.get(SmtConstant.CREATED_DATE.getDescription()),
							searchDto.getEndDate()));
				}
			} else if (searchDto.getDateKey().equalsIgnoreCase(DateKeyType.LASTRUN.name())) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(rootSignalDetection.get("lastRunDate"),
						searchDto.getStartDate()));

				if (null != searchDto.getEndDate()) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(rootSignalDetection.get("lastRunDate"),
							searchDto.getEndDate()));
				}

			} else if (searchDto.getDateKey().equalsIgnoreCase(DateKeyType.NEXTRUN.name())) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(rootSignalDetection.get("nextRunDate"),
						searchDto.getStartDate()));

				if (null != searchDto.getEndDate()) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(rootSignalDetection.get("nextRunDate"),
							searchDto.getEndDate()));
				}

			}
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param rootSignalDetection
	 * @param predicates
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addPts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getPts())) {
			Root<Pt> rootPt = criteriaQuery.from(Pt.class);
			Predicate signalDetectionPtEquals = criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootPt.get(SmtConstant.DETECTION_ID.getDescription()));

			Predicate hlgtNameIn = criteriaBuilder.isTrue(rootPt.get("ptName").in(searchDto.getPts()));

			predicates.add(signalDetectionPtEquals);
			predicates.add(hlgtNameIn);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param rootSignalDetection
	 * @param predicates
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addHlgts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getHlgts())) {
			Root<Hlgt> rootHlgt = criteriaQuery.from(Hlgt.class);
			Predicate signalDetectionHlgtEquals = criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootHlgt.get(SmtConstant.DETECTION_ID.getDescription()));

			Predicate hlgtNameIn = criteriaBuilder.isTrue(rootHlgt.get("hlgtName").in(searchDto.getHlgts()));

			predicates.add(signalDetectionHlgtEquals);
			predicates.add(hlgtNameIn);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param rootSignalDetection
	 * @param predicates
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addHlts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getHlts())) {
			Root<Hlt> rootHlt = criteriaQuery.from(Hlt.class);
			Predicate signalDetectionHltEquals = criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootHlt.get(SmtConstant.DETECTION_ID.getDescription()));

			Predicate hltNameIn = criteriaBuilder.isTrue(rootHlt.get("hltName").in(searchDto.getHlts()));

			predicates.add(signalDetectionHltEquals);
			predicates.add(hltNameIn);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param rootSignalDetection
	 * @param predicates
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSocs(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getSocs())) {
			Root<Soc> rootSoc = criteriaQuery.from(Soc.class);
			Predicate signalDetectionSocEquals = criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootSoc.get(SmtConstant.DETECTION_ID.getDescription()));

			Predicate socNameIn = criteriaBuilder.isTrue(rootSoc.get("socName").in(searchDto.getSocs()));

			predicates.add(signalDetectionSocEquals);
			predicates.add(socNameIn);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param rootIngredient
	 * @param predicates
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addLicenses(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
			Root<License> rootLicense = criteriaQuery.from(License.class);
			Predicate ingredientLicenseEquals = criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootLicense.get("ingredientId"));

			Predicate licenseNameIn = criteriaBuilder
					.isTrue(rootLicense.get("licenseName").in(searchDto.getLicenses()));

			predicates.add(ingredientLicenseEquals);
			predicates.add(licenseNameIn);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param criteriaQuery
	 * @param rootIngredient
	 * @param predicates
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addProducts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
			Root<Product> rootProduct = criteriaQuery.from(Product.class);
			Predicate ingredientProductEquals = criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootProduct.get("detectionId"));

			Predicate productNameIn = criteriaBuilder
					.isTrue(rootProduct.get("productName").in(searchDto.getProducts()));

			predicates.add(ingredientProductEquals);
			predicates.add(productNameIn);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootIngredient
	 * @param predicates
	 */
	private void addIngredients(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Ingredient> rootIngredient,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getIngredients())) {
			Predicate ingredientNameEquals = criteriaBuilder
					.isTrue(rootIngredient.get("ingredientName").in(searchDto.getIngredients()));
			predicates.add(ingredientNameEquals);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootSignalDetection
	 * @param predicates
	 */
	private void addFrequency(SearchDto searchDto, CriteriaBuilder criteriaBuilder,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getFrequency())) {
			predicates
					.add(criteriaBuilder.isTrue(rootSignalDetection.get("runFrequency").in(searchDto.getFrequency())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootSignalDetection
	 * @param predicates
	 */
	private void addDescription(SearchDto searchDto, CriteriaBuilder criteriaBuilder,
			Root<SignalDetection> rootSignalDetection, List<Predicate> predicates) {
		if (StringUtils.isNotBlank(searchDto.getDescription())) {
			predicates
					.add(criteriaBuilder.isTrue(rootSignalDetection.get("description").in(searchDto.getDescription())));
		}
	}

	private void addOtherInfoToSignalDetection(SignalDetection signalDetection) {
	
		List<Ingredient> ingredients = ingredientRepository.findByDetectionId(signalDetection.getId());
		List<Product> products = null;
		List<License> licenses = null;
		
		if (!CollectionUtils.isEmpty(ingredients)) {
			for (Ingredient ingredient : ingredients) {
				 products = productRepository.findByIngredientId(ingredient.getId());
				if (!CollectionUtils.isEmpty(products)) {
				ingredient.setProducts(products);
				}
				licenses = licenseRepository.findByIngredientId(ingredient.getId());
				if (!CollectionUtils.isEmpty(licenses)) {
				ingredient.setLicenses(licenses);
			}
			}
			signalDetection.setIngredients(ingredients);
		}
		signalDetection.setIngredients(ingredients);
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

		List<DenominatorForPoisson> denominatorForPoissonList = denominatorForPoissonRepository
				.findByDetectionId(signalDetection.getId());
		List<IncludeAE> includeAEList = includeAERepository.findByDetectionId(signalDetection.getId());
		signalDetection.setDenominatorForPoisson(denominatorForPoissonList);
		signalDetection.setIncludeAEs(includeAEList);
		signalDetection.setQueryBuilder(queryBuilderRepository.findByDetectionId(signalDetection.getId()));
	}

	/**
	 * @param socs
	 */
	private void setSocValues(List<Soc> socs) {
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setHlgts(hlgtRepository.findBySocId(soc.getId()));
				soc.setHlts(hltRepository.findBySocId(soc.getId()));
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
			LOG.info("Next Run Date  -->> " + createdDate);
		}
		signalDetection.setNextRunDates(nextRunDates);
	}
	
	

	/**
	 * 
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addUserGroupKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder,Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees, List<Predicate> predicates) {
		
		 if(searchFilters.ifUserGroupKey(searchDto)&& ! searchFilters.isOwnerUserKey(searchDto)){
			 predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(searchDto.getUserGroupKeys()))));
			  } 
		
			 }
	/**
     * 
     * @param searchDto
     * @param criteriaBuilder
     * @param assignmentAssignees
     * @param rootTopic
     * @param predicates
     */
    private void addSearchKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder, 
    		Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees, Root<SignalDetection> rootSignalDetection,List<Predicate> predicates){
    	 if(searchFilters.ifUserGroupKey(searchDto) && searchFilters.isOwnerUserKey(searchDto)){
    		 predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_KEY.getDescription()).in(searchDto.getUserKeys())),
					   criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(searchDto.getUserGroupKeys())),
					   criteriaBuilder.isTrue(rootSignalDetection.get(SmtConstant.OWNER.getDescription()).in(searchDto.getOwners())))));
			   
  	 }
    }
    /**
     * 
     * @param searchDto
     * @param criteriaBuilder
     * @param assignmentAssignees
     * @param rootTopic
     * @param predicates
     */
    private void addUserKey(SearchDto searchDto, CriteriaBuilder criteriaBuilder, 
    		Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees, List<Predicate> predicates){
    	 if(searchFilters.ifUserKey(searchDto)&& !searchFilters.isOwnerUserGRoupKey(searchDto)){
    		 predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_KEY.getDescription()).in(searchDto.getUserKeys()))));
    	 }
    }
    
    /**
     * 
     * @param searchDto
     * @param criteriaBuilder
     * @param assignmentAssignees
     * @param rootTopic
     * @param predicates
     */
    private void addOwners(SearchDto searchDto, CriteriaBuilder criteriaBuilder, 
    		Root<SignalDetection> rootSignalDetection,List<Predicate> predicates){
    	if(searchFilters.ifOwner(searchDto) && !searchFilters.isGroupKeyUserKey(searchDto)){
    		 predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(rootSignalDetection.get(SmtConstant.OWNER.getDescription()).in(searchDto.getOwners())),
					  criteriaBuilder.isTrue(rootSignalDetection.get(SmtConstant.OWNER.getDescription()).isNull())));
   	 }
    }
    
    /**
     * 
     * @param searchDto
     * @param criteriaBuilder
     * @param assignmentAssignees
     * @param rootTopic
     * @param predicates
     */
    private void addOwnersUserKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder, 
    		Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees, Root<SignalDetection> rootSignalDetection,List<Predicate> predicates){
    	if(searchFilters.isOwnerUserKey(searchDto)&& !searchFilters.ifUserGroupKey(searchDto)){
    		predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_KEY.getDescription()).in(searchDto.getUserKeys())),
					   criteriaBuilder.isTrue(rootSignalDetection.get(SmtConstant.OWNER.getDescription()).in(searchDto.getOwners()))));
   	 }
    }
    /**
     * 
     * @param searchDto
     * @param criteriaBuilder
     * @param assignmentAssignees
     * @param rootTopic
     * @param predicates
     */
    private void addOwnersUserGroupKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder, 
    		Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees, 
    		Root<SignalDetection> rootSignalDetection,List<Predicate> predicates){
    	 if(searchFilters.isOwnerUserGRoupKey(searchDto)&& !searchFilters.ifUserKey(searchDto)){
    		 predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(searchDto.getUserGroupKeys())),
					   criteriaBuilder.isTrue(rootSignalDetection.get(SmtConstant.OWNER.getDescription()).in(searchDto.getOwners()))));
    	 }
    }
    /**
     * 
     * @param searchDto
     * @param criteriaBuilder
     * @param assignmentAssignees
     * @param rootTopic
     * @param predicates
     */
    private void addUserGroupKeysUserKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder, 
    		Join<SignalDetection,TopicSignalDetectionAssignmentAssignees> joinDetectionAssignees, 
    		 List<Predicate> predicates){
    	 if(searchFilters.isGroupKeyUserKey(searchDto)&& !searchFilters.ifOwner(searchDto)){
    		 predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_KEY.getDescription()).in(searchDto.getUserKeys())),
					   criteriaBuilder.isTrue(joinDetectionAssignees.get(SmtConstant.USER_GROUP_KEY.getDescription()).in(searchDto.getUserGroupKeys()))));
    	 }
    }
}
