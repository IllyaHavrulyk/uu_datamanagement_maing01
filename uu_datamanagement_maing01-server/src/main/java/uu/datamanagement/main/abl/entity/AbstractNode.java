package uu.datamanagement.main.abl.entity;

public abstract class AbstractNode {

  private String nodeName;

  public AbstractNode() {
  }

  public AbstractNode(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }
}
