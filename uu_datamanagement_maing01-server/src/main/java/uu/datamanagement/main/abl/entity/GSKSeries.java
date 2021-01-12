package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import uu.datamanagement.main.utils.BusinessType;

public class GSKSeries {

  private String area;
  private BusinessType businessType;
  private List<ManualGSKBlock> manualGSKBlock;
  private List<CountryGSKBlock> countryGSKBlock;
  private List<AutoGSKBlock> autoGSKBlocks;

  public GSKSeries() {
    this.manualGSKBlock = new ArrayList<>();
    this.countryGSKBlock = new ArrayList<>();
    this.autoGSKBlocks = new ArrayList<>();
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

  public List<ManualGSKBlock> getManualGSKBlock() {
    return manualGSKBlock;
  }

  public void setManualGSKBlock(List<ManualGSKBlock> manualGSKBlock) {
    this.manualGSKBlock = manualGSKBlock;
  }

  public List<CountryGSKBlock> getCountryGSKBlock() {
    return countryGSKBlock;
  }

  public void setCountryGSKBlock(List<CountryGSKBlock> countryGSKBlock) {
    this.countryGSKBlock = countryGSKBlock;
  }

  public List<AutoGSKBlock> getAutoGSKBlocks() {
    return autoGSKBlocks;
  }

  public void setAutoGSKBlocks(List<AutoGSKBlock> autoGSKBlocks) {
    this.autoGSKBlocks = autoGSKBlocks;
  }

  public ManualGSKBlock getLastManualBlock() {
    return manualGSKBlock.get(manualGSKBlock.size() - 1);
  }

  public AutoGSKBlock getLastAutoBlock() {
    return autoGSKBlocks.get(autoGSKBlocks.size() - 1);
  }

  public CountryGSKBlock getLastCountryBlock() {
    return countryGSKBlock.get(countryGSKBlock.size() - 1);
  }
}
