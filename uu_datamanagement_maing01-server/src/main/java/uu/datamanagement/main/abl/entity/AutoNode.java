package uu.datamanagement.main.abl.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AutoNode extends AbstractNode {

  public AutoNode(String nodeName) {
    super(nodeName);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("nodeName", getNodeName())
      .toString();
  }
}
