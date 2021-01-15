package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.datamanagement.main.utils.BusinessType;

public class GskSeries implements GskBlock<AbstractGskBlock> {

  private String area;
  private int timeSeriesId;
  private BusinessType businessType;

  private List<ManualGskBlock> manualGskBlocks = new ArrayList<>();
  private List<AutoGskBlock> autoGskBlocks = new ArrayList<>();
  private List<CountryGskBlock> countryGskBlocks = new ArrayList<>();

  public GskSeries() {
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

  public List<ManualGskBlock> getManualGskBlocks() {
    return manualGskBlocks;
  }

  public void setManualGskBlocks(List<ManualGskBlock> manualGskBlocks) {
    this.manualGskBlocks = manualGskBlocks;
  }

  public List<CountryGskBlock> getCountryGskBlocks() {
    return countryGskBlocks;
  }

  public void setCountryGskBlocks(List<CountryGskBlock> countryGskBlocks) {
    this.countryGskBlocks = countryGskBlocks;
  }

  public List<AutoGskBlock> getAutoGskBlocks() {
    return autoGskBlocks;
  }

  public void setAutoGskBlocks(List<AutoGskBlock> autoGskBlocks) {
    this.autoGskBlocks = autoGskBlocks;
  }

  public ManualGskBlock getLastManualBlock() {
    return manualGskBlocks.get(manualGskBlocks.size() - 1);
  }

  public AutoGskBlock getLastAutoBlock() {
    return autoGskBlocks.get(autoGskBlocks.size() - 1);
  }

  public CountryGskBlock getLastCountryBlock() {
    return countryGskBlocks.get(countryGskBlocks.size() - 1);
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
      .append("manualGskBlock", manualGskBlocks)
      .append("countryGskBlock", countryGskBlocks)
      .append("autoGskBlocks", autoGskBlocks)
      .toString();
  }

  @Override
  public List<AbstractGskBlock> getAllBlocks() {
    List<AbstractGskBlock> blocks = new ArrayList<>();
    blocks.addAll(getAutoGskBlocks());
    blocks.addAll(getManualGskBlocks());
    blocks.addAll(getCountryGskBlocks());
    return blocks;
  }
}
