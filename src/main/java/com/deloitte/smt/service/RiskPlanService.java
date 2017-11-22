package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.util.JsonUtil;
import com.deloitte.smt.util.SearchFilters;
import com.deloitte.smt.util.SignalUtil;
import com.deloitte.smt.util.SmtResponse;

/**
 * Created by RajeshKumar on 12-04-2017.
 */
@Transactional
@Service
public class RiskPlanService {
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;

	@Autowired
	AssessmentPlanRepository assessmentPlanRepository;

	@Autowired
	RiskPlanRepository riskPlanRepository;

	@Autowired
	RiskTaskRepository riskTaskRepository;

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
	AttachmentRepository attachmentRepository;

	@Autowired
	private AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@Autowired
	RiskPlanAssignmentService riskPlanAssignmentService;
	
	@Autowired
	SignalAuditService signalAuditService;
	@Autowired
	TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
	
	@Autowired
	private SearchFilters searchFilters;
	/**
	 * 
	 * @param riskPlan
	 * @param attachments
	 * @param assessmentId
	 * @return
	 * @throws ApplicationException
	 */
	public RiskPlan insert(RiskPlan riskPlan, MultipartFile[] attachments, Long assessmentId)
			throws ApplicationException {
		riskPlan.setStatus("New");
		Date d = new Date();
		riskPlan.setCreatedDate(d);
		riskPlan.setLastModifiedDate(d);
		riskPlan.setRiskTaskStatus("Completed");
		riskPlan.setSummary(SmtConstant.SUMMARY.getDescription());
		AssignmentConfiguration assignmentConfiguration = null;

		RiskPlan riskPlanUpdated;
		Long riskPlanExist = riskPlanRepository.countByNameIgnoreCase(riskPlan.getName());
		
		if (assessmentId != null) {
			AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
			if (assessmentPlan == null) {
				throw new ApplicationException("Assessment Plan not found with the given Id : " + assessmentId);
			}
			assessmentPlan.setRiskPlan(riskPlan);
			riskPlan.setAssessmentPlan(assessmentPlan);
			if (riskPlanExist>0) {
				throw exceptionBuilder.buildException(ErrorType.RISKPLAN_NAME_DUPLICATE, null);
			}
			
			riskPlanUpdated = riskPlanRepository.save(riskPlan);
			assessmentPlanRepository.save(assessmentPlan);
		} else {
			if (riskPlanExist>0) {
				throw exceptionBuilder.buildException(ErrorType.RISKPLAN_NAME_DUPLICATE, null);
			}
			riskPlanUpdated = riskPlanRepository.save(riskPlan);
		}
		List<Attachment> attachmentList = attachmentService.addAttachments(riskPlanUpdated.getId(), attachments, AttachmentType.RISK_ASSESSMENT, null,
				riskPlanUpdated.getFileMetadata(), riskPlanUpdated.getCreatedBy());
		updateSignalUrl(riskPlanUpdated);
		
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
			if(assignmentConfiguration.getRiskOwner()!=null){
				riskPlan.setOwner(assignmentConfiguration.getRiskOwner());
			}
			riskPlanAssignmentService.saveAssignmentAssignees(assignmentConfiguration, riskPlanUpdated);
		}
		signalAuditService.saveOrUpdateRiskPlanAudit(riskPlanUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
		return riskPlanUpdated;
	}
	/**
	 * 
	 * @param riskPlanUpdated
	 */
	private void updateSignalUrl(RiskPlan riskPlanUpdated) {
		if (!CollectionUtils.isEmpty(riskPlanUpdated.getSignalUrls())) {
			for (SignalURL url : riskPlanUpdated.getSignalUrls()) {
				url.setTopicId(riskPlanUpdated.getId());
				url.setCreatedDate(riskPlanUpdated.getCreatedDate());
				url.setCreatedBy(riskPlanUpdated.getCreatedBy());
				url.setModifiedBy(riskPlanUpdated.getModifiedBy());
				url.setModifiedDate(riskPlanUpdated.getLastModifiedDate());
			}
			signalURLRepository.save(riskPlanUpdated.getSignalUrls());
		}
	}
	public List<RiskTask> associateRiskTasks(RiskPlan riskPlan){
		List<RiskTask> tasks = null;
		
		if(!CollectionUtils.isEmpty(riskPlan.getRiskTemplateIds())){
			List<RiskTask> riskTaskList = new ArrayList<>();
			for(Long id:riskPlan.getRiskTemplateIds()){
				List<RiskTask> riskTasks = riskTaskRepository.findAllByTemplateId(id);
				tasks = createRiskTask(riskTaskList, riskTasks, riskPlan);
			}
		}
		checkRiskTaskStatus(riskPlan);
		return tasks;
	}
	
