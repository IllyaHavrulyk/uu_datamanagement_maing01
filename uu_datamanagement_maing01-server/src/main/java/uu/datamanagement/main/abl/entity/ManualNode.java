package uu.datamanagement.main.abl.entity;

import java.math.BigDecimal;

public class ManualNode {

  private String nodeName;
  private BigDecimal factor;

  public ManualNode(String nodeName, BigDecimal factor) {
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

  public BigDecimal getFactor() {
    return factor;
  }

  public void setFactor(BigDecimal factor) {
    this.factor = factor;
  }
}
