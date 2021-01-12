package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.datamanagement.main.utils.TimeInterval;

public class AutoGskBlock {

  private String gskName;
  private TimeInterval timeInterval;
  private List<AutoNode> autoNodes;

  public AutoGskBlock() {
  }

  public AutoGskBlock(String gskName) {
    this.gskName = gskName;
    this.autoNodes = new ArrayList<>();
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

  public List<AutoNode> getAutoNodes() {
    return autoNodes;
  }

  public void setAutoNodes(List<AutoNode> autoNodes) {
    this.autoNodes = autoNodes;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("gskName", gskName)
      .append("timeInterval", timeInterval)
      .append("autoNodes", autoNodes)
      .toString();
  }
}
