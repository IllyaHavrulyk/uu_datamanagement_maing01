package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.datamanagement.main.utils.TimeInterval;

public class ManualGskBlock {

  private String gskName;
  private TimeInterval timeInterval;
  private List<ManualNode> manualNodes;

  public ManualGskBlock() {
  }

  public ManualGskBlock(String gskName) {
    this.gskName = gskName;
    manualNodes = new ArrayList<>();
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

  public List<ManualNode> getManualNodes() {
    return manualNodes;
  }

  public void setManualNodes(List<ManualNode> manualNodes) {
    this.manualNodes = manualNodes;
  }

  public ManualNode getLastNode() {
    return manualNodes.get(manualNodes.size() - 1);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("gskName", gskName)
      .append("timeInterval", timeInterval)
      .append("manualNodes", manualNodes)
      .toString();
  }
}
