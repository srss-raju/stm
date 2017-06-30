package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.DateKeyType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskInstRepository;

/**
 * Created by RajeshKumar on 12-04-2017.
 */
@Transactional
@Service
public class RiskPlanService {

	@Autowired
	AssessmentPlanRepository assessmentPlanRepository;

	@Autowired
	RiskPlanRepository riskPlanRepository;

	@Autowired
	RiskTaskRepository riskTaskRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	TaskInstRepository taskInstRepository;

	@Autowired
	CaseService caseService;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	LicenseRepository licenseRepository;

	@Autowired
	IngredientRepository ingredientRepository;

	@Autowired
	SearchService searchService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CommentsRepository commentsRepository;

	@Autowired
	SignalURLRepository signalURLRepository;

	@Autowired
	private AssignmentConfigurationRepository assignmentConfigurationRepository;

	public RiskPlan insert(RiskPlan riskPlan, MultipartFile[] attachments, Long assessmentId)
			throws ApplicationException {
		CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
		riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
		riskPlan.setStatus("New");
		Date d = new Date();
		riskPlan.setCreatedDate(d);
		riskPlan.setLastModifiedDate(d);
		riskPlan.setRiskTaskStatus("Not Completed");
		AssignmentConfiguration assignmentConfiguration = null;

		if (!StringUtils.isEmpty(riskPlan.getSource())) {
			assignmentConfiguration = assignmentConfigurationRepository
					.findByIngredientAndSignalSource(riskPlan.getIngredient(), riskPlan.getSource());
		}
		// If Source is not null and combination not available we have to fetch
		// with Ingredient
		if (assignmentConfiguration == null) {
			assignmentConfiguration = assignmentConfigurationRepository
					.findByIngredientAndSignalSourceIsNull(riskPlan.getIngredient());
		}

		if (assignmentConfiguration != null) {
			riskPlan.setAssignTo(assignmentConfiguration.getRiskPlanAssignmentUser());
		}

		RiskPlan riskPlanUpdated;
		if (assessmentId != null) {
			AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
			if (assessmentPlan == null) {
				throw new ApplicationException("Assessment Plan not found with the given Id : " + assessmentId);
			}
			assessmentPlan.setRiskPlan(riskPlan);
			riskPlan.setAssessmentPlan(assessmentPlan);
			riskPlanUpdated = riskPlanRepository.save(riskPlan);
			assessmentPlanRepository.save(assessmentPlan);
		} else {
			riskPlanUpdated = riskPlanRepository.save(riskPlan);
		}
		attachmentService.addAttachments(riskPlanUpdated.getId(), attachments, AttachmentType.RISK_ASSESSMENT, null,
				riskPlanUpdated.getFileMetadata());
		if (!CollectionUtils.isEmpty(riskPlanUpdated.getSignalUrls())) {
			for (SignalURL url : riskPlanUpdated.getSignalUrls()) {
				url.setTopicId(riskPlanUpdated.getId());
			}
			signalURLRepository.save(riskPlanUpdated.getSignalUrls());
		}
		return riskPlanUpdated;
	}

