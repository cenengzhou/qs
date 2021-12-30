package com.gammon.pcms.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.BasePersistedObject;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Audited
@AuditOverride(forClass = BasePersistedAuditObject.class)
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "APPROVAL_SUMMARY", indexes = {
        @Index(name = "APPROVAL_SUMMARY_UN", columnList = "ID_TABLE, NAME_TABLE, NO_JOB", unique = true)
})
@OptimisticLocking(type = OptimisticLockType.NONE)
@SequenceGenerator(name = "APPROVAL_SUMMARY_GEN", sequenceName = "APPROVAL_SUMMARY_SEQ", allocationSize = 1)
@AttributeOverride(name = "id", column = @Column(name = "ID", unique = true, nullable = false, insertable = false, updatable = false, precision = 19, scale = 0))
@NamedQuery(name = "APPROVAL_SUMMARY.findAll", query = "SELECT v FROM ApprovalSummary v")
public class ApprovalSummary extends BasePersistedObject {

    public static final String PaymentCertNameObject = "PAYMENT_CERT";

    private Long id;
    private Long idTable;
    private String nameTable;
    private String noJob;
    private String summary;
    private String eoj;
    private String contingencies;
    private String riskOpps;
    private String others;

    public ApprovalSummary() {
    }

    public ApprovalSummary(Long id, Long idTable, String nameTable, String noJob, String summary, String eoj, String contingencies, String riskOpps, String others) {
        this.id = id;
        this.idTable = idTable;
        this.nameTable = nameTable;
        this.noJob = noJob;
        this.summary = summary;
        this.eoj = eoj;
        this.contingencies = contingencies;
        this.riskOpps = riskOpps;
        this.others = others;
    }

    public ApprovalSummary(Long idTable, String nameTable, String noJob, String summary, String eoj, String contingencies, String riskOpps, String others) {
        this.idTable = idTable;
        this.nameTable = nameTable;
        this.noJob = noJob;
        this.summary = summary;
        this.eoj = eoj;
        this.contingencies = contingencies;
        this.riskOpps = riskOpps;
        this.others = others;
    }

    @Column(name = "\"OTHERS\"", length = 300)
    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Column(name = "RISK_OPPS", length = 300)
    public String getRiskOpps() {
        return riskOpps;
    }

    public void setRiskOpps(String riskOpps) {
        this.riskOpps = riskOpps;
    }

    @Column(name = "CONTINGENCIES", length = 300)
    public String getContingencies() {
        return contingencies;
    }

    public void setContingencies(String contingencies) {
        this.contingencies = contingencies;
    }

    @Column(name = "EOJ", length = 300)
    public String getEoj() {
        return eoj;
    }

    public void setEoj(String eoj) {
        this.eoj = eoj;
    }

    @Column(name = "SUMMARY", length = 500)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Column(name = "NO_JOB", nullable = false, length = 5)
    public String getNoJob() {
        return noJob;
    }

    public void setNoJob(String noJob) {
        this.noJob = noJob;
    }

    @Column(name = "NAME_TABLE", nullable = false, length = 30)
    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    @Column(name = "ID_TABLE", nullable = false)
    public Long getIdTable() {
        return idTable;
    }

    public void setIdTable(Long idTable) {
        this.idTable = idTable;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPROVAL_SUMMARY_GEN")
    @Column(name = "ID", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}