	private void checkRiskTaskStatus(RiskPlan riskPlan){
		boolean allTasksCompletedFlag=true;
		List<RiskTask> riskTaskList=riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(String.valueOf(riskPlan.getId()));
		if(!CollectionUtils.isEmpty(riskTaskList)){
			for(RiskTask riskTask:riskTaskList){
        		if(!"Completed".equals(riskTask.getStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
			if(allTasksCompletedFlag){
				riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(),riskPlan.getId());
	        }else{
	        	riskPlanRepository.updateRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription(),riskPlan.getId());
	        }
			
		}
	}
	
	List<TopicRiskPlanAssignmentAssignees> associateRiskPlanAssignmentAssignees(List<RiskPlan> riskPlanList){
		List<TopicRiskPlanAssignmentAssignees> topicRiskPlanAssignmentAssigneesList=new ArrayList<>();
		for(RiskPlan riskPlan :riskPlanList){
			topicRiskPlanAssignmentAssigneesList =	topicRiskPlanAssignmentAssigneesRepository.findByRiskId(riskPlan.getId());
			riskPlan.setTopicRiskPlanAssignmentAssignees(topicRiskPlanAssignmentAssigneesList);
		}
		return topicRiskPlanAssignmentAssigneesList;
	}


	private List<RiskTask> createRiskTask(List<RiskTask> riskTaskList, List<RiskTask> templateRiskTasks, RiskPlan riskPlan) {
		if(!CollectionUtils.isEmpty(templateRiskTasks)){
			for(RiskTask templateTask:templateRiskTasks){
				RiskTask riskTask = new RiskTask();
				riskTask.setActionType(templateTask.getActionType());
				riskTask.setAssignTo(templateTask.getAssignTo());
				if (templateTask.getAssignTo() == null) {
					riskTask.setAssignTo(riskPlan.getAssignTo());
				} else {
					riskTask.setAssignTo(templateTask.getAssignTo());
				}
				
				riskTask.setCreatedBy(templateTask.getCreatedBy());
				riskTask.setCreatedDate(new Date());
				riskTask.setDescription(templateTask.getDescription());
				riskTask.setDueDate(templateTask.getDueDate());
				riskTask.setName(templateTask.getName());
				riskTask.setNotes(templateTask.getNotes());
				riskTask.setStatus("New");
				riskTask.setOwner(riskPlan.getAssignTo());
				riskTask.setRecipients(templateTask.getRecipients());
				riskTask.setInDays(templateTask.getInDays());
				if(templateTask.getInDays() != null){
					riskTask.setDueDate(SignalUtil.getDueDate(templateTask.getInDays().intValue(), riskTask.getCreatedDate()));
				}
				riskTask.setRiskId(String.valueOf(riskPlan.getId()));
				riskTask = riskTaskRepository.save(riskTask);
				riskTaskList.add(riskTask);
			}
		}
		return riskTaskList;
	}
	
	/**
	 * @param sort
	 * @param action
	 * @param signalAction
	 */
	public void associateTemplateAttachments(RiskTask riskTask, RiskTask templateRiskTask) {
		Sort sort = new Sort(Sort.Direction.DESC, SmtConstant.CREATED_DATE.getDescription());
		List<Attachment> attachments = attachmentRepository.findAllByAttachmentResourceIdAndAttachmentType(templateRiskTask.getId(), AttachmentType.RISK_TASK_ASSESSMENT, sort);
		if (!CollectionUtils.isEmpty(attachments)) {
			List<Attachment> riskTaskAttachments = new ArrayList<>();
			for (Attachment attachment : attachments) {
				Attachment riskTaskAttachment = new Attachment();

				riskTaskAttachment.setDescription(attachment.getDescription());
				riskTaskAttachment.setAttachmentsURL(attachment.getAttachmentsURL());
				riskTaskAttachment.setAttachmentResourceId(riskTask.getId());
				riskTaskAttachment.setContentType(attachment.getContentType());
				riskTaskAttachment.setContent(attachment.getContent());
				riskTaskAttachment.setFileName(attachment.getFileName());
				riskTaskAttachment.setCreatedDate(new Date());
				riskTaskAttachment.setAttachmentType(attachment.getAttachmentType());

				riskTaskAttachments.add(riskTaskAttachment);
			}
			attachmentRepository.save(riskTaskAttachments);
		}
	}
	
	/**
	 * @param riskTask
	 * @param templateTask
	 */
	public void associateTemplateURLs(RiskTask riskTask, RiskTask templateRiskTask) {
		List<SignalURL> riskTemplateTaskUrls = signalURLRepository.findByTopicId(templateRiskTask.getId());
		if (!CollectionUtils.isEmpty(riskTemplateTaskUrls)) {
			List<SignalURL> riskTaskURLs = new ArrayList<>();
			for (SignalURL url : riskTemplateTaskUrls) {
				SignalURL riskTaskURL = new SignalURL();
				riskTaskURL.setDescription(url.getDescription());
				riskTaskURL.setTopicId(riskTask.getId());
				riskTaskURL.setUrl(url.getUrl());
				riskTaskURL.setModifiedDate(new Date());
				riskTaskURLs.add(riskTaskURL);
			}
			signalURLRepository.save(riskTaskURLs);
		}
	}

	/**
	 * @param searchDto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SmtResponse findAllRiskPlansForSearch(SearchDto searchDto) {
		StringBuilder queryBuilder = new StringBuilder();
		List<String> whereClauses = new ArrayList<>();


		if (searchDto != null) {
			buildQuery(searchDto, queryBuilder, whereClauses);
		}else{
			queryBuilder.append("SELECT r.* FROM sm_risk_plan r ");
		}
		buildWhereConditions(searchDto, whereClauses);
		appendWhereClause(whereClauses, queryBuilder);
		
		queryBuilder.append(" order by created_date DESC");
		String queryStr = queryBuilder.toString();
		Query q = entityManager.createNativeQuery(queryStr, RiskPlan.class);
		setParameters(queryStr, searchDto, q);
		
		SmtResponse smtResponse=new SmtResponse();
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
		
		/**For adding TopicRiskPlanAssignmentAssignees to RiskPlan */
		if (smtResponse.getResult() != null) {
			List<RiskPlan> result = (List<RiskPlan>) smtResponse.getResult();
			associateRiskPlanAssignmentAssignees(result);
		}
		return smtResponse;
	}

