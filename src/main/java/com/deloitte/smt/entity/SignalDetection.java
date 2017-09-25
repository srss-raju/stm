package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Rajesh on 21-04-2017.
 */
@Entity
@Table(name = "sm_signal_detection")
public class SignalDetection implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	private String description;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	private String query;
	private String runFrequency;
	private String windowType;
	private Date lastRunDate;
	private Date nextRunDate;
	private Long signalDetected;
	private String caselistId;
	private Long casesCount;
	private Long minCases;
	private boolean socSelected;
	
	public boolean isSocSelected() {
		return socSelected;
	}

	public void setSocSelected(boolean socSelected) {
		this.socSelected = socSelected;
	}

	@Transient
	private List<QueryBuilder> queryBuilder;
	
	private String owner;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detectionId")
	private List<TopicSignalDetectionAssignmentAssignees> topicSignalDetectionAssignmentAssignees;

	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper mapper=new ObjectMapper();
		
		SignalDetection signalDetection=new SignalDetection();
		String json=mapper.writeValueAsString(signalDetection);
		
		System.out.println(json);
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "signalDetection")
	private Set<SignalDetectionStatistics> signalDetectionStatistics;

	@Transient
	private List<Ingredient> ingredients;
	@Transient
	private List<Soc> socs;
	
	@Transient
	private List<Smq> smqs;
	
	@Transient
	private List<DenominatorForPoisson> denominatorForPoisson;

	@Transient
	private List<IncludeAE> includeAEs;

	@Transient
	private List<Date> nextRunDates;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getRunFrequency() {
		return runFrequency;
	}

	public void setRunFrequency(String runFrequency) {
		this.runFrequency = runFrequency;
	}

	public Set<SignalDetectionStatistics> getSignalDetectionStatistics() {
		return signalDetectionStatistics;
	}

	public void setSignalDetectionStatistics(Set<SignalDetectionStatistics> signalDetectionStatistics) {
		this.signalDetectionStatistics = signalDetectionStatistics;
	}

	public List<Soc> getSocs() {
		return socs;
	}

	public void setSocs(List<Soc> socs) {
		this.socs = socs;
	}

	public List<DenominatorForPoisson> getDenominatorForPoisson() {
		return denominatorForPoisson;
	}

	public void setDenominatorForPoisson(List<DenominatorForPoisson> denominatorForPoisson) {
		this.denominatorForPoisson = denominatorForPoisson;
	}

	public List<IncludeAE> getIncludeAEs() {
		return includeAEs;
	}

	public void setIncludeAEs(List<IncludeAE> includeAEs) {
		this.includeAEs = includeAEs;
	}

	public String getWindowType() {
		return windowType;
	}

	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}

	public Date getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public List<Date> getNextRunDates() {
		return nextRunDates;
	}

	public void setNextRunDates(List<Date> nextRunDates) {
		this.nextRunDates = nextRunDates;
	}

	public Long getSignalDetected() {
		return signalDetected;
	}

	public void setSignalDetected(Long signalDetected) {
		this.signalDetected = signalDetected;
	}

	public Date getLastRunDate() {
		return lastRunDate;
	}

	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}

	public List<QueryBuilder> getQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(List<QueryBuilder> queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	public String getCaselistId() {
		return caselistId;
	}

	public void setCaselistId(String caselistId) {
		this.caselistId = caselistId;
	}

	public Long getCasesCount() {
		return casesCount;
	}

	public void setCasesCount(Long casesCount) {
		this.casesCount = casesCount;
	}

	public Long getMinCases() {
		return minCases;
	}

	public void setMinCases(Long minCases) {
		this.minCases = minCases;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<TopicSignalDetectionAssignmentAssignees> getTopicSignalDetectionAssignmentAssignees() {
		return topicSignalDetectionAssignmentAssignees;
	}

	public void setTopicSignalDetectionAssignmentAssignees(
			List<TopicSignalDetectionAssignmentAssignees> topicSignalDetectionAssignmentAssignees) {
		this.topicSignalDetectionAssignmentAssignees = topicSignalDetectionAssignmentAssignees;
	}

	public List<Smq> getSmqs() {
		return smqs;
	}

	public void setSmqs(List<Smq> smqs) {
		this.smqs = smqs;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

}
