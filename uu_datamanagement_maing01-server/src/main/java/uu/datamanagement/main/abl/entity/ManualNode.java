package uu.datamanagement.main.abl.entity;

public class ManualNode {

  private String nodeName;
  // big decial
  private String factor;

  public ManualNode(String nodeName, String factor) {
    this.nodeName = nodeName;
    this.factor = factor;
  }

  public ManualNode() {
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getFactor() {
    return factor;
  }

  public void setFactor(String factor) {
    this.factor = factor;
  }
}
