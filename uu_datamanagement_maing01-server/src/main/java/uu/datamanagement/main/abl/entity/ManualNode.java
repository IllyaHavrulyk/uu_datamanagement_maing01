package uu.datamanagement.main.abl.entity;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ManualNode extends AbstractNode {

  private BigDecimal factor;

  public ManualNode(String nodeName) {
    super(nodeName);
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
      .append("nodeName", getNodeName())
      .append("factor", factor)
      .toString();
  }
}
