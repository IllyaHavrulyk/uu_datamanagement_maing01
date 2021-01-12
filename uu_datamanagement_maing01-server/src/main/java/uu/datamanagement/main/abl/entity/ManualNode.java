package uu.datamanagement.main.abl.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ManualNode {

  private String nodeName;
  private BigDecimal factor;

  public ManualNode(String nodeName) {
    this.nodeName = nodeName;
  }

  public ManualNode() {
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public BigDecimal getFactor() {
    return factor;
  }

  public void setFactor(BigDecimal factor) {
    this.factor = factor;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("nodeName", nodeName)
      .append("factor", factor)
      .toString();
  }
}
