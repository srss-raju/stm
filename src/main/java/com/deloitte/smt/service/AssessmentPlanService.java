package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TopicRepository;

/**
 * Created by myelleswarapu on 10-04-2017.
 */
@Transactional
@Service
public class AssessmentPlanService {

    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LicenseRepository licenseRepository;

    @Autowired
    IngredientRepository ingredientRepository;
    
    @Autowired
    CommentsRepository commentsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    SearchService searchService;
    
    @Autowired
    SignalURLRepository signalURLRepository;

    public AssessmentPlan findById(Long assessmentId) throws ApplicationException {
        AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
        if(assessmentPlan == null) {
            throw new ApplicationException("Assessment Plan not found with given Id :"+assessmentId);
        }
        if("New".equalsIgnoreCase(assessmentPlan.getAssessmentPlanStatus())) {
            assessmentPlan.setAssessmentPlanStatus("In Progress");
            assessmentPlan = assessmentPlanRepository.save(assessmentPlan);
        }
        assessmentPlan.setComments(commentsRepository.findByAssessmentId(assessmentId));
        assessmentPlan.setSignalUrls(signalURLRepository.findByTopicId(assessmentId));
        return assessmentPlan;
    }

    public void unlinkSignalToAssessment(Long assessmentId, Long topicId){
        Topic t = null;
        AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
        if(assessmentPlan != null && !CollectionUtils.isEmpty(assessmentPlan.getTopics())) {
            for(Topic topic : assessmentPlan.getTopics())  {
                if(topic.getId() == topicId) {
                    t = topic;
                    break;
                }
            }
            if(t != null) {
                assessmentPlan.getTopics().remove(t);
                t.setAssessmentPlan(null);
                assessmentPlanRepository.save(assessmentPlan);
                topicRepository.save(t);
            }
        }
    }

	public List<AssessmentPlan> findAllAssessmentPlans(SearchDto searchDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssessmentPlan> criteriaQuery = criteriaBuilder.createQuery(AssessmentPlan.class);

		Root<Topic> topic = criteriaQuery.from(Topic.class);
		Join<Topic, AssessmentPlan> topicAssignmentJoin = topic.join("assessmentPlan", JoinType.INNER);
		Join<AssessmentPlan,RiskPlan> assementRiskJoin=topicAssignmentJoin.join(SmtConstant.RISK_PLAN.getDescription(),JoinType.LEFT);
		

		if (null != searchDto) {
			List<Predicate> predicates = new ArrayList<>(10);
			
			if (!CollectionUtils.isEmpty(searchDto.getRiskTaskStatus())) {
				predicates.add(criteriaBuilder.isTrue(assementRiskJoin.get("riskTaskStatus").in(searchDto.getRiskTaskStatus())));
				
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
				predicates.add(criteriaBuilder.isTrue(topicAssignmentJoin.get(SmtConstant.ASSESSMENT_PLAN_STATUS.getDescription()).in(searchDto.getStatuses())));
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getAssessmentTaskStatus())) {
				predicates.add(criteriaBuilder.isTrue(topicAssignmentJoin.get(SmtConstant.ASSESSMENT_TASK_STATUS.getDescription()).in(searchDto.getAssessmentTaskStatus())));
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
				predicates.add(criteriaBuilder.isTrue(topicAssignmentJoin.get(SmtConstant.ASSIGN_TO.getDescription()).in(searchDto.getAssignees())));
			}

			if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
				Root<Product> rootProduct = criteriaQuery.from(Product.class);
				Predicate productEquals = criteriaBuilder.equal(topic.get("id"),
						rootProduct.get(SmtConstant.TOPIC_ID.getDescription()));
				Predicate producNameEquals = criteriaBuilder
						.isTrue(rootProduct.get("productName").in(searchDto.getProducts()));
				predicates.add(productEquals);
				predicates.add(producNameEquals);
			}

