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
import com.deloitte.smt.entity.Soc;
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
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.util.SignalUtil;

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

	@PersistenceContext
	private EntityManager entityManager;

	public SignalDetection createOrUpdateSignalDetection(SignalDetection signalDetection) throws ApplicationException {
		try {

			Long signalDetectionExist = signalDetectionRepository.countByNameIgnoreCase(signalDetection.getName());
			if (signalDetectionExist > 0) {
				throw exceptionBuilder.buildException(ErrorType.DETECTION_NAME_DUPLICATE, null);
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
			signalDetection.setId(clone.getId());
			Ingredient ingredient = signalDetection.getIngredient();
			if (ingredient != null) {
				ingredient.setDetectionId(signalDetection.getId());
				ingredientRepository.deleteByDetectionId(signalDetection.getId());
				ingredient = ingredientRepository.save(ingredient);

				saveProduct(signalDetection, ingredient);
				saveLicense(signalDetection, ingredient);
			}

			saveSoc(signalDetection);
			saveIncludeAE(signalDetection);
			saveDenominatorForPoisson(signalDetection);
			saveQueryBuilder(signalDetection);

			return signalDetection;
		} catch (ApplicationException ex) {
				throw new ApplicationException("Problem Creating Signal Detection",  ex);
		}catch(Exception ex){
			throw new ApplicationException("Problem Creating Signal Detection", ex);
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
	private void saveProduct(SignalDetection signalDetection, Ingredient ingredient) {
		List<Product> products = ingredient.getProducts();
		if (!CollectionUtils.isEmpty(products)) {
			for (Product singleProduct : products) {
				singleProduct.setIngredientId(ingredient.getId());
				singleProduct.setDetectionId(signalDetection.getId());
			}
			productRepository.deleteByDetectionId(signalDetection.getId());
			productRepository.save(products);
		}
	}

	/**
	 * @param signalDetection
	 * @param ingredient
	 */
	private void saveLicense(SignalDetection signalDetection, Ingredient ingredient) {
		List<License> licenses = ingredient.getLicenses();
		if (!CollectionUtils.isEmpty(licenses)) {
			for (License singleLicense : licenses) {
				singleLicense.setIngredientId(ingredient.getId());
				singleLicense.setDetectionId(signalDetection.getId());
			}
			licenseRepository.deleteByDetectionId(signalDetection.getId());
			licenseRepository.save(licenses);
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

	public SignalDetection findById(Long id) throws ApplicationException {
		SignalDetection signalDetection = signalDetectionRepository.findOne(id);
		if (null == signalDetection) {
			throw new ApplicationException("Signal Detection not found with given Id :" + id);
		}
		addOtherInfoToSignalDetection(signalDetection);
		return signalDetection;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SignalDetection> findAllForSearch(SearchDto searchDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();

		Root<SignalDetection> rootSignalDetection = criteriaQuery.from(SignalDetection.class);

		if (null != searchDto) {
			Root<Ingredient> rootIngredient = criteriaQuery.from(Ingredient.class);
			List<Predicate> predicates = new ArrayList<>(10);
			predicates.add(criteriaBuilder.equal(rootSignalDetection.get("id"),
					rootIngredient.get(SmtConstant.DETECTION_ID.getDescription())));

			addDescription(searchDto, criteriaBuilder, rootSignalDetection, predicates);
			addFrequency(searchDto, criteriaBuilder, rootSignalDetection, predicates);
			addIngredients(searchDto, criteriaBuilder, rootIngredient, predicates);
			addProducts(searchDto, criteriaBuilder, criteriaQuery, rootIngredient, predicates);
			addLicenses(searchDto, criteriaBuilder, criteriaQuery, rootIngredient, predicates);
			addSocs(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addHlts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addHlgts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addPts(searchDto, criteriaBuilder, criteriaQuery, rootSignalDetection, predicates);
			addCreatedOrLastRunDate(searchDto, criteriaBuilder, rootSignalDetection, predicates);

			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			criteriaQuery.multiselect(rootSignalDetection).where(andPredicate)
					.orderBy(criteriaBuilder.desc(rootSignalDetection.get(SmtConstant.CREATED_DATE.getDescription())));

		} else {
			criteriaQuery.multiselect(rootSignalDetection)
					.orderBy(criteriaBuilder.desc(rootSignalDetection.get(SmtConstant.CREATED_DATE.getDescription())));
		}

		TypedQuery<SignalDetection> q = entityManager.createQuery(criteriaQuery);
		List<SignalDetection> results = q.getResultList();
		if (!CollectionUtils.isEmpty(results)) {
			for (SignalDetection signalDetection : results) {
				signalDetection.setDenominatorForPoisson(
						denominatorForPoissonRepository.findByDetectionId(signalDetection.getId()));
			}
		}
		return results;
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
			Root<Ingredient> rootIngredient, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
			Root<License> rootLicense = criteriaQuery.from(License.class);
			Predicate ingredientLicenseEquals = criteriaBuilder.equal(rootIngredient.get("id"),
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
			Root<Ingredient> rootIngredient, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
			Root<Product> rootProduct = criteriaQuery.from(Product.class);
			Predicate ingredientProductEquals = criteriaBuilder.equal(rootIngredient.get("id"),
					rootProduct.get("ingredientId"));

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
		Ingredient ingredient;
		ingredient = ingredientRepository.findByDetectionId(signalDetection.getId());
		List<Product> products;
		products = productRepository.findByDetectionId(signalDetection.getId());

		List<License> licenses;
		licenses = licenseRepository.findByDetectionId(signalDetection.getId());

		if (ingredient != null) {
			ingredient.setProducts(products);
			ingredient.setLicenses(licenses);
			signalDetection.setIngredient(ingredient);
		}

		List<Soc> socs;
		socs = socRepository.findByDetectionId(signalDetection.getId());
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setHlgts(hlgtRepository.findBySocId(soc.getId()));

				soc.setHlts(hltRepository.findBySocId(soc.getId()));
				soc.setPts(ptRepository.findBySocId(soc.getId()));
			}
		}
		signalDetection.setSocs(socs);

		List<DenominatorForPoisson> denominatorForPoissonList = denominatorForPoissonRepository
				.findByDetectionId(signalDetection.getId());
		List<IncludeAE> includeAEList = includeAERepository.findByDetectionId(signalDetection.getId());
		signalDetection.setDenominatorForPoisson(denominatorForPoissonList);
		signalDetection.setIncludeAEs(includeAEList);
		signalDetection.setQueryBuilder(queryBuilderRepository.findByDetectionId(signalDetection.getId()));
	}

	public List<SignalDetection> ganttDetections(List<SignalDetection> detections) {
		if (!CollectionUtils.isEmpty(detections)) {
			for (SignalDetection signalDetection : detections) {
				createGanttSignalDetections(signalDetection);
			}
		}
		return detections;
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
}
