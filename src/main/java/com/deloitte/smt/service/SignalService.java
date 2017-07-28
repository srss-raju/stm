package com.deloitte.smt.service;

import com.deloitte.smt.entity.SignalConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SignalConfigurationType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.NonSignalRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalConfigurationRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RKB on 04-04-2017.
 */
@Transactional
@Service
public class SignalService {

	private static final Logger LOG = Logger.getLogger(SignalService.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder exceptionBuilder;

	@Autowired
	private TaskService taskService;

	@Autowired
	private SignalMatchService signalMatchService;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TopicRepository topicRepository;
	@Autowired
	private IngredientRepository ingredientRepository;
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private LicenseRepository licenseRepository;
	@Autowired
	TaskInstRepository taskInstRepository;
	@Autowired
	SignalURLRepository signalURLRepository;

	@Autowired
	CaseService caseService;

	@Autowired
	AssessmentPlanRepository assessmentPlanRepository;

	@Autowired
	RiskPlanRepository riskPlanRepository;

	@Autowired
	AttachmentService attachmentService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SocRepository socRepository;

	@Autowired
	private HlgtRepository hlgtRepository;

	@Autowired
	private HltRepository hltRepository;

	@Autowired
	private PtRepository ptRepository;

	@Autowired
	private AssessmentActionRepository assessmentActionRepository;

	@Autowired
	private TaskTemplateRepository taskTemplateRepository;

	@Autowired
	private TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;

	@Autowired
	SearchService searchService;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	SignalConfigurationRepository signalConfigurationRepository;

	@Autowired
	NonSignalRepository nonSignalRepository;

	@Autowired
	private AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@Autowired
	CommentsRepository commentsRepository;

	public NonSignal createOrupdateNonSignal(NonSignal nonSignal) {
		Calendar c = Calendar.getInstance();

		if (nonSignal.getId() == null) {
			nonSignal.setCreatedDate(c.getTime());
		}

		nonSignal.setLastModifiedDate(c.getTime());
		List<NonSignal> nonSignals = nonSignalRepository.findAll();
		boolean isNonSignalMatched = nonSignals.stream()
				.anyMatch(ns -> ns.getProductKey().equals(nonSignal.getProductKey())
						&& ns.getPtDesc().equals(nonSignal.getPtDesc()) && ns.getName().equals(nonSignal.getName()));

		if (isNonSignalMatched) {
			return null;
		} else {
			return nonSignalRepository.save(nonSignal);
		}

	}

	public Topic findById(Long topicId) throws ApplicationException {
		Topic topic = topicRepository.findOne(topicId);
		if (topic == null) {
			throw new ApplicationException("Signal not found with the given Id :" + topicId);
		}
		if (null == topic.getSignalValidation()) {
			topic.setSignalValidation(SmtConstant.IN_PROGRESS.getDescription());
		}
		if ("New".equalsIgnoreCase(topic.getSignalStatus())) {
			topic.setSignalStatus(SmtConstant.IN_PROGRESS.getDescription());
			topic = topicRepository.save(topic);
		}
		Ingredient ingredient = ingredientRepository.findByTopicId(topic.getId());
		List<Product> products = productRepository.findByTopicId(topic.getId());
		List<License> licenses = licenseRepository.findByTopicId(topic.getId());
		List<Soc> socs = socRepository.findByTopicId(topic.getId());
		if (ingredient != null) {
			ingredient.setProducts(products);
			ingredient.setLicenses(licenses);
			topic.setIngredient(ingredient);
			topic.setSocs(socs);
		}
		topic.setSignalUrls(signalURLRepository.findByTopicId(topicId));
		findSoc(topic);
		if (!SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(topic.getSignalStatus())) {
			LOG.debug("FOUND SIGNAL" + topic.getId());
			topic = signalMatchService.findMatchingSignal(topic);
		}
		return topic;
	}
	
	public List<Comments> getTopicComments(Long topicId){
		return commentsRepository.findByTopicId(topicId);
	}

	public Topic createTopic(Topic topic, MultipartFile[] attachments) throws ApplicationException {
		String processInstanceId = runtimeService.startProcessInstanceByKey("topicProcess").getProcessInstanceId();
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		taskService.delegateTask(task.getId(), "Demo Demo");
		if (topic.getId() != null) {
			topic.setId(null);
		}
		if (null == topic.getSignalValidation()) {
			topic.setSignalValidation(SmtConstant.IN_PROGRESS.getDescription());
		}
		Calendar c = Calendar.getInstance();
		topic.setCreatedDate(c.getTime());
		topic.setLastModifiedDate(c.getTime());
		topic.setSignalStatus("New");
		topic.setProcessId(processInstanceId);
		c.add(Calendar.DAY_OF_YEAR, 5);
		topic.setDueDate(c.getTime());
		setTopicIdForSignalStatistics(topic);

		SignalConfiguration signalConfiguration = signalConfigurationRepository
				.findByConfigName(SignalConfigurationType.DEFAULT_CONFIG.name());
		if (null != signalConfiguration) {
			topic.setCohortPercentage(Long.valueOf(signalConfiguration.getCohortPercentage()));
			topic.setConfidenceIndex(Long.valueOf(signalConfiguration.getConfidenceIndex()));
		}

		Long topicExist = topicRepository.countByNameIgnoreCase(topic.getName());
		if (topicExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.SIGNAL_NAME_DUPLICATE, null);
		}

		Topic topicUpdated = topicRepository.save(topic);
		Ingredient ingredient = topicUpdated.getIngredient();
		if (ingredient != null) {
			AssignmentConfiguration assignmentConfiguration = null;
			if (!StringUtils.isEmpty(topicUpdated.getSourceName())) {
				assignmentConfiguration = assignmentConfigurationRepository
						.findByIngredientAndSignalSource(ingredient.getIngredientName(), topicUpdated.getSourceName());
			}
			// If Source is null and combination not available we have to
			// fetch
			// with Ingredient
			if (assignmentConfiguration == null) {
				assignmentConfiguration = assignmentConfigurationRepository
						.findByIngredientAndSignalSourceIsNull(ingredient.getIngredientName());
			}

			if (assignmentConfiguration != null) {
				topicUpdated.setAssignTo(assignmentConfiguration.getSignalValidationAssignmentUser());
				topicUpdated = topicRepository.save(topicUpdated);
			}

			ingredient.setTopicId(topicUpdated.getId());
			ingredient = ingredientRepository.save(ingredient);

			saveProducts(topicUpdated, ingredient);

			saveLicenses(topicUpdated, ingredient);
		}
		saveSoc(topicUpdated);
		saveSignalUrl(topicUpdated);
		attachmentService.addAttachments(topicUpdated.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT, null,
				topicUpdated.getFileMetadata());
		LOG.info("Start Algorithm for matching signal");
		return signalMatchService.findMatchingSignal(topicUpdated);
	}
	
