package com.seawen.jiralite.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Code.
 */
@Entity
@Table(name = "code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "code")
public class Code extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Size(max = 60)
//    @Column(name = "created_by", length = 60)
//    private String createdBy;
//
//    @Column(name = "created_date")
//    private Instant createdDate;
//
//    @Size(max = 60)
//    @Column(name = "last_modified_by", length = 60)
//    private String lastModifiedBy;
//
//    @Column(name = "last_modified_date")
//    private Instant lastModifiedDate;
//
//    @Column(name = "version")
//    private Integer version;

    @NotNull
    @Size(max = 30)
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @NotNull
    @Size(max = 60)
    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @NotNull
    @Column(name = "seq_num", nullable = false)
    private Integer seqNum;

    @Lob
    @Column(name = "remark")
    private String remark;

    @ManyToOne
    @JoinColumn(name = "code_type_code", referencedColumnName = "type_code")
    private CodeType codeType;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Code createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Code createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Code lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Code lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public Code version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public Code code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Code name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public Code seqNum(Integer seqNum) {
        this.seqNum = seqNum;
        return this;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getRemark() {
        return remark;
    }

    public Code remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public Code codeType(CodeType codeType) {
        this.codeType = codeType;
        return this;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Code code = (Code) o;
        if (code.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), code.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Code{" +
            "id=" + getId() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", version='" + getVersion() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", seqNum='" + getSeqNum() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
