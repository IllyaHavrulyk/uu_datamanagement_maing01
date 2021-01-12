package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import uu.datamanagement.main.utils.TimeInterval;

public class CountryGSKBlock {

  private String gskName;
  private TimeInterval timeInterval;
  private List<CountryNode> countryNodes;

  public CountryGSKBlock() {
  }

  public CountryGSKBlock(String gskName) {
    this.gskName = gskName;
    this.countryNodes = new ArrayList<>();
  }

  public List<CountryNode> getCountryNodes() {
    return countryNodes;
  }

  public void setCountryNodes(List<CountryNode> countryNodes) {
    this.countryNodes = countryNodes;
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
