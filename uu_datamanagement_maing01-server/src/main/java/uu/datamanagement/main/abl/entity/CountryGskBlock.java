package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CountryGskBlock extends AbstractGskBlock {

  private List<CountryNode> countryNodes;

  public CountryGskBlock(String gskName) {
    super(gskName);
    this.countryNodes = new ArrayList<>();
  }

  public List<CountryNode> getCountryNodes() {
    return countryNodes;
  }

  public void setCountryNodes(List<CountryNode> countryNodes) {
    this.countryNodes = countryNodes;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("gskName", getGskName())
      .append("timeInterval", getTimeInterval())
      .append("countryNodes", countryNodes)
      .toString();
  }

  @Override
  public List<? extends CountryNode> getNodes() {
    return countryNodes;
  }
}
