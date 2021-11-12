package com.gammon.pcms.dto;

import java.math.BigDecimal;

public class RocAndRocSubdetailWrapper  {

  private Long itemNo;
  private Long subdetailId;
  private String description;
  private BigDecimal amountBest;
  private BigDecimal amountRealistic;
  private BigDecimal amountWorst;
  private int year;
  private int month;
  private String hyperlink;
  private String remarks;
  private String systemStatus;

  public RocAndRocSubdetailWrapper() {
  }

  public RocAndRocSubdetailWrapper(Long itemNo, Long subdetailId, String description, BigDecimal amountBest, BigDecimal amountRealistic, BigDecimal amountWorst, int year, int month, String hyperlink, String remarks, String systemStatus) {
    this.itemNo = itemNo;
    this.subdetailId = subdetailId;
    this.description = description;
    this.amountBest = amountBest;
    this.amountRealistic = amountRealistic;
    this.amountWorst = amountWorst;
    this.year = year;
    this.month = month;
    this.hyperlink = hyperlink;
    this.remarks = remarks;
    this.systemStatus = systemStatus;
  }

  @Override
  public String toString() {
    return "RocAndRocSubdetailWrapper{" +
            "itemNo='" + itemNo + '\'' +
            ", description='" + description + '\'' +
            ", amountBest=" + amountBest +
            ", amountRealistic=" + amountRealistic +
            ", amountWorst=" + amountWorst +
            ", year=" + year +
            ", month=" + month +
            ", hyperlink='" + hyperlink + '\'' +
            ", remarks='" + remarks + '\'' +
            '}';
  }

  public Long getItemNo() {
    return itemNo;
  }

  public void setItemNo(Long itemNo) {
    this.itemNo = itemNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getHyperlink() {
    return hyperlink;
  }

  public void setHyperlink(String hyperlink) {
    this.hyperlink = hyperlink;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Long getSubdetailId() {
    return subdetailId;
  }

  public void setSubdetailId(Long subdetailId) {
    this.subdetailId = subdetailId;
  }

  public String getSystemStatus() {
    return systemStatus;
  }

  public void setSystemStatus(String systemStatus) {
    this.systemStatus = systemStatus;
  }
}