	/**
	 * @param searchDto
	 * @param queryBuilder
	 * @param whereClauses
	 */
	private void buildQuery(SearchDto searchDto, StringBuilder queryBuilder,
			List<String> whereClauses) {
		queryBuilder.append(
				"SELECT distinct(r.*) FROM sm_risk_plan r left join  sm_assessment_plan a on   r.id=a.risk_plan_id left join sm_topic t on t.assessment_plan_id=a.id left outer join sm_topic_riskplan_assignment_assignees ra on r.id=ra.risk_id ");

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
		
		if (!CollectionUtils.isEmpty(searchDto.getHlts())) {
			queryBuilder.append(" inner join sm_hlt hlt on hlt.topic_id=t.id ");
			whereClauses.add(" hlt.hlt_name in :hltNames");
		}
		
		if (!CollectionUtils.isEmpty(searchDto.getHlgts())) {
			queryBuilder.append(" inner join sm_hlgt hlgt on hlgt.topic_id=t.id ");
			whereClauses.add(" hlgt.hlgt_name in :hlgtNames");
		}
		
		if (!CollectionUtils.isEmpty(searchDto.getPts())) {
			queryBuilder.append(" inner join sm_pt pt on pt.topic_id=t.id ");
			whereClauses.add(" pt.pt_name in :ptNames");
		}
		
		if (!CollectionUtils.isEmpty(searchDto.getSocs())) {
			queryBuilder.append(" inner join sm_soc soc on soc.topic_id=t.id ");
			whereClauses.add(" soc.soc_name in :socNames");
		}
		
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
			addRiskTaskStatus(searchDto, whereClauses);
			addStartDate(searchDto, whereClauses);
			addEndDate(searchDto, whereClauses);
			addSearchKeys(searchDto, whereClauses);
			addOwners(searchDto, whereClauses);
			addUserGroupUserKeys(searchDto, whereClauses);
			addUserKeys(searchDto, whereClauses);
			addOwnerUserKey(searchDto, whereClauses);
			addOwnerUserGroupKey(searchDto, whereClauses);
			addUserGroupKey(searchDto, whereClauses);
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
	private void addStatus(SearchDto searchDto, List<String> whereClauses) {
		if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
			whereClauses.add(" r.status in :statuses");
		}
	}
	

