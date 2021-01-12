package uu.datamanagement.main.abl.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AutoNode {

  private String nodeName;

  public AutoNode() {
  }

  public AutoNode(String nodeName) {
    this.nodeName = nodeName;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("nodeName", nodeName)
      .toString();
  }
}