	/**
	 * @param searchDto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RiskPlan> findAllRiskPlansForSearch2(SearchDto searchDto) {
		StringBuilder queryBuilder = new StringBuilder();
		List<String> whereClauses = new ArrayList<>();

		boolean searchAll = false;

		if (searchDto == null) {
			searchAll = true;
		} else if (!CollectionUtils.isEmpty(searchDto.getProducts())
				|| !CollectionUtils.isEmpty(searchDto.getLicenses())
				|| !CollectionUtils.isEmpty(searchDto.getIngredients())) {
			queryBuilder.append(
					"SELECT r.* FROM sm_risk_plan r left join  sm_assessment_plan a on   r.id=a.risk_plan_id left join sm_topic t on t.assessment_plan_id=a.id");

			if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
				queryBuilder.append(" inner join sm_product p on p.topic_id=t.id ");
				whereClauses.add(" p.product_name in :productNames");
			}

			if (!CollectionUtils.isEmpty(searchDto.getIngredients())) {
				queryBuilder.append(" inner join sm_ingredient i on i.topic_id=t.id ");
				whereClauses.add("i.ingredient_name in :ingredientNames");
			}

			if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
				queryBuilder.append(" inner join sm_license l on l.topic_id=t.id ");
				whereClauses.add("l.license_name in :licenseNames");
			}

		}

		if (searchAll) {
			queryBuilder.append("SELECT r.* FROM sm_risk_plan r ");
		}

		buildWhereConditions(searchDto, whereClauses);
		appendWhereClause(whereClauses, queryBuilder);

		queryBuilder.append(" order by created_date DESC");
		String queryStr = queryBuilder.toString();
		Query query = entityManager.createNativeQuery(queryStr, RiskPlan.class);
		setParameters(queryStr, searchDto, query);

		return query.getResultList();
	}

	private void appendWhereClause(List<String> whereClauses, StringBuilder queryBuilder) {
		if (!whereClauses.isEmpty()) {
			int i = 1;
			queryBuilder.append(" where ");
			for (String condition : whereClauses) {
				queryBuilder.append(condition);
				if (i < whereClauses.size()) {
					queryBuilder.append(" and ");
				}
				i++;
			}
		}
	}

	private void buildWhereConditions(SearchDto searchDto, List<String> whereClauses) {
		if (searchDto != null) {
			addStatus(searchDto, whereClauses);
			addAssignees(searchDto, whereClauses);
			addRiskTaskStatus(searchDto, whereClauses);
			addStartDate(searchDto, whereClauses);
			addEndDate(searchDto, whereClauses);
		}
	}

	/**
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addEndDate(SearchDto searchDto, List<String> whereClauses) {
		if (null != searchDto.getEndDate()) {
			if (DateKeyType.DUEDATE.name().equalsIgnoreCase(searchDto.getDateKey())) {
				whereClauses.add("r.risk_due_date<= :endDate");

			} else if (DateKeyType.CREATED.name().equalsIgnoreCase(searchDto.getDateKey())) {
				whereClauses.add("r.created_date<= :endDate");
			}
		}
	}

	/**
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addStartDate(SearchDto searchDto, List<String> whereClauses) {
		if (null != searchDto.getStartDate()) {
			if (DateKeyType.DUEDATE.name().equalsIgnoreCase(searchDto.getDateKey())) {
				whereClauses.add(" r.risk_due_date>= :startDate ");

			} else if (DateKeyType.CREATED.name().equalsIgnoreCase(searchDto.getDateKey())) {
				whereClauses.add(" r.created_date>= :startDate");

			}

		}
	}

	/**
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addRiskTaskStatus(SearchDto searchDto, List<String> whereClauses) {
		if (!CollectionUtils.isEmpty(searchDto.getRiskTaskStatus())) {
			whereClauses.add(" r.risk_task_status in :riskTaskStatus");
		}
	}

	/**
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addAssignees(SearchDto searchDto, List<String> whereClauses) {
		if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
			whereClauses.add(" r.assign_to in :assignTo");
		}
	}

	/**
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addStatus(SearchDto searchDto, List<String> whereClauses) {
		if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
			whereClauses.add(" r.status in :statuses");
		}
	}

	private void setParameters(String queryStr, SearchDto searchDto, Query query) {
		if (queryStr.contains(":statuses")) {
			query.setParameter("statuses", searchDto.getStatuses());
		}

		if (queryStr.contains(":riskTaskStatus")) {
			query.setParameter("riskTaskStatus", searchDto.getRiskTaskStatus());
		}

		if (queryStr.contains(":assignTo")) {
			query.setParameter("assignTo", searchDto.getAssignees());
		}

		if (queryStr.contains(":startDate")) {
			query.setParameter("startDate", searchDto.getStartDate());
		}

		if (queryStr.contains(":endDate")) {
			query.setParameter("endDate", searchDto.getEndDate());
		}

		if (queryStr.contains(":productNames")) {
			query.setParameter("productNames", searchDto.getProducts());
		}

		if (queryStr.contains(":ingredientNames")) {
			query.setParameter("ingredientNames", searchDto.getIngredients());
		}

		if (queryStr.contains(":licenseNames")) {
			query.setParameter("licenseNames", searchDto.getLicenses());
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<RiskPlan> findAllRiskPlansForSearch(SearchDto searchDto) {
		List<Long> riskTopicIds = new ArrayList<>();
		boolean searchAll = true;

		if (searchDto != null && !CollectionUtils.isEmpty(searchDto.getStatuses())) {
			searchAll = false;
		}
		searchAll = searchService.getSignalIdsForSearch(searchDto, riskTopicIds, searchAll);
		if (searchAll) {
			Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
			return riskPlanRepository.findAll(sort);
		}
		List<RiskPlan> riskPlanList = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT o FROM RiskPlan o ");
		boolean executeQuery = false;

		executeQuery = appendAssessmentPlan(searchDto, riskTopicIds, queryString, executeQuery);
		executeQuery = appendRiskPlanStatus(searchDto, queryString, executeQuery);

		queryString.append(" ORDER BY o.createdDate DESC");
		Query q = entityManager.createQuery(queryString.toString(), RiskPlan.class);

		if (queryString.toString().contains(":topicIds")) {
			if (CollectionUtils.isEmpty(riskTopicIds)) {
				q.setParameter("topicIds", null);
			} else {
				q.setParameter("topicIds", riskTopicIds);
			}
		}
		if (searchDto != null && queryString.toString().contains(":riskPlanStatus")) {
			q.setParameter("riskPlanStatus", searchDto.getStatuses());
		}
		if (executeQuery) {
			riskPlanList = q.getResultList();
		}
		return riskPlanList;
	}

	/**
	 * @param searchDto
	 * @param queryString
	 * @param executeQuery
	 * @return
	 */
	private boolean appendRiskPlanStatus(SearchDto searchDto, StringBuilder queryString, boolean executeQuery) {
		boolean riskPlanStatusFlag = executeQuery;
		if (searchDto != null && !CollectionUtils.isEmpty(searchDto.getStatuses())) {
			riskPlanStatusFlag = true;
			if (queryString.toString().contains(":topicIds")) {
				queryString.append(" AND o.status IN :riskPlanStatus ");
			} else {
				queryString.append(" WHERE o.status IN :riskPlanStatus ");
			}
		}
		return riskPlanStatusFlag;
	}