	/**
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addUserGroupKey(SearchDto searchDto, List<String> whereClauses) {
		 if(searchFilters.ifUserGroupKey(searchDto)&& ! searchFilters.isOwnerUserKey(searchDto)){
			whereClauses.add(" ra.user_group_key in :userGroupKeys ");
		}
	}
	/**
	 * 
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addSearchKeys(SearchDto searchDto, List<String> whereClauses){
		if(searchFilters.ifUserGroupKey(searchDto) && searchFilters.isOwnerUserKey(searchDto)){
			whereClauses.add(" r.owner in :owners or ra.user_group_key in :userGroupKeys or ra.user_key in :userKeys");
		}
	}
	/**
	 * 
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addOwners(SearchDto searchDto, List<String> whereClauses){
		if(searchFilters.ifOwner(searchDto) && !searchFilters.isGroupKeyUserKey(searchDto)){
			whereClauses.add("r.owner in :owners");
		}
	}
	/**
	 * 
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addUserGroupUserKeys(SearchDto searchDto, List<String> whereClauses){
		 if(searchFilters.isGroupKeyUserKey(searchDto)&& !searchFilters.ifOwner(searchDto)){
		whereClauses.add(" ra.user_group_key in :userGroupKeys or ra.user_key in :userKeys");
		 }
	}
	/**
	 * 
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addUserKeys(SearchDto searchDto, List<String> whereClauses){
		if(searchFilters.ifUserKey(searchDto)&& !searchFilters.isOwnerUserGRoupKey(searchDto)){
			whereClauses.add(" ra.user_key in :userKeys ");
		}
	}
	/**
	 * 
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addOwnerUserKey(SearchDto searchDto, List<String> whereClauses){
		if(searchFilters.isOwnerUserKey(searchDto)&& !searchFilters.ifUserGroupKey(searchDto)){
		whereClauses.add(" ra.user_key in :userKeys or r.owner in :owners ");
		}
	}
	/**
	 * 
	 * @param searchDto
	 * @param whereClauses
	 */
	private void addOwnerUserGroupKey(SearchDto searchDto, List<String> whereClauses){
		if(searchFilters.isOwnerUserGRoupKey(searchDto)&& !searchFilters.ifUserKey(searchDto)){
			whereClauses.add(" ra.user_group_key in :userGroupKeys or r.owner in :owners ");
		}
	}

