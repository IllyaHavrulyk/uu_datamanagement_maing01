package uu.datamanagement.main.abl.entity;

import uu.datamanagement.main.utils.TimeInterval;

public class CountryGSKBlock {

  private String gskName;
  private TimeInterval timeInterval;

  public CountryGSKBlock() {
  }

  public CountryGSKBlock(String gskName, TimeInterval timeInterval) {
    this.gskName = gskName;
    this.timeInterval = timeInterval;
  }

  public String getGskName() {
    return gskName;
  }

  public void setGskName(String gskName) {
    this.gskName = gskName;
  }

  public TimeInterval getTimeInterval() {
    return timeInterval;
  }

  public void setTimeInterval(TimeInterval timeInterval) {
    this.timeInterval = timeInterval;
  }

}
