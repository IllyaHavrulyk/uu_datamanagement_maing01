package uu.datamanagement.main.abl.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ManualGskBlock extends AbstractGskBlock {

  private List<ManualNode> manualNodes;

  public ManualGskBlock(String gskName) {
    super(gskName);
    manualNodes = new ArrayList<>();
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
      .append("gskName", getGskName())
      .append("timeInterval", getTimeInterval())
      .append("manualNodes", manualNodes)
      .toString();
  }
}