	private void setParameters(String queryStr, SearchDto searchDto, Query query) {
		setInitialParameters(queryStr, searchDto, query);
		setSecondaryParameters(queryStr, searchDto, query);
	}

	/**
	 * @param queryStr
	 * @param searchDto
	 * @param query
	 */
	private void setSecondaryParameters(String queryStr, SearchDto searchDto,
			Query query) {
		if (queryStr.contains(":productNames")) {
			query.setParameter("productNames", searchDto.getProducts());
		}

		if (queryStr.contains(":ingredientNames")) {
			query.setParameter("ingredientNames", searchDto.getIngredients());
		}

		if (queryStr.contains(":licenseNames")) {
			query.setParameter("licenseNames", searchDto.getSocs());
		}
		
		if (queryStr.contains(":hltNames")) {
			query.setParameter("hltNames", searchDto.getHlts());
		}
		
		if (queryStr.contains(":hlgtNames")) {
			query.setParameter("hlgtNames", searchDto.getHlgts());
		}
		
		if (queryStr.contains(":ptNames")) {
			query.setParameter("ptNames", searchDto.getPts());
		}
		
		if (queryStr.contains(":socNames")) {
			query.setParameter("socNames", searchDto.getSocs());
		}
		if (queryStr.contains(":owners")) {
			query.setParameter("owners", searchDto.getOwners());
		}
		setAssignees(queryStr, searchDto, query);
	}

	/**
	 * @param queryStr
	 * @param searchDto
	 * @param query
	 */
	private void setAssignees(String queryStr, SearchDto searchDto, Query query) {
		if (queryStr.contains(":userKeys")) {
			query.setParameter("userKeys", searchDto.getUserKeys());
		}

		if (queryStr.contains(":userGroupKeys")) {
			query.setParameter("userGroupKeys", searchDto.getUserGroupKeys());
		}

	}

	/**
	 * @param queryStr
	 * @param searchDto
	 * @param query
	 */
	private void setInitialParameters(String queryStr, SearchDto searchDto,
			Query query) {
		if (queryStr.contains(":statuses")) {
			query.setParameter("statuses", searchDto.getStatuses());
		}

		if (queryStr.contains(":riskTaskStatus")) {
			query.setParameter("riskTaskStatus", searchDto.getRiskTaskStatus());
		}

		if (queryStr.contains(":startDate")) {
			query.setParameter("startDate", searchDto.getStartDate());
		}

		if (queryStr.contains(":endDate")) {
			query.setParameter("endDate", searchDto.getEndDate());
		}
	}
	/**
	 * 
	 * @param riskTask
	 * @param attachments
	 * @throws IOException
	 * @throws ApplicationException
	 */
	public void createRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
		    
		Date d = new Date();
		riskTask.setCreatedDate(d);
		riskTask.setLastUpdatedDate(d);
		riskTask.setStatus("New");
		if(riskTask.getRiskId() != null){
			RiskPlan riskPlan = riskPlanRepository.findOne(Long.valueOf(riskTask.getRiskId()));
			riskTask.setAssignTo(riskPlan.getAssignTo());
			riskTask.setOwner(riskPlan.getAssignTo());
		}

		Long riskTaskExists=riskTaskRepository.countByNameIgnoreCaseAndRiskId(riskTask.getName(),riskTask.getRiskId());
		if (riskTaskExists > 0) {
			throw exceptionBuilder.buildException(ErrorType.RISKPACTION_NAME_DUPLICATE, null);
		}
		
		RiskTask riskTaskUpdated = riskTaskRepository.save(riskTask);
		checkRiskTaskStatus(riskTaskUpdated);
		List<Attachment> attachmentList = attachmentService.addAttachments(riskTaskUpdated.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				null, riskTaskUpdated.getFileMetadata(), riskTaskUpdated.getCreatedBy());
		if (!CollectionUtils.isEmpty(riskTaskUpdated.getSignalUrls())) {
			for (SignalURL url : riskTaskUpdated.getSignalUrls()) {
				url.setTopicId(riskTaskUpdated.getId());
				url.setModifiedDate(new Date());
			}
			signalURLRepository.save(riskTaskUpdated.getSignalUrls());
		}
		
