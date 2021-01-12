package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.datamanagement.main.utils.TimeInterval;

public class CountryGskBlock {

  private String gskName;
  private TimeInterval timeInterval;
  private List<CountryNode> countryNodes;

  public CountryGskBlock() {
  }

  public CountryGskBlock(String gskName) {
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

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("gskName", gskName)
      .append("timeInterval", timeInterval)
      .append("countryNodes", countryNodes)
      .toString();
  }
}
