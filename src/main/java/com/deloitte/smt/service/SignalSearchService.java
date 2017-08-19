package com.deloitte.smt.service;

import java.util.ArrayList;
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

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicSignalValidationAssignmentAssignees;

@Service
public class SignalSearchService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
			addDueDate(searchDto, criteriaBuilder, rootTopic, predicates);
			addSignalConfirmations(searchDto, criteriaBuilder, rootTopic, predicates);
			/**TopicSignalValidationAssignmentAssignees **/
			addUserKeys(searchDto, criteriaBuilder,rootTopic,predicates);
			addUserGroupKeys(searchDto, criteriaBuilder, rootTopic,predicates);

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
	/**
	 * 
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addUserKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic, List<Predicate> predicates) {
			  
			  if (!CollectionUtils.isEmpty(searchDto.getUserKeys())) {
			   Join<Topic,TopicSignalValidationAssignmentAssignees> joinAssignees = rootTopic.join("topicSignalValidationAssignmentAssignees", JoinType.LEFT); //left outer join
			   predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinAssignees.get("userKey").in(searchDto.getUserKeys())), criteriaBuilder.isTrue(rootTopic.get("owner").in(searchDto.getOwner()))));
			      
			  }
			 }
	/**
	 * 
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param query
	 * @param rootTopic
	 * @param predicates
	 */
	private void addUserGroupKeys(SearchDto searchDto, CriteriaBuilder criteriaBuilder, Root<Topic> rootTopic, List<Predicate> predicates) {
			  
			  if (!CollectionUtils.isEmpty(searchDto.getUserKeys())) {
			   Join<Topic,TopicSignalValidationAssignmentAssignees> joinAssignees = rootTopic.join("topicSignalValidationAssignmentAssignees", JoinType.LEFT); //left outer join
			   predicates.add(criteriaBuilder.or(criteriaBuilder.isTrue(joinAssignees.get("userGroupKey").in(searchDto.getUserKeys())), criteriaBuilder.isTrue(rootTopic.get("owner").in(searchDto.getOwner()))));
			      
			  }
			 }

}
