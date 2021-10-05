package com.gammon.pcms.dto;

public class RocDetailJasperWrapper implements IRocDetailJasperWrapper {

  private double amountBest;
  private double amountRealistic;
  private double amountWorst;
  private String category;
  private String description;
  private String projectRef;
  private String remark;
  private String rocId;
  
  public RocDetailJasperWrapper() {
  }

  public RocDetailJasperWrapper(double amountBest, double amountRealistic, double amountWorst, String category, String description, String projectRef, String remark, String rocId) {
    this.amountBest = amountBest;
    this.amountRealistic = amountRealistic;
    this.amountWorst = amountWorst;
    this.category = category;
    this.description = description;
    this.projectRef = projectRef;
    this.remark = remark;
    this.rocId = rocId;
  }


  public double getAmountBest() {
    return this.amountBest;
  }

  public void setAmountBest(double amountBest) {
    this.amountBest = amountBest;
  }

  public double getAmountRealistic() {
    return this.amountRealistic;
  }

  public void setAmountRealistic(double amountRealistic) {
    this.amountRealistic = amountRealistic;
  }

  public double getAmountWorst() {
    return this.amountWorst;
  }

  public void setAmountWorst(double amountWorst) {
    this.amountWorst = amountWorst;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getProjectRef() {
    return this.projectRef;
  }

  public void setProjectRef(String projectRef) {
    this.projectRef = projectRef;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRocId() {
    return this.rocId;
  }

  public void setRocId(String rocId) {
    this.rocId = rocId;
  }

  @Override
  public String toString() {
    return "{" +
      " amountBest='" + getAmountBest() + "'" +
      ", amountRealistic='" + getAmountRealistic() + "'" +
      ", amountWorst='" + getAmountWorst() + "'" +
      ", category='" + getCategory() + "'" +
      ", description='" + getDescription() + "'" +
      ", projectRef='" + getProjectRef() + "'" +
      ", remark='" + getRemark() + "'" +
      ", rocId='" + getRocId() + "'" +
      "}";
  }

}
