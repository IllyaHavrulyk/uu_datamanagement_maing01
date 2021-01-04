package uu.datamanagement.main.abl.entity;

import java.util.List;
import uu.datamanagement.main.utils.TimeInterval;

public class ManualGSKBlock {

  private String gskName;
  private TimeInterval timeInterval;
  private List<ManualNode> manualNodes;

  public ManualGSKBlock() {
  }

  public ManualGSKBlock(String gskName, TimeInterval timeInterval, List<ManualNode> manualNodes) {
    this.gskName = gskName;
    this.timeInterval = timeInterval;
    this.manualNodes = manualNodes;
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
}