		signalAuditService.saveOrUpdateRiskTaskAudit(riskTaskUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
	}
	/**
	 * 
	 * @param riskTask
	 */
	private void checkRiskTaskStatus(RiskTask riskTask){
		List<RiskTask> risks = findAllByRiskId(riskTask.getRiskId(), null);
		boolean allTasksCompletedFlag = true;
		if (!CollectionUtils.isEmpty(risks)) {
			for (RiskTask risk : risks) {
				if (!SmtConstant.COMPLETED.getDescription().equals(risk.getStatus())) {
					allTasksCompletedFlag = false;
				}
			}
		}
		if(allTasksCompletedFlag){
			riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(riskTask.getRiskId()));
		}
		else{
			riskPlanRepository.updateRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription(),  Long.valueOf(riskTask.getRiskId()));
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

	public void delete(Long riskTaskId) throws ApplicationException {
		RiskTask riskTask = riskTaskRepository.findOne(riskTaskId);
		if (riskTask == null) {
			throw new ApplicationException("Failed to delete Action. Invalid Id received");
		}
		
		riskTaskRepository.delete(riskTask);
		checkRiskTaskStatus(riskTask);
	}

	public void updateRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
		if (riskTask.getId() == null) {
			throw new ApplicationException("Failed to update Action. Invalid Id received");
		}
		
		String riskTaskOriginal = JsonUtil.converToJson(riskTaskRepository.findOne(riskTask.getId()));
		