	/**
	 * @param searchDto
	 * @param riskTopicIds
	 * @param queryString
	 * @param executeQuery
	 * @return
	 */
	private boolean appendAssessmentPlan(SearchDto searchDto, List<Long> riskTopicIds, StringBuilder queryString,
			boolean executeQuery) {
		boolean assessmentPlanFlag = executeQuery;
		if (!CollectionUtils.isEmpty(riskTopicIds) && searchDto != null) {
			assessmentPlanFlag = appendQueryString(searchDto, queryString, assessmentPlanFlag);
		}
		return assessmentPlanFlag;
	}

	/**
	 * @param searchDto
	 * @param queryString
	 * @param assessmentPlanFlag
	 * @return
	 */
	private boolean appendQueryString(SearchDto searchDto, StringBuilder queryString, boolean assessmentPlanFlag) {
		boolean executeQuery = assessmentPlanFlag;
		if (!CollectionUtils.isEmpty(searchDto.getProducts()) || !CollectionUtils.isEmpty(searchDto.getLicenses())
				|| !CollectionUtils.isEmpty(searchDto.getIngredients())) {
			executeQuery = true;
			queryString.append("INNER JOIN o.assessmentPlan a ");
			queryString.append("INNER JOIN a.topics t WHERE t.id IN :topicIds ");
		}
		return executeQuery;
	}

	public void createRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws IOException {
		if (riskTask.getCaseInstanceId() != null
				&& SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(riskTask.getStatus())) {
			Task task = taskService.createTaskQuery().caseInstanceId(riskTask.getCaseInstanceId()).singleResult();
			taskService.complete(task.getId());
		}
		Task task = taskService.newTask();
		task.setCaseInstanceId(riskTask.getCaseInstanceId());
		task.setName(riskTask.getName());
		taskService.saveTask(task);
		List<Task> list = taskService.createTaskQuery().caseInstanceId(riskTask.getCaseInstanceId()).list();
		TaskInst taskInstance = new TaskInst();
		taskInstance.setId(list.get(list.size() - 1).getId());
		taskInstance.setCaseDefKey("risk");
		taskInstance.setTaskDefKey("risk");
		taskInstance.setCaseInstId(riskTask.getCaseInstanceId());
		taskInstance.setStartTime(new Date());
		riskTask.setTaskId(taskInstance.getId());
		Date d = new Date();
		riskTask.setCreatedDate(d);
		riskTask.setLastUpdatedDate(d);
		riskTask.setStatus("New");
		RiskPlan riskPlan = riskPlanRepository.findOne(Long.valueOf(riskTask.getRiskId()));
		riskTask.setAssignTo(riskPlan.getAssignTo());
		riskTask.setOwner(riskPlan.getAssignTo());

		taskInstRepository.save(taskInstance);
		RiskTask riskTaskUpdated = riskTaskRepository.save(riskTask);
		attachmentService.addAttachments(riskTaskUpdated.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				null, riskTaskUpdated.getFileMetadata());
		if (!CollectionUtils.isEmpty(riskTaskUpdated.getSignalUrls())) {
			for (SignalURL url : riskTaskUpdated.getSignalUrls()) {
				url.setTopicId(riskTaskUpdated.getId());
			}
			signalURLRepository.save(riskTaskUpdated.getSignalUrls());
		}
	}

