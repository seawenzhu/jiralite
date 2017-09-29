package com.seawen.jiralite.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "issue")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 60)
    @Column(name = "created_by", length = 60)
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 60)
    @Column(name = "last_modified_by", length = 60)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "version")
    private Integer version;

    @NotNull
    @Size(max = 30)
    @Column(name = "issue_no", length = 30, nullable = false)
    private String issueNo;

    @Size(max = 500)
    @Column(name = "issue_subject", length = 500)
    private String issueSubject;

    @NotNull
    @Size(max = 30)
    @Column(name = "issue_type", length = 30, nullable = false)
    private String issueType;

    @NotNull
    @Size(max = 30)
    @Column(name = "issue_priority", length = 30, nullable = false)
    private String issuePriority;

    @Size(max = 12)
    @Column(name = "issue_status", length = 12)
    private String issueStatus;

    @Size(max = 60)
    @Column(name = "reporter", length = 60)
    private String reporter;

    @Size(max = 60)
    @Column(name = "assigner", length = 60)
    private String assigner;

    @NotNull
    @Lob
    @Column(name = "remark", nullable = false)
    private String remark;

    @OneToMany(mappedBy = "issue")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comments> comments = new HashSet<>();

    @ManyToOne
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Issue createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Issue createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Issue lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Issue lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public Issue version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public Issue issueNo(String issueNo) {
        this.issueNo = issueNo;
        return this;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getIssueSubject() {
        return issueSubject;
    }

    public Issue issueSubject(String issueSubject) {
        this.issueSubject = issueSubject;
        return this;
    }

    public void setIssueSubject(String issueSubject) {
        this.issueSubject = issueSubject;
    }

    public String getIssueType() {
        return issueType;
    }

    public Issue issueType(String issueType) {
        this.issueType = issueType;
        return this;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssuePriority() {
        return issuePriority;
    }

    public Issue issuePriority(String issuePriority) {
        this.issuePriority = issuePriority;
        return this;
    }

    public void setIssuePriority(String issuePriority) {
        this.issuePriority = issuePriority;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public Issue issueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
        return this;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getReporter() {
        return reporter;
    }

    public Issue reporter(String reporter) {
        this.reporter = reporter;
        return this;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssigner() {
        return assigner;
    }

    public Issue assigner(String assigner) {
        this.assigner = assigner;
        return this;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getRemark() {
        return remark;
    }

    public Issue remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<Comments> getComments() {
        return comments;
    }

    public Issue comments(Set<Comments> comments) {
        this.comments = comments;
        return this;
    }

    public Issue addComments(Comments comments) {
        this.comments.add(comments);
        comments.setIssue(this);
        return this;
    }

    public Issue removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setIssue(null);
        return this;
    }

    public void setComments(Set<Comments> comments) {
        this.comments = comments;
    }

    public Project getProject() {
        return project;
    }

    public Issue project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Issue issue = (Issue) o;
        if (issue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Issue{" +
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
