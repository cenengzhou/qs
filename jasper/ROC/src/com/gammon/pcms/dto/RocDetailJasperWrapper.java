package com.gammon.pcms.dto;

public class RocDetailJasperWrapper implements IRocDetailJasperWrapper {

  private double amountBest;
  private double amountBestMovement;
  private double amountRealistic;
  private double amountRealisticMovement;
  private double amountWorst;
  private double amountWorstMovement;
  private String category;
  private String description;
  private String impact;
  private String itemNo;
  private int month;
  private String projectRef;
  private String remark;
  private int rocId;
  private int year;
  
  public RocDetailJasperWrapper() {
  }

  public RocDetailJasperWrapper(String remark) {
    this.remark = remark;
  }
  
  public RocDetailJasperWrapper(
    double amountBest, double amountBestMovement, 
    double amountRealistic, double amountRealisticMovement, 
    double amountWorst, double amountWorstMovement, 
    String category, String description, String impact, String itemNo, 
    String projectRef, String remark) {
    this.amountBest = amountBest;
    this.amountBestMovement = amountBestMovement;
    this.amountRealistic = amountRealistic;
    this.amountRealisticMovement = amountRealisticMovement;
    this.amountWorst = amountWorst;
    this.amountWorstMovement = amountWorstMovement;
    this.category = category;
    this.description = description;
    this.impact = impact;
    this.itemNo = itemNo;
    this.projectRef = projectRef;
    this.remark = remark;
  }

  public RocDetailJasperWrapper(
    double amountBest, double amountBestMovement, 
    double amountRealistic, double amountRealisticMovement, 
    double amountWorst, double amountWorstMovement, 
    String category, String description, String impact, String itemNo, 
      int month, String projectRef, String remark, int rocId, int year) {
    this.amountBest = amountBest;
    this.amountBestMovement = amountBestMovement;
    this.amountRealistic = amountRealistic;
    this.amountRealisticMovement = amountRealisticMovement;
    this.amountWorst = amountWorst;
    this.amountWorstMovement = amountWorstMovement;
    this.category = category;
    this.description = description;
    this.impact = impact;
    this.itemNo = itemNo;
    this.month = month;
    this.projectRef = projectRef;
    this.remark = remark;
    this.rocId = rocId;
    this.year = year;
  }

  public static RocDetailJasperWrapper convert(IRocDetailJasperWrapper wrapper) {
    RocDetailJasperWrapper result = new RocDetailJasperWrapper(
    wrapper.getAmountBest(), wrapper.getAmountBestMovement(),
    wrapper.getAmountRealistic(), wrapper.getAmountRealisticMovement(),
    wrapper.getAmountWorst(), wrapper.getAmountWorstMovement(),
    wrapper.getCategory(), 
    wrapper.getDescription(),
    wrapper.getImpact(),
    wrapper.getItemNo(),
    wrapper.getMonth(),
    wrapper.getProjectRef(),
    wrapper.getRemark(),
    wrapper.getRocId(),
    wrapper.getYear()
    );
    return result;
  }
  
  public double getAmountBest() {
    return amountBest;
  }

  public void setAmountBest(double amountBest) {
    this.amountBest = amountBest;
  }

  public double getAmountBestMovement() {
    return amountBestMovement;
  }

  public void setAmountBestMovement(double amountBestMovement) {
    this.amountBestMovement = amountBestMovement;
  }

  public double getAmountRealistic() {
    return amountRealistic;
  }

  public void setAmountRealistic(double amountRealistic) {
    this.amountRealistic = amountRealistic;
  }

  public double getAmountRealisticMovement() {
    return amountRealisticMovement;
  }

  public void setAmountRealisticMovement(double amountRealisticMovement) {
    this.amountRealisticMovement = amountRealisticMovement;
  }

  public double getAmountWorst() {
    return amountWorst;
  }

  public void setAmountWorst(double amountWorst) {
    this.amountWorst = amountWorst;
  }

  public double getAmountWorstMovement() {
    return amountWorstMovement;
  }

  public void setAmountWorstMovement(double amountWorstMovement) {
    this.amountWorstMovement = amountWorstMovement;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImpact() {
    return impact;
  }

  public void setImpact(String impact) {
    this.impact = impact;
  }

  public String getItemNo() {
    return itemNo;
  }

  public void setItemNo(String itemNo) {
    this.itemNo = itemNo;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public String getProjectRef() {
    return projectRef;
  }

  public void setProjectRef(String projectRef) {
    this.projectRef = projectRef;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getRocId() {
    return rocId;
  }

  public void setRocId(int rocId) {
    this.rocId = rocId;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  @Override
  public String toString() {
    return "RocDetailJasperWrapper [amountBest=" + amountBest + ", amountBestMovement=" + amountBestMovement
        + ", amountRealistic=" + amountRealistic + ", amountRealisticMovement=" + amountRealisticMovement
        + ", amountWorst=" + amountWorst + ", amountWorstMovement=" + amountWorstMovement + ", category=" + category
        + ", description=" + description + ", impact=" + impact + ", itemNo=" + itemNo + ", month=" + month
        + ", projectRef=" + projectRef + ", remark=" + remark + ", rocId=" + rocId + ", year=" + year + "]";
  }

}
