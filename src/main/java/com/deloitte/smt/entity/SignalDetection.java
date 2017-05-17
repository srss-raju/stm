package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Rajesh on 21-04-2017.
 */
@Entity
@Table(name = "sm_signal_detection")
public class SignalDetection implements Serializable{

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy="signalDetection")
    private Set<SignalDetectionStatistics> signalDetectionStatistics;

    @Transient
    private Ingredient ingredient;
    
    @Transient
    private List<Soc> socs;
    
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

	public void setSignalDetectionStatistics(
			Set<SignalDetectionStatistics> signalDetectionStatistics) {
		this.signalDetectionStatistics = signalDetectionStatistics;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
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

	public void setDenominatorForPoisson(
			List<DenominatorForPoisson> denominatorForPoisson) {
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

    }
