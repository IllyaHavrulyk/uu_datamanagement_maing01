package uu.datamanagement.main.abl.entity;

import java.util.Objects;
import org.springframework.util.Assert;
import uu.datamanagement.main.utils.TimeInterval;

public abstract class AbstractGskBlock implements EntityWithTimeInterval, GskNode<AbstractNode> {

  private String gskName;
  private TimeInterval timeInterval;

  AbstractGskBlock() {
  }

  AbstractGskBlock(String gskName) {
    Assert.hasLength(gskName, "Name cannot be empty.");
    this.gskName = gskName;
  }

  public TimeInterval getTimeInterval() {
    return timeInterval;
  }

  public void setTimeInterval(TimeInterval timeInterval) {
    this.timeInterval = timeInterval;
  }

  public String getGskName() {
    return gskName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(gskName, timeInterval);
  }

}
