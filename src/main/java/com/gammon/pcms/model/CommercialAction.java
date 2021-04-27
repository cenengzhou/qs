package com.gammon.pcms.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.PcmsPersistedAuditObject;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * The persistent class for the COMMERCIAL_ACTION database table.
 *
 */
@Audited
@AuditOverride(forClass = PcmsPersistedAuditObject.class)
@Entity
@Table(name = "COMMERCIAL_ACTION")
@SequenceGenerator(name = "COMMERCIAL_ACTION_GEN", sequenceName = "COMMERCIAL_ACTION_SEQ", allocationSize = 1)
@NamedQuery(name = "CommercialAction.findAll", query = "SELECT v FROM CommercialAction v")
public class CommercialAction extends PcmsPersistedAuditObject implements Serializable {

    private static final long serialVersionUID = 1L;

    public CommercialAction() {
        // CommercialAction constructor
    }

    private Long id;
    private String itemNo;
    private String actionDesc;
    private String actionStatus;
    private Date actionDate;
    private String actionResp;
    private String noJob;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMERCIAL_ACTION_GEN")
    @Column(name = "ID", unique = true, nullable = false, scale = 0)
    public Long getId() {
        return id;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    @Column(name = "ITEM_NO", nullable = false)
    public String getItemNo() {
        return itemNo;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    @Column(name = "ACTION_DESC", nullable = false)
    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    @Column(name = "ACTION_STATUS", nullable = false)
    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    @Column(name = "ACTION_DATE")
    public Date getActionDate() {
        return actionDate;
    }

    public void setActionResp(String actionResp) {
        this.actionResp = actionResp;
    }

    @Column(name = "ACTION_RESP")
    public String getActionResp() {
        return actionResp;
    }

    public void setNoJob(String noJob) {
        this.noJob = noJob;
    }

    @Column(name = "NO_JOB")
    public String getNoJob() {
        return noJob;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