	public List<Comments> updateComments(Topic topic){
		List<Comments> list = topic.getComments();
		if (!CollectionUtils.isEmpty(list)) {
			for (Comments comment : list) {
				comment.setTopicId(topic.getId());
				comment.setModifiedDate(new Date());
				comment.setCreatedBy(topic.getOwner());
			}
		}
		return commentsRepository.save(topic.getComments());
	}

	/**
	 * @param topicUpdated
	 */
	private void saveSignalUrl(Topic topicUpdated) {
		if (!CollectionUtils.isEmpty(topicUpdated.getSignalUrls())) {
			for (SignalURL url : topicUpdated.getSignalUrls()) {
				url.setTopicId(topicUpdated.getId());
				url.setModifiedDate(new Date());
			}
			signalURLRepository.save(topicUpdated.getSignalUrls());
		}
	}

	/**
	 * @param topicUpdated
	 */
	private void saveSoc(Topic topicUpdated) {
		List<Soc> socs = topicUpdated.getSocs();
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setTopicId(topicUpdated.getId());
			}
			socs = socRepository.save(socs);
			List<Hlgt> hlgts;
			List<Hlt> hlts;
			List<Pt> pts;

			for (Soc soc : socs) {
				hlgts = soc.getHlgts();
				hlts = soc.getHlts();
				pts = soc.getPts();
				saveHlgt(topicUpdated, hlgts, soc);
				saveHlt(topicUpdated, hlts, soc);
				savePt(topicUpdated, pts, soc);
			}
		}
	}

	/**
	 * @param topicUpdated
	 * @param pts
	 * @param soc
	 */
	private void savePt(Topic topicUpdated, List<Pt> pts, Soc soc) {
		if (!CollectionUtils.isEmpty(pts)) {
			for (Pt pt : pts) {
				pt.setSocId(soc.getId());
				pt.setTopicId(topicUpdated.getId());
			}
			ptRepository.save(pts);
		}
	}

	/**
	 * @param topicUpdated
	 * @param hlts
	 * @param soc
	 */
	private void saveHlt(Topic topicUpdated, List<Hlt> hlts, Soc soc) {
		if (!CollectionUtils.isEmpty(hlts)) {
			for (Hlt hlt : hlts) {
				hlt.setSocId(soc.getId());
				hlt.setTopicId(topicUpdated.getId());
			}
			hltRepository.save(hlts);
		}
	}

	/**
	 * @param topicUpdated
	 * @param hlgts
	 * @param soc
	 */
	private void saveHlgt(Topic topicUpdated, List<Hlgt> hlgts, Soc soc) {
		if (!CollectionUtils.isEmpty(hlgts)) {
			for (Hlgt hlgt : hlgts) {
				hlgt.setSocId(soc.getId());
				hlgt.setTopicId(topicUpdated.getId());
			}
			hlgtRepository.save(hlgts);
		}
	}

	/**
	 * @param topicUpdated
	 * @param ingredient
	 */
	private void saveLicenses(Topic topicUpdated, Ingredient ingredient) {
		List<License> licenses = ingredient.getLicenses();
		if (!CollectionUtils.isEmpty(licenses)) {
			for (License singleLicense : licenses) {
				singleLicense.setIngredientId(ingredient.getId());
				singleLicense.setTopicId(topicUpdated.getId());
			}
			licenseRepository.save(licenses);
		}
	}

	/**
	 * @param topicUpdated
	 * @param ingredient
	 */
	private void saveProducts(Topic topicUpdated, Ingredient ingredient) {
		List<Product> products = ingredient.getProducts();
		if (!CollectionUtils.isEmpty(products)) {
			for (Product singleProduct : products) {
				singleProduct.setIngredientId(ingredient.getId());
				singleProduct.setTopicId(topicUpdated.getId());
			}
			productRepository.save(products);
		}
	}

	/**
	 * @param topic
	 */
	private void setTopicIdForSignalStatistics(Topic topic) {
		if (!CollectionUtils.isEmpty(topic.getSignalStatistics())) {
			for (SignalStatistics signalStatistic : topic.getSignalStatistics()) {
				signalStatistic.setTopic(topic);
			}
		}
	}

	public String updateTopic(Topic topic, MultipartFile[] attachments) throws ApplicationException {
		if (topic.getId() == null) {
			throw new ApplicationException("Update failed for Topic, since it does not have any valid Id field.");
		}
		topic.setLastModifiedDate(new Date());
		setTopicIdForSignalStatistics(topic);
		attachmentService.addAttachments(topic.getId(), attachments, AttachmentType.TOPIC_ATTACHMENT,
				topic.getDeletedAttachmentIds(), topic.getFileMetadata());
		topicRepository.save(topic);
		saveSignalUrl(topic);
		return "Update Success";
	}

	public AssessmentPlan validateAndPrioritize(Long topicId, AssessmentPlan assessmentPlan)
			throws ApplicationException {
		Topic topic = topicRepository.findOne(topicId);
		if (topic == null) {
			throw new ApplicationException("Topic not found with the given Id [" + topicId + "]");
		}
		Task task = taskService.createTaskQuery().processInstanceId(topic.getProcessId()).singleResult();
		if (task == null) {
			throw new ApplicationException("Task not found for the process " + topic.getProcessId());
		}
		taskService.complete(task.getId());

		CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
		topic.setProcessId(instance.getCaseInstanceId());
		Date d = new Date();
		assessmentPlan.setCreatedDate(d);
		assessmentPlan.setLastModifiedDate(d);
		if (assessmentPlan.getId() == null) {
			assessmentPlan.setAssessmentPlanStatus("New");
		}
		AssignmentConfiguration assignmentConfiguration = null;

		if (!StringUtils.isEmpty(topic.getSourceName())) {
			assignmentConfiguration = assignmentConfigurationRepository
					.findByIngredientAndSignalSource(assessmentPlan.getIngrediantName(), assessmentPlan.getSource());
		}
		// If Source is not null and combination not available we have to fetch
		// with Ingredient
		if (assignmentConfiguration == null) {
			assignmentConfiguration = assignmentConfigurationRepository
					.findByIngredientAndSignalSourceIsNull(assessmentPlan.getIngrediantName());
		}

		if (assignmentConfiguration != null) {
			assessmentPlan.setAssignTo(assignmentConfiguration.getAssessmentAssignmentUser());
		}
		assessmentPlan.setCaseInstanceId(instance.getCaseInstanceId());
		assessmentPlan.setAssessmentTaskStatus("Not Completed");

		Long assessmentPlanExist = assessmentPlanRepository
				.countByAssessmentNameIgnoreCase(assessmentPlan.getAssessmentName());
		if (assessmentPlanExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENTPLAN_NAME_DUPLICATE, null);
		}

    	
		topic.setAssessmentPlan(assessmentPlanRepository.save(assessmentPlan));
		topic.setSignalStatus(SmtConstant.COMPLETED.getDescription());
		topic.setSignalValidation(SmtConstant.COMPLETED.getDescription());
		topic.setLastModifiedDate(new Date());
		topicRepository.save(topic);

		return assessmentPlan;
	}

	public List<Topic> findTopics(SearchDto searchDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Topic> query = criteriaBuilder.createQuery(Topic.class);
		Root<Topic> rootTopic = query.from(Topic.class);

		if (null != searchDto) {
			List<Predicate> predicates = new ArrayList<>(10);

			addProducts(searchDto, criteriaBuilder, query, rootTopic, predicates);
			addLicenses(searchDto, criteriaBuilder, query, rootTopic, predicates);
			addIngredients(searchDto, criteriaBuilder, query, rootTopic, predicates);
			addHlts(searchDto, criteriaBuilder, query, rootTopic, predicates);
			addHlgts(searchDto, criteriaBuilder, query, rootTopic, predicates);
			addPts(searchDto, criteriaBuilder, query, rootTopic, predicates);
			addCreatedDate(searchDto, criteriaBuilder, rootTopic, predicates);
			addStatuses(searchDto, criteriaBuilder, rootTopic, predicates);
			addSignalNames(searchDto, criteriaBuilder, rootTopic, predicates);
			addAssignees(searchDto, criteriaBuilder, rootTopic, predicates);
			addDueDate(searchDto, criteriaBuilder, rootTopic, predicates);
			addSignalConfirmations(searchDto, criteriaBuilder, rootTopic, predicates);

			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			query.select(rootTopic).where(andPredicate)
					.orderBy(criteriaBuilder.desc(rootTopic.get(SmtConstant.CREATED_DATE.getDescription())))
					.distinct(true);
		} else {
			query.select(rootTopic)
					.orderBy(criteriaBuilder.desc(rootTopic.get(SmtConstant.CREATED_DATE.getDescription())));
		}

		TypedQuery<Topic> q = entityManager.createQuery(query);
		return q.getResultList();
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addSignalConfirmations(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getSignalConfirmations())) {
			predicates.add(
					criteriaBuilder.isTrue(rootTopic.get("signalConfirmation").in(searchDto.getSignalConfirmations())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addDueDate(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (null != searchDto.getStartDate() && null != searchDto.getEndDate()) {
			if (searchDto.isDueDate()) {
				predicates
						.add(criteriaBuilder.greaterThanOrEqualTo(rootTopic.get("dueDate"), searchDto.getStartDate()));
				predicates.add(criteriaBuilder.lessThanOrEqualTo(rootTopic.get("dueDate"), searchDto.getEndDate()));
			} else {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(
						rootTopic.get(SmtConstant.CREATED_DATE.getDescription()), searchDto.getStartDate()));
				predicates.add(criteriaBuilder.lessThanOrEqualTo(
						rootTopic.get(SmtConstant.CREATED_DATE.getDescription()), searchDto.getEndDate()));
			}

		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addAssignees(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("assignTo").in(searchDto.getAssignees())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addSignalNames(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getSignalNames())) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("name").in(searchDto.getSignalNames())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addStatuses(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
			predicates.add(criteriaBuilder.isTrue(rootTopic.get("signalStatus").in(searchDto.getStatuses())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param rootTopic
	 * @param predicates
	 */
	private void addCreatedDate(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic,
			List<Predicate> predicates) {
		if (null != searchDto.getCreatedDate()) {
			predicates.add(criteriaBuilder.equal(rootTopic.get(SmtConstant.CREATED_DATE.getDescription()),
					searchDto.getCreatedDate()));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addPts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery<Topic> query,
			Root<Topic> rootTopic, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getPts())) {
			Root<Pt> rootPt = query.from(Pt.class);
			Predicate ptTopicEquals = criteriaBuilder.equal(rootTopic.get("id"),
					rootPt.get(SmtConstant.TOPIC_ID.getDescription()));
			Predicate ptNameEquals = criteriaBuilder.isTrue(rootPt.get("ptName").in(searchDto.getPts()));
			predicates.add(ptTopicEquals);
			predicates.add(ptNameEquals);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addHlgts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery<Topic> query,
			Root<Topic> rootTopic, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getHlgts())) {
			Root<Hlgt> rootHlgt = query.from(Hlgt.class);
			Predicate hlgtTopicEquals = criteriaBuilder.equal(rootTopic.get("id"),
					rootHlgt.get(SmtConstant.TOPIC_ID.getDescription()));
			Predicate hlgtNameEquals = criteriaBuilder.isTrue(rootHlgt.get("hlgtName").in(searchDto.getHlgts()));
			predicates.add(hlgtTopicEquals);
			predicates.add(hlgtNameEquals);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addHlts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery<Topic> query,
			Root<Topic> rootTopic, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getHlts())) {
			Root<Hlt> rootHlt = query.from(Hlt.class);
			Predicate hltTopicEquals = criteriaBuilder.equal(rootTopic.get("id"),
					rootHlt.get(SmtConstant.TOPIC_ID.getDescription()));
			Predicate hltNameEquals = criteriaBuilder.isTrue(rootHlt.get("hltName").in(searchDto.getHlts()));
			predicates.add(hltTopicEquals);
			predicates.add(hltNameEquals);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addIngredients(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery<Topic> query,
			Root<Topic> rootTopic, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getIngredients())) {
			Root<Ingredient> rootIngredient = query.from(Ingredient.class);
			Predicate ingredientEquals = criteriaBuilder.equal(rootTopic.get("id"),
					rootIngredient.get(SmtConstant.TOPIC_ID.getDescription()));
			Predicate ingredientNameEquals = criteriaBuilder
					.isTrue(rootIngredient.get("ingredientName").in(searchDto.getIngredients()));
			predicates.add(ingredientEquals);
			predicates.add(ingredientNameEquals);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addLicenses(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery<Topic> query,
			Root<Topic> rootTopic, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
			Root<License> rootLicense = query.from(License.class);
			Predicate licenseEquals = criteriaBuilder.equal(rootTopic.get("id"),
					rootLicense.get(SmtConstant.TOPIC_ID.getDescription()));
			Predicate licenseNameEquals = criteriaBuilder
					.isTrue(rootLicense.get("licenseName").in(searchDto.getLicenses()));
			predicates.add(licenseEquals);
			predicates.add(licenseNameEquals);
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addProducts(SearchDto searchDto, CriteriaBuilder criteriaBuilder, CriteriaQuery<Topic> query,
			Root<Topic> rootTopic, List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
			Root<Product> rootProduct = query.from(Product.class);
			Predicate productEquals = criteriaBuilder.equal(rootTopic.get("id"),
					rootProduct.get(SmtConstant.TOPIC_ID.getDescription()));
			Predicate producNameEquals = criteriaBuilder
					.isTrue(rootProduct.get("productName").in(searchDto.getProducts()));
			predicates.add(productEquals);
			predicates.add(producNameEquals);
		}
	}

	public Long getValidateAndPrioritizeCount(String assignTo) {
		return topicRepository.countByAssignToAndSignalStatusNotLikeIgnoreCase(assignTo,
				SmtConstant.COMPLETED.getDescription());
	}

	public Long getAssessmentCount(String assignTo) {
		return assessmentPlanRepository.countByAssignToAndAssessmentPlanStatusNotLikeIgnoreCase(assignTo,
				SmtConstant.COMPLETED.getDescription());
	}

	public Long getRiskCount(String assignTo) {
		return riskPlanRepository.countByAssignToAndStatusNotLikeIgnoreCase(assignTo,
				SmtConstant.COMPLETED.getDescription());
	}

	public String getCountsByFilter(String ingredientName, String assignTo) {
		List<Ingredient> ingredients = ingredientRepository.findAllByIngredientNameIn(Arrays.asList(ingredientName));
		List<Long> topicIds = new ArrayList<>();
		ingredients.parallelStream().forEach(ingredient -> topicIds.add(ingredient.getTopicId()));
		List<Topic> signals = topicRepository.findAllByIdInAndAssignToAndSignalStatusNotLikeOrderByCreatedDateDesc(
				topicIds, assignTo, SmtConstant.COMPLETED.getDescription());
		List<AssessmentPlan> assessmentPlanList = signals.stream().map(signal -> signal.getAssessmentPlan()).filter(
				e -> e != null && !SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(e.getAssessmentPlanStatus()))
				.collect(Collectors.toList());
		List<RiskPlan> riskPlanList = assessmentPlanList.stream().map(assessmentPlan -> assessmentPlan.getRiskPlan())
				.filter(e -> e != null && !SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(e.getStatus()))
				.collect(Collectors.toList());
		return SignalUtil.getCounts(Long.valueOf(signals.size()), Long.valueOf(assessmentPlanList.size()),
				Long.valueOf(riskPlanList.size()));
	}

	public List<TaskTemplate> getTaskTamplatesOfIngrediant(String ingrediantName) {
		List<Long> templateIds = taskTemplateIngrediantRepository.findByIngrediantName(ingrediantName);
		return taskTemplateRepository.findByIdIn(templateIds);
	}

	public List<SignalAction> associateTemplateTasks(AssessmentPlan assessmentPlan) {
		Sort sort = new Sort(Sort.Direction.DESC, SmtConstant.CREATED_DATE.getDescription());
		List<SignalAction> signalActionList = null;
		if (!CollectionUtils.isEmpty(assessmentPlan.getTemplateIds())) {
			for (Long id : assessmentPlan.getTemplateIds()) {
				List<SignalAction> actions = assessmentActionRepository.findAllByTemplateId(id);
				signalActionList = new ArrayList<>();
				createAssessmentActions(assessmentPlan, sort, signalActionList, actions);
			}
		}
		return signalActionList;
	}

	/**
	 * @param assessmentPlan
	 * @param sort
	 * @param signalActionList
	 * @param actions
	 */
	private void createAssessmentActions(AssessmentPlan assessmentPlan, Sort sort, List<SignalAction> signalActionList,
			List<SignalAction> actions) {
		if (!CollectionUtils.isEmpty(actions)) {
			for (SignalAction action : actions) {
				createAssessmentAction(assessmentPlan, sort, signalActionList, action);
			}
		}
	}

	/**
	 * @param assessmentPlan
	 * @param sort
	 * @param signalActionList
	 * @param action
	 */
	private void createAssessmentAction(AssessmentPlan assessmentPlan, Sort sort, List<SignalAction> signalActionList,
			SignalAction action) {
		SignalAction signalAction = new SignalAction();
		signalAction.setCaseInstanceId(assessmentPlan.getCaseInstanceId());
		signalAction.setActionName(action.getActionName());
		signalAction.setCreatedDate(new Date());
		signalAction.setLastModifiedDate(action.getLastModifiedDate());
		signalAction.setActionStatus(action.getActionStatus());
		signalAction.setDueDate(action.getDueDate());
		signalAction.setAssessmentId(String.valueOf(assessmentPlan.getId()));
		signalAction.setActionType(action.getActionType());
		signalAction.setDueDate(SignalUtil.getDueDate(action.getInDays(), signalAction.getCreatedDate()));
		signalAction.setInDays(action.getInDays());
		signalAction.setActionDescription(action.getActionDescription());
		signalAction.setActionNotes(action.getActionNotes());
		if (action.getAssignTo() == null) {
			signalAction.setAssignTo(assessmentPlan.getAssignTo());
		} else {
			signalAction.setAssignTo(action.getAssignTo());
		}
		signalAction.setOwner(assessmentPlan.getAssignTo());
		Task task = taskService.newTask();
		task.setCaseInstanceId(signalAction.getCaseInstanceId());
		task.setName(signalAction.getActionName());
		taskService.saveTask(task);
		List<Task> list = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).list();
		TaskInst taskInstance = new TaskInst();
		taskInstance.setId(list.get(list.size() - 1).getId());
		taskInstance.setCaseDefKey("assessment");
		taskInstance.setTaskDefKey("assessment");
		taskInstance.setCaseInstId(assessmentPlan.getCaseInstanceId());
		taskInstance.setStartTime(new Date());
		signalAction.setTaskId(taskInstance.getId());
		signalAction = assessmentActionRepository.save(signalAction);
		signalActionList.add(signalAction);
		associateTemplateAttachments(sort, action, signalAction);
		associateTemplateURLs(action, signalAction);
	}

	/**
	 * @param action
	 * @param signalAction
	 */
	public void associateTemplateURLs(SignalAction action, SignalAction signalAction) {
		List<SignalURL> templateTaskUrls = signalURLRepository.findByTopicId(action.getId());
		if (!CollectionUtils.isEmpty(templateTaskUrls)) {
			List<SignalURL> assessmentActionSignalURLs = new ArrayList<>();
			for (SignalURL url : templateTaskUrls) {
				SignalURL assessmentActionSignalURL = new SignalURL();
				assessmentActionSignalURL.setDescription(url.getDescription());
				assessmentActionSignalURL.setTopicId(signalAction.getId());
				assessmentActionSignalURL.setUrl(url.getUrl());
				assessmentActionSignalURL.setModifiedDate(new Date());
				assessmentActionSignalURLs.add(assessmentActionSignalURL);
			}
			signalURLRepository.save(assessmentActionSignalURLs);
		}
	}

	/**
	 * @param sort
	 * @param action
	 * @param signalAction
	 */
	public void associateTemplateAttachments(Sort sort, SignalAction action, SignalAction signalAction) {
		List<Attachment> attachments = attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(
				action.getId(), AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, sort);
		if (!CollectionUtils.isEmpty(attachments)) {
			List<Attachment> assessmentActionAttachments = new ArrayList<>();
			for (Attachment attachment : attachments) {
				Attachment assessmentActionAttachment = new Attachment();

				assessmentActionAttachment.setDescription(attachment.getDescription());
				assessmentActionAttachment.setAttachmentsURL(attachment.getAttachmentsURL());
				assessmentActionAttachment.setAttachmentResourceId(signalAction.getId());
				assessmentActionAttachment.setContentType(attachment.getContentType());
				assessmentActionAttachment.setContent(attachment.getContent());
				assessmentActionAttachment.setFileName(attachment.getFileName());
				assessmentActionAttachment.setCreatedDate(new Date());
				assessmentActionAttachment.setAttachmentType(attachment.getAttachmentType());

				assessmentActionAttachments.add(assessmentActionAttachment);
			}
			attachmentRepository.save(assessmentActionAttachments);
		}
	}

	public List<Topic> findTopicsByRunInstanceId(Long runInstanceId) {
		List<Topic> topics = topicRepository.findTopicByRunInstanceIdOrderByCreatedDateAsc(runInstanceId);
		if (!CollectionUtils.isEmpty(topics)) {
			for (Topic topic : topics) {
				Ingredient ingredient = ingredientRepository.findByTopicId(topic.getId());
				List<Product> products = productRepository.findByTopicId(topic.getId());
				List<License> licenses = licenseRepository.findByTopicId(topic.getId());
				if (ingredient != null) {
					ingredient.setProducts(products);
					ingredient.setLicenses(licenses);
					topic.setIngredient(ingredient);
				}
				topic.setSocs(findSoc(topic));
			}
		}
		return topics;
	}

	/**
	 * @param topic
	 * @return
	 */
	private List<Soc> findSoc(Topic topic) {
		List<Soc> socs = socRepository.findByTopicId(topic.getId());
		if (!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setHlgts(hlgtRepository.findBySocId(soc.getId()));
				soc.setHlts(hltRepository.findBySocId(soc.getId()));
				soc.setPts(ptRepository.findBySocId(soc.getId()));
			}
		}
		return socs;
	}

	public void deleteSignalURL(Long signalUrlId) throws ApplicationException {
		SignalURL signalURL = signalURLRepository.findOne(signalUrlId);
		if (signalURL == null) {
			throw new ApplicationException("Risk Plan Action Type not found with the given Id : " + signalUrlId);
		}
		signalURLRepository.delete(signalURL);
	}

	public List<NonSignal> findNonSignalsByRunInstanceId(Long runInstanceId) {
		return nonSignalRepository.findNonSignalByRunInstanceIdOrderByCreatedDateAsc(runInstanceId);
	}

	public void deleteTopicComments(Long commentsId) throws ApplicationException {
		Comments comments = commentsRepository.findOne(commentsId);
		if (comments == null) {
			throw new ApplicationException("Risk Plan Action Type not found with the given Id : " + commentsId);
		}
		commentsRepository.delete(comments);
		
	}

}
