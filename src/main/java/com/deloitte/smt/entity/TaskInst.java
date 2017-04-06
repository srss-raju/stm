package com.deloitte.smt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Entity
@Table(name = "act_hi_taskinst")
public class TaskInst {

    @Id
    @Column(name = "id_")
    private String id;
    @Column(name = "task_def_key_")
    private String taskDefKey;
    @Column(name = "proc_def_key_")
    private String procDefKey;
    @Column(name = "proc_def_id_")
    private String procDefId;
    @Column(name = "proc_inst_id_")
    private String procInstId;
    @Column(name = "execution_id_")
    private String executionId;
    @Column(name = "case_def_key_")
    private String caseDefKey;
    @Column(name = "case_def_id_")
    private String caseDefId;
    @Column(name = "case_inst_id_")
    private String caseInstId;
    @Column(name = "case_execution_id_")
    private String caseExecutionId;
    @Column(name = "act_inst_id_")
    private String actInstId;
    @Column(name = "name_")
    private String name;
    @Column(name = "parent_task_id_")
    private String parentTaskId;
    @Column(name = "description_")
    private String description;
    @Column(name = "owner_")
    private String owner;
    @Column(name = "assignee_")
    private String assignee;
    @Column(name = "start_time_")
    private Date startTime;
    @Column(name = "end_time_")
    private Date endTime;
    @Column(name = "duration_")
    private Long duration;
    @Column(name = "delete_reason_")
    private String deleteReason;
    @Column(name = "priority_")
    private int priority;
    @Column(name = "due_date_")
    private Date dueDate;
    @Column(name = "follow_up_date_")
    private Date followUpDate;
    @Column(name = "tenant_id_")
    private String tenantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getCaseDefKey() {
        return caseDefKey;
    }

    public void setCaseDefKey(String caseDefKey) {
        this.caseDefKey = caseDefKey;
    }

    public String getCaseDefId() {
        return caseDefId;
    }

    public void setCaseDefId(String caseDefId) {
        this.caseDefId = caseDefId;
    }

    public String getCaseInstId() {
        return caseInstId;
    }

    public void setCaseInstId(String caseInstId) {
        this.caseInstId = caseInstId;
    }

    public String getCaseExecutionId() {
        return caseExecutionId;
    }

    public void setCaseExecutionId(String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