		riskTask.setLastUpdatedDate(new Date());
		riskTaskRepository.save(riskTask);
		List<Attachment> attachmentList = attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT,
				riskTask.getDeletedAttachmentIds(), riskTask.getFileMetadata(), riskTask.getCreatedBy());
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
				url.setModifiedDate(new Date());
			}
			signalURLRepository.save(riskTask.getSignalUrls());
		}
		if (allTasksCompletedFlag) {
			String riskPlanOriginal = JsonUtil.converToJson(riskPlanRepository.findOne(Long.parseLong(riskTask.getRiskId())));
			riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(riskTask.getRiskId()));
			signalAuditService.saveOrUpdateRiskPlanAudit(riskPlanRepository.findOne(Long.parseLong(riskTask.getRiskId())), riskPlanOriginal, null, SmtConstant.UPDATE.getDescription());
		}
		
		signalAuditService.saveOrUpdateRiskTaskAudit(riskTask, riskTaskOriginal, attachmentList, SmtConstant.UPDATE.getDescription());
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
		List<TopicRiskPlanAssignmentAssignees> topicRiskPlanAssignmentAssigneesList=topicRiskPlanAssignmentAssigneesRepository.findByRiskId(riskId);
		riskPlan.setTopicRiskPlanAssignmentAssignees(topicRiskPlanAssignmentAssigneesList);
		riskPlan.setComments(commentsRepository.findByRiskPlanId(riskId));
		riskPlan.setSignalUrls(signalURLRepository.findByTopicId(riskId));
		AssessmentPlan assessmentPlan=riskPlan.getAssessmentPlan();
		if(null!=assessmentPlan){
		assessmentPlan.setSignalUrls(signalURLRepository.findByTopicId(assessmentPlan.getId()));
		}
		checkRiskTaskStatus(riskPlan);
		return riskPlan;
	}

	public void riskPlanSummary(RiskPlan riskPlan, MultipartFile[] attachments) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		riskPlan.setLastModifiedDate(new Date());
		riskPlan.setStatus(SmtConstant.COMPLETED.getDescription());
		attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT,
				riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata(), riskPlan.getCreatedBy());
		riskPlanRepository.save(riskPlan);
	}

	public void updateRiskPlan(RiskPlan riskPlan, MultipartFile[] attachments) throws ApplicationException {
		if (riskPlan.getId() == null) {
			throw new ApplicationException("Failed to update Risk. Invalid Id received");
		}
		String riskPlanOriginal = JsonUtil.converToJson(riskPlanRepository.findOne(riskPlan.getId()));
		
		riskPlan.setLastModifiedDate(new Date());
		
		if(null==riskPlan.getSummary()|| riskPlan.getSummary().isEmpty()){
		riskPlan.setSummary(SmtConstant.SUMMARY.getDescription());
			if(riskPlan.getStatus().equals(SmtConstant.COMPLETED.getDescription())){
				riskPlan.setSummary(SmtConstant.SUMMARY_COMPLETED.getDescription());
			}
		}
		else{
			riskPlan.setSummary(riskPlan.getSummary());
		}
		List<Attachment> attachmentList = attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT,
				riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata(), riskPlan.getCreatedBy());
		setRiskTaskStatus(riskPlan);
		RiskPlan riskPlanUpdated = riskPlanRepository.save(riskPlan);
		List<TopicRiskPlanAssignmentAssignees> assigneeList = riskPlan.getTopicRiskPlanAssignmentAssignees();
		if(!CollectionUtils.isEmpty(assigneeList)){
			topicRiskPlanAssignmentAssigneesRepository.deleteByRiskId(riskPlanUpdated.getId());
			for (TopicRiskPlanAssignmentAssignees assignee : assigneeList) {
				assignee.setRiskId(riskPlanUpdated.getId());
			}
			topicRiskPlanAssignmentAssigneesRepository.save(assigneeList);
		}
		
		List<Comments> list = riskPlan.getComments();
		if (!CollectionUtils.isEmpty(list)) {
			for (Comments comment : list) {
				comment.setRiskPlanId(riskPlan.getId());
				comment.setModifiedDate(new Date());
			}
		}
		commentsRepository.save(riskPlan.getComments());
		
		updateSignalUrl(riskPlan);
		signalAuditService.saveOrUpdateRiskPlanAudit(riskPlan, riskPlanOriginal, attachmentList, SmtConstant.UPDATE.getDescription());
	}
	
	
	/**
	 * This method sets the risk task status as completed 
     * when all tasks are completed else Not completed
	 * @param riskPlan
	 */
	private void setRiskTaskStatus(RiskPlan riskPlan){
		boolean riskTaskStatus=false;
		List<RiskTask> riskTaskStatues=	riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(String.valueOf(riskPlan.getId()));
		 if(!CollectionUtils.isEmpty(riskTaskStatues)){
			 for(RiskTask riskTask:riskTaskStatues){
				 if(!riskTask.getStatus().equals(SmtConstant.COMPLETED.getDescription())){
					 riskTaskStatus=true;
				 }
			 }
			 if(riskTaskStatus){
				 riskPlan.setRiskTaskStatus(SmtConstant.NOTCOMPLETED.getDescription());
			 }
		 }
	}
		 
	public List<RiskTask> associateRiskTemplateTasks(RiskPlan riskPlan) {
		return associateRiskTasks(riskPlan);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @throws ApplicationException
	 */
	public void updateRiskName(Long id,String name) throws ApplicationException{
		List<RiskPlan> list = riskPlanRepository.findAll();
		List<String> riskPlanNames = riskPlanRepository.findByRiskName(name, id);

		for (RiskPlan riskPlan : list) {

			if (id.equals(riskPlan.getId()) && riskPlanNames.contains(name)) {
				throw exceptionBuilder.buildException(ErrorType.RISKPLAN_NAME_DUPLICATE, null);
			} else if (id.equals(riskPlan.getId()) && !riskPlanNames.contains(name)) {
				riskPlan.setLastModifiedDate(new Date());
				riskPlan.setName(name);
				riskPlanRepository.save(riskPlan);
			}
		}
	}
}
