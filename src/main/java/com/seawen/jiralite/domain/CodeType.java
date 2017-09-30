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
 * A CodeType.
 */
@Entity
@Table(name = "code_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "codetype")
public class CodeType extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "type_code", length = 30, nullable = false)
    private String typeCode;

//    @NotNull
    @Size(max = 30)
    @Column(name = "parent_type_code", length = 30)
    private String parentTypeCode;

    @NotNull
    @Size(max = 60)
    @Column(name = "type_name", length = 60, nullable = false)
    private String typeName;

    @Lob
    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "codeType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Code> codes = new HashSet<>();

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

    public CodeType createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public CodeType createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public CodeType lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public CodeType lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public CodeType version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public CodeType typeCode(String typeCode) {
        this.typeCode = typeCode;
        return this;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getParentTypeCode() {
        return parentTypeCode;
    }

    public CodeType parentTypeCode(String parentTypeCode) {
        this.parentTypeCode = parentTypeCode;
        return this;
    }

    public void setParentTypeCode(String parentTypeCode) {
        this.parentTypeCode = parentTypeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public CodeType typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRemark() {
        return remark;
    }

    public CodeType remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<Code> getCodes() {
        return codes;
    }

    public CodeType codes(Set<Code> codes) {
        this.codes = codes;
        return this;
    }

    public CodeType addCode(Code code) {
        this.codes.add(code);
        code.setCodeType(this);
        return this;
    }

    public CodeType removeCode(Code code) {
        this.codes.remove(code);
        code.setCodeType(null);
        return this;
    }

    public void setCodes(Set<Code> codes) {
        this.codes = codes;
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
        CodeType codeType = (CodeType) o;
        if (codeType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), codeType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CodeType{" +
            "id=" + getId() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", version='" + getVersion() + "'" +
            ", typeCode='" + getTypeCode() + "'" +
            ", parentTypeCode='" + getParentTypeCode() + "'" +
            ", typeName='" + getTypeName() + "'" +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
