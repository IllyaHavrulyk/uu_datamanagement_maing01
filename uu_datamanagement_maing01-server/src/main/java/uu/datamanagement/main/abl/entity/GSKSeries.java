package uu.datamanagement.main.abl.entity;

import uu.datamanagement.main.utils.BusinessType;

public class GSKSeries {

  private String area;
  private BusinessType businessType;
  private ManualGSKBlock manualGSKBlock;

  public GSKSeries() {
  }

  public GSKSeries(String area, BusinessType businessType, ManualGSKBlock manualGSKBlock) {
    this.area = area;
    this.businessType = businessType;
    this.manualGSKBlock = manualGSKBlock;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public BusinessType getBusinessType() {
    return businessType;
  }

  public void setBusinessType(BusinessType businessType) {
    this.businessType = businessType;
  }

  public ManualGSKBlock getManualGSKBlock() {
    return manualGSKBlock;
  }

  public void setManualGSKBlock(ManualGSKBlock manualGSKBlock) {
    this.manualGSKBlock = manualGSKBlock;
  }
}
