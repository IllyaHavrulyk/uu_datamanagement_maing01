package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AutoGskBlock extends AbstractGskBlock {

  private List<AutoNode> autoNodes;

  public AutoGskBlock(String gskName) {
    super(gskName);
    this.autoNodes = new ArrayList<>();
  }

  public List<AutoNode> getAutoNodes() {
    return autoNodes;
  }

  public void setAutoNodes(List<AutoNode> autoNodes) {
    this.autoNodes = autoNodes;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("gskName", getGskName())
      .append("timeInterval", getTimeInterval())
      .append("autoNodes", autoNodes)
      .toString();
  }

  @Override
  public List<? extends AutoNode> getNodes() {
    return autoNodes;
  }
}