			if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
				Root<License> rootLicense = criteriaQuery.from(License.class);
				Predicate licenseEquals = criteriaBuilder.equal(topic.get("id"), rootLicense.get(SmtConstant.TOPIC_ID.getDescription()));
				Predicate licenseNameEquals = criteriaBuilder.isTrue(rootLicense.get("licenseName").in(searchDto.getLicenses()));
				predicates.add(licenseEquals);
				predicates.add(licenseNameEquals);
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getIngredients())) {
				Root<Ingredient> rootIngredient = criteriaQuery.from(Ingredient.class);
				Predicate ingredientEquals = criteriaBuilder.equal(topic.get("id"), rootIngredient.get(SmtConstant.TOPIC_ID.getDescription()));
				Predicate ingredientNameEquals = criteriaBuilder.isTrue(rootIngredient.get("ingredientName").in(searchDto.getIngredients()));
				predicates.add(ingredientEquals);
				predicates.add(ingredientNameEquals);
			}
			
			if (!CollectionUtils.isEmpty(searchDto.getHlts())) {
				Root<Hlt> rootHlt = criteriaQuery.from(Hlt.class);
				Predicate hltTopicEquals = criteriaBuilder.equal(topic.get("id"), rootHlt.get(SmtConstant.TOPIC_ID.getDescription()));
				Predicate hltNameEquals = criteriaBuilder.isTrue(rootHlt.get("hltName").in(searchDto.getHlts()));
				predicates.add(hltTopicEquals);
				predicates.add(hltNameEquals);
			}

			if (!CollectionUtils.isEmpty(searchDto.getHlgts())) {
				Root<Hlgt> rootHlgt = criteriaQuery.from(Hlgt.class);
				Predicate hlgtTopicEquals = criteriaBuilder.equal(topic.get("id"), rootHlgt.get(SmtConstant.TOPIC_ID.getDescription()));
				Predicate hlgtNameEquals = criteriaBuilder.isTrue(rootHlgt.get("hlgtName").in(searchDto.getHlgts()));
				predicates.add(hlgtTopicEquals);
				predicates.add(hlgtNameEquals);
			}

			if (!CollectionUtils.isEmpty(searchDto.getPts())) {
				Root<Pt> rootPt = criteriaQuery.from(Pt.class);
				Predicate ptTopicEquals = criteriaBuilder.equal(topic.get("id"), rootPt.get(SmtConstant.TOPIC_ID.getDescription()));
				Predicate ptNameEquals = criteriaBuilder.isTrue(rootPt.get("ptName").in(searchDto.getPts()));
				predicates.add(ptTopicEquals);
				predicates.add(ptNameEquals);
			}
			
			if (null != searchDto.getStartDate()) {
				if (searchDto.isDueDate()) {
					predicates.add(
							criteriaBuilder.greaterThanOrEqualTo(topicAssignmentJoin.get(SmtConstant.ASSESSMENT_DUE_DATE.getDescription()), searchDto.getStartDate()));
					if(null!=searchDto.getEndDate()){
						predicates.add(criteriaBuilder.lessThanOrEqualTo(topicAssignmentJoin.get(SmtConstant.ASSESSMENT_DUE_DATE.getDescription()), searchDto.getEndDate()));
					}
					
				} else {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription()),	searchDto.getStartDate()));
					if(null!=searchDto.getEndDate()){
						predicates.add(
								criteriaBuilder.lessThanOrEqualTo(topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription()), searchDto.getEndDate()));
					}
					
				}

			}
			
			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			criteriaQuery.select(criteriaBuilder.construct(AssessmentPlan.class, topicAssignmentJoin.get("id"),
					topicAssignmentJoin.get("assessmentName"), topicAssignmentJoin.get("priority"),
					topicAssignmentJoin.get("inDays"), topicAssignmentJoin.get("ingrediantName"),
					topicAssignmentJoin.get("source"), topicAssignmentJoin.get("caseInstanceId"),
					topicAssignmentJoin.get(SmtConstant.ASSESSMENT_PLAN_STATUS.getDescription()), topicAssignmentJoin.get("assessmentRiskStatus"),
					topicAssignmentJoin.get(SmtConstant.ASSESSMENT_DUE_DATE.getDescription()), topicAssignmentJoin.get("finalAssessmentSummary"),
					topicAssignmentJoin.get(SmtConstant.RISK_PLAN.getDescription()), topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription()),
					topicAssignmentJoin.get("createdBy"), topicAssignmentJoin.get("lastModifiedDate"),
					topicAssignmentJoin.get(SmtConstant.ASSIGN_TO.getDescription()), topicAssignmentJoin.get(SmtConstant.ASSESSMENT_TASK_STATUS.getDescription())))
					.where(andPredicate).orderBy(criteriaBuilder.desc(topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription())));
		} else {
			criteriaQuery.select(criteriaBuilder.construct(AssessmentPlan.class, topicAssignmentJoin.get("id"),
					topicAssignmentJoin.get("assessmentName"), topicAssignmentJoin.get("priority"),
					topicAssignmentJoin.get("inDays"), topicAssignmentJoin.get("ingrediantName"),
					topicAssignmentJoin.get("source"), topicAssignmentJoin.get("caseInstanceId"),
					topicAssignmentJoin.get(SmtConstant.ASSESSMENT_PLAN_STATUS.getDescription()), topicAssignmentJoin.get("assessmentRiskStatus"),
					topicAssignmentJoin.get(SmtConstant.ASSESSMENT_DUE_DATE.getDescription()), topicAssignmentJoin.get("finalAssessmentSummary"),
					topicAssignmentJoin.get(SmtConstant.RISK_PLAN.getDescription()), topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription()),
					topicAssignmentJoin.get("createdBy"), topicAssignmentJoin.get("lastModifiedDate"),
					topicAssignmentJoin.get(SmtConstant.ASSIGN_TO.getDescription()), topicAssignmentJoin.get(SmtConstant.ASSESSMENT_TASK_STATUS.getDescription())))
			.orderBy(criteriaBuilder.desc(topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription())));
		}

		TypedQuery<AssessmentPlan> q = entityManager.createQuery(criteriaQuery);
		return q.getResultList();
	}

    public void updateAssessment(AssessmentPlan assessmentPlan, MultipartFile[] attachments) throws ApplicationException, IOException {
        if(assessmentPlan.getId() == null) {
            throw new ApplicationException("Failed to update Assessment. Invalid Id received");
        }
        assessmentPlan.setLastModifiedDate(new Date());
        attachmentService.addAttachments(assessmentPlan.getId(), attachments, AttachmentType.ASSESSMENT_ATTACHMENT, assessmentPlan.getDeletedAttachmentIds(), assessmentPlan.getFileMetadata());
        assessmentPlanRepository.save(assessmentPlan);
        List<Comments> list = assessmentPlan.getComments();
        if(!CollectionUtils.isEmpty(list)){
        	for(Comments comment:list){
        		comment.setAssessmentId(assessmentPlan.getId());
        		comment.setModifiedDate(new Date());
        	}
        }
        commentsRepository.save(assessmentPlan.getComments());
        if(!CollectionUtils.isEmpty(assessmentPlan.getSignalUrls())){
        	for(SignalURL url:assessmentPlan.getSignalUrls()){
        		url.setTopicId(assessmentPlan.getId());
        	}
        	signalURLRepository.save(assessmentPlan.getSignalUrls());
        }
    }

    public void finalAssessment(AssessmentPlan assessmentPlan, MultipartFile[] attachments) throws ApplicationException, IOException {
        if(assessmentPlan.getId() == null) {
            throw new ApplicationException("Failed to update Assessment. Invalid Id received");
        }
        assessmentPlan.setLastModifiedDate(new Date());
        assessmentPlan.setAssessmentPlanStatus("Completed");
        attachmentService.addAttachments(assessmentPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT, assessmentPlan.getDeletedAttachmentIds(), assessmentPlan.getFileMetadata());
        assessmentPlanRepository.save(assessmentPlan);
    }
}