	public RiskTask findById(Long id) {
		RiskTask riskTask = riskTaskRepository.findOne(id);
		if ("New".equalsIgnoreCase(riskTask.getStatus())) {
			riskTask.setStatus("In Progress");
			riskTask = riskTaskRepository.save(riskTask);
		}
		riskTask.setSignalUrls(signalURLRepository.findByTopicId(riskTask.getId()));
		return riskTask;
	}

	public List<RiskTask> findAllByRiskId(String riskId, String status) {
		if (status != null) {
			return riskTaskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(riskId, status);
		}
		return riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(riskId);
	}

	public void delete(Long riskTaskId, String taskId) throws ApplicationException {
		RiskTask riskTask = riskTaskRepository.findOne(riskTaskId);
		if (riskTask == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		riskTaskRepository.delete(riskTask);
		taskService.deleteTask(taskId);
	}

	public void updateRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
		if (riskTask.getId() == null) {
			throw new ApplicationException("Failed to update Action. Invalid Id received");
		}
		if (SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(riskTask.getStatus())) {
			taskService.complete(riskTask.getTaskId());
		}
		riskTask.setLastUpdatedDate(new Date());
		riskTaskRepository.save(riskTask);
		attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				riskTask.getDeletedAttachmentIds(), riskTask.getFileMetadata());
		List<RiskTask> risks = findAllByRiskId(riskTask.getRiskId(), null);
		boolean allTasksCompletedFlag = true;
		if (!CollectionUtils.isEmpty(risks)) {
			for (RiskTask risk : risks) {
				if (!SmtConstant.COMPLETED.getDescription().equals(risk.getStatus())) {
					allTasksCompletedFlag = false;
				}
			}
		}
		if (!CollectionUtils.isEmpty(riskTask.getSignalUrls())) {
			for (SignalURL url : riskTask.getSignalUrls()) {
				url.setTopicId(riskTask.getId());
			}
			signalURLRepository.save(riskTask.getSignalUrls());
		}
		if (allTasksCompletedFlag) {
			riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(),
					Long.valueOf(riskTask.getRiskId()));
		}
	}

	public RiskPlan findByRiskId(Long riskId) throws ApplicationException {
		RiskPlan riskPlan = riskPlanRepository.findOne(riskId);
		if (riskPlan == null) {
			throw new ApplicationException("Risk Plan not found with the given Id : " + riskId);
		}
		if ("New".equalsIgnoreCase(riskPlan.getStatus())) {
			riskPlan.setStatus("In Progress");
			riskPlan = riskPlanRepository.save(riskPlan);
		}
		riskPlan.setComments(commentsRepository.findByRiskPlanId(riskId));
		riskPlan.setSignalUrls(signalURLRepository.findByTopicId(riskPlan.getId()));
		return riskPlan;
	}

	public void riskPlanSummary(RiskPlan riskPlan, MultipartFile[] attachments) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		riskPlan.setLastModifiedDate(new Date());
		riskPlan.setStatus(SmtConstant.COMPLETED.getDescription());
		attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT,
				riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata());
		riskPlanRepository.save(riskPlan);
	}

	public void updateRiskPlan(RiskPlan riskPlan, MultipartFile[] attachments) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		riskPlan.setLastModifiedDate(new Date());
		attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT,
				riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata());
		riskPlanRepository.save(riskPlan);
		List<Comments> list = riskPlan.getComments();
		if (!CollectionUtils.isEmpty(list)) {
			for (Comments comment : list) {
				comment.setRiskPlanId(riskPlan.getId());
				comment.setModifiedDate(new Date());
			}
		}
		commentsRepository.save(riskPlan.getComments());

		if (!CollectionUtils.isEmpty(riskPlan.getSignalUrls())) {
			for (SignalURL url : riskPlan.getSignalUrls()) {
				url.setTopicId(riskPlan.getId());
			}
			signalURLRepository.save(riskPlan.getSignalUrls());
		}
	}
}
