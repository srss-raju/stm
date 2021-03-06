package com.deloitte.smt.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by myelleswarapu on 14-04-2017.
 */
public class SearchDto {

	
	private List<String> statuses;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdDate;
	private List<String> ingredients;
	private List<String> products;
	private List<String> licenses;
	private List<String> socs;
	private List<String> hlgts = new ArrayList<>();
	private List<String> hlts;
	private List<String> pts;
	private List<?> assignees;
	private String description;
	private List<String> signalConfirmations;
	private List<String> signalNames;
	private String ingredient;
	private List<Long> userKeys = new ArrayList<>();
	private List<Long> userGroupKeys = new ArrayList<>();
	private String owner;
	private List<String> frequency;
	private List<String> sources;
	private List<String> finalDispositions;
	private List<Integer> smqIds;
	
	private String dateKey;
	
	private List<String> owners;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private boolean isDueDate;
	private List<String> assessmentTaskStatus;
	private List<String> riskTaskStatus;

	private boolean gantt;
	
	private int fetchSize;
	private int fromRecord;

	public boolean isGantt() {
		return gantt;
	}

	public void setGantt(boolean gantt) {
		this.gantt = gantt;
	}

	public List<String> getStatuses() {
		return statuses;
	}

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }

	public List<String> getSocs() {
		return socs;
	}

	public void setSocs(List<String> socs) {
		this.socs = socs;
	}

	public List<String> getHlgts() {
		return hlgts;
	}

	public void setHlgts(List<String> hlgts) {
		this.hlgts = hlgts;
	}

	public List<String> getHlts() {
		return hlts;
	}

	public void setHlts(List<String> hlts) {
		this.hlts = hlts;
	}

	public List<String> getPts() {
		return pts;
	}

	public void setPts(List<String> pts) {
		this.pts = pts;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSignalConfirmations() {
        return signalConfirmations;
    }

    public void setSignalConfirmations(List<String> signalConfirmations) {
        this.signalConfirmations = signalConfirmations;
    }

    public List<String> getSignalNames() {
        return signalNames;
    }

    public void setSignalNames(List<String> signalNames) {
        this.signalNames = signalNames;
    }

	public boolean isDueDate() {
		return isDueDate;
	}

	public void setDueDate(boolean isDueDate) {
		this.isDueDate = isDueDate;
	}

	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<String> getFrequency() {
		return frequency;
	}

	public void setFrequency(List<String> frequency) {
		this.frequency = frequency;
	}
	
	public List<String> getRiskTaskStatus() {
		return riskTaskStatus;
	}

	public void setRiskTaskStatus(List<String> riskTaskStatus) {
		this.riskTaskStatus = riskTaskStatus;
	}

	public List<String> getAssessmentTaskStatus() {
		return assessmentTaskStatus;
	}

	public void setAssessmentTaskStatus(List<String> assessmentTaskStatus) {
		this.assessmentTaskStatus = assessmentTaskStatus;
	
	}

	public List<Long> getUserKeys() {
		return userKeys;
	}

	public void setUserKeys(List<Long> userKeys) {
		this.userKeys = userKeys;
	}

	public List<Long> getUserGroupKeys() {
		return userGroupKeys;
	}

	public void setUserGroupKeys(List<Long> userGroupKeys) {
		this.userGroupKeys = userGroupKeys;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public List<String> getFinalDispositions() {
		return finalDispositions;
	}

	public void setFinalDispositions(List<String> finalDispositions) {
		this.finalDispositions = finalDispositions;
	}

	public List<Integer> getSmqIds() {
		return smqIds;
	}

	public void setSmqIds(List<Integer> smqIds) {
		this.smqIds = smqIds;
	}

	public List<?> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<?> assignees) {
		this.assignees = assignees;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public List<String> getOwners() {
		return owners;
	}

	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
	public String getDateKey() {
		return dateKey;
	}

	public void setDateKey(String dateKey) {
		this.dateKey = dateKey;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public int getFromRecord() {
		return fromRecord;
	}

	public void setFromRecord(int fromRecord) {
		this.fromRecord = fromRecord;
	}

}
