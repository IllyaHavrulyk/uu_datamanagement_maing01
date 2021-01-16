package uu.datamanagement.main.abl.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CountryNode extends AbstractNode{

  public CountryNode(String nodeName) {
    super(nodeName);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("nodeName", getNodeName())
      .toString();
  }
}
