package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.datamanagement.main.utils.BusinessType;

public class GskSeries {

  private String area;
  private int timeSeriesId;
  private BusinessType businessType;
  private List<ManualGskBlock> manualGSKBlock;
  private List<CountryGskBlock> countryGSKBlock;
  private List<AutoGskBlock> autoGskBlocks;

  public GskSeries() {
    this.manualGSKBlock = new ArrayList<>();
    this.countryGSKBlock = new ArrayList<>();
    this.autoGskBlocks = new ArrayList<>();
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

  public List<ManualGskBlock> getManualGSKBlock() {
    return manualGSKBlock;
  }

  public void setManualGSKBlock(List<ManualGskBlock> manualGSKBlock) {
    this.manualGSKBlock = manualGSKBlock;
  }

  public List<CountryGskBlock> getCountryGSKBlock() {
    return countryGSKBlock;
  }

  public void setCountryGSKBlock(List<CountryGskBlock> countryGSKBlock) {
    this.countryGSKBlock = countryGSKBlock;
  }

  public List<AutoGskBlock> getAutoGSKBlocks() {
    return autoGskBlocks;
  }

  public void setAutoGSKBlocks(List<AutoGskBlock> autoGskBlocks) {
    this.autoGskBlocks = autoGskBlocks;
  }

  public ManualGskBlock getLastManualBlock() {
    return manualGSKBlock.get(manualGSKBlock.size() - 1);
  }

  public AutoGskBlock getLastAutoBlock() {
    return autoGskBlocks.get(autoGskBlocks.size() - 1);
  }

  public CountryGskBlock getLastCountryBlock() {
    return countryGSKBlock.get(countryGSKBlock.size() - 1);
  }

  public int getTimeSeriesId() {
    return timeSeriesId;
  }

  public void setTimeSeriesId(int timeSeriesId) {
    this.timeSeriesId = timeSeriesId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("area", area)
      .append("timeSeriesId", timeSeriesId)
      .append("businessType", businessType)
      .append("manualGSKBlock", manualGSKBlock)
      .append("countryGSKBlock", countryGSKBlock)
      .append("autoGskBlocks", autoGskBlocks)
      .toString();
  }
}
