package com.gammon.pcms.dto;

import java.math.BigDecimal;

public class RocAndRocDetailWrapper {

  private Long itemNo;
  private Long detailId;
  private int year;
  private int month;
  private BigDecimal amountBest;
  private BigDecimal amountRealistic;
  private BigDecimal amountWorst;
  private String remarks;
  private String status;
  private String systemStatus;

  public RocAndRocDetailWrapper() {
  }

  public RocAndRocDetailWrapper(Long itemNo, Long detailId, int year, int month, BigDecimal amountBest, BigDecimal amountRealistic, BigDecimal amountWorst, String remarks, String status, String systemStatus) {
    this.itemNo = itemNo;
    this.detailId = detailId;
    this.year = year;
    this.month = month;
    this.amountBest = amountBest;
    this.amountRealistic = amountRealistic;
    this.amountWorst = amountWorst;
    this.remarks = remarks;
    this.status = status;
    this.systemStatus = systemStatus;
  }

  public Long getItemNo() {
    return itemNo;
  }

  public void setItemNo(Long itemNo) {
    this.itemNo = itemNo;
  }

  public Long getDetailId() {
    return detailId;
  }

  public void setDetailId(Long detailId) {
    this.detailId = detailId;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public BigDecimal getAmountBest() {
    return amountBest;
  }

  public void setAmountBest(BigDecimal amountBest) {
    this.amountBest = amountBest;
  }

  public BigDecimal getAmountRealistic() {
    return amountRealistic;
  }

  public void setAmountRealistic(BigDecimal amountRealistic) {
    this.amountRealistic = amountRealistic;
  }

  public BigDecimal getAmountWorst() {
    return amountWorst;
  }

  public void setAmountWorst(BigDecimal amountWorst) {
    this.amountWorst = amountWorst;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSystemStatus() {
    return systemStatus;
  }

  public void setSystemStatus(String systemStatus) {
    this.systemStatus = systemStatus;
  }

  @Override
  public String toString() {
    return "RocAndRocDetailWrapper{" +
            "itemNo=" + itemNo +
            ", detailId=" + detailId +
            ", year=" + year +
            ", month=" + month +
            ", amountBest=" + amountBest +
            ", amountRealistic=" + amountRealistic +
            ", amountWorst=" + amountWorst +
            ", remarks='" + remarks + '\'' +
            ", status='" + status + '\'' +
            ", systemStatus='" + systemStatus + '\'' +
            '}';
  }
}
