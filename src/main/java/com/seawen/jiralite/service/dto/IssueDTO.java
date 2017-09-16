package com.seawen.jiralite.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Issue entity.
 */
public class IssueDTO implements Serializable {

    private Long id;

    @Size(max = 60)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 60)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Integer version;

    @NotNull
    @Size(max = 30)
    private String issueNo;

    @Size(max = 500)
    private String issueSubject;

    @NotNull
    @Size(max = 30)
    private String issueType;

    @NotNull
    @Size(max = 30)
    private String issuePriority;

    @Size(max = 12)
    private String issueStatus;

    @Size(max = 60)
    private String reporter;

    @Size(max = 60)
    private String assigner;

    @NotNull
    @Lob
    private String remark;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getIssueSubject() {
        return issueSubject;
    }

    public void setIssueSubject(String issueSubject) {
        this.issueSubject = issueSubject;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(String issuePriority) {
        this.issuePriority = issuePriority;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueDTO issueDTO = (IssueDTO) o;
        if(issueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueDTO{" +
            "id=" + getId() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", version='" + getVersion() + "'" +
            ", issueNo='" + getIssueNo() + "'" +
            ", issueSubject='" + getIssueSubject() + "'" +
            ", issueType='" + getIssueType() + "'" +
            ", issuePriority='" + getIssuePriority() + "'" +
            ", issueStatus='" + getIssueStatus() + "'" +
            ", reporter='" + getReporter() + "'" +
            ", assigner='" + getAssigner() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
