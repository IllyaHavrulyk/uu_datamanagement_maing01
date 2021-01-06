package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import uu.datamanagement.main.utils.TimeInterval;

public class AutoGSKBlock {

  private String gskName;
  private TimeInterval timeInterval;
  private List<AutoNode> autoNodes;

  public AutoGSKBlock() {
  }

  public AutoGSKBlock(String gskName, TimeInterval timeInterval, List<AutoNode> autoNodes) {
    this.gskName = gskName;
    this.timeInterval = timeInterval;
    this.autoNodes = autoNodes;
  }

  public AutoGSKBlock(String gskName) {
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
}
