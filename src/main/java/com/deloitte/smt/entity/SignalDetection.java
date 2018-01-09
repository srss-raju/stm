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

import lombok.Data;

/**
 * Created by Rajesh on 21-04-2017.
 */
@Data
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
	
	@OneToMany
	private List<QueryBuilder> queryBuilder;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detectionId")
	private List<Query> queries;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detectionId")
	private List<Geography> geography;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detectionId")
	private List<DetectionAssignees> detectionAssignees;
	
	private String owner;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "signalDetection")
	private Set<SignalDetectionStatistics> signalDetectionStatistics;

	@OneToMany
	private List<Soc> socs;
	
	@OneToMany
	private List<Smq> smqs;
	
	@OneToMany
	private List<DenominatorForPoission> denominatorForPoission;

	@OneToMany
	private List<IncludeAE> includeAEs;

	@OneToMany
	private List<Date> nextRunDates;
	
	@OneToMany
    private List<TopicCondition> conditions;
	
	@OneToMany
    private List<TopicProduct> products;

}
