package uu.datamanagement.main.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@JsonDeserialize(using = TimeIntervalDeserializer.class)
public class TimeInterval implements Comparable<TimeInterval> {

  private static final DateTimeFormatter formatter = new DateTimeFormatter();

  private ZonedDateTime from;
  private ZonedDateTime to;

  public TimeInterval() {
  }

  public TimeInterval(ZonedDateTime from, ZonedDateTime to) {
    this.from = from;
    this.to = to;
  }

  public ZonedDateTime getFrom() {
    return from;
  }

  public void setFrom(ZonedDateTime from) {
    this.from = from;
  }

  public ZonedDateTime getTo() {
    return to;
  }

  public void setTo(ZonedDateTime to) {
    this.to = to;
  }

  public boolean isSubIntervalOf(TimeInterval timeInterval) {
    if (isInvalid() || timeInterval.isInvalid()) {
      return false;
    }
    TimeInterval thisInterval = withZone(ZoneOffset.UTC);
    TimeInterval otherInterval = timeInterval.withZone(ZoneOffset.UTC);
    return thisInterval.getFrom().compareTo(otherInterval.getFrom()) >= 0
      && thisInterval.getTo().compareTo(otherInterval.getTo()) <= 0;
  }

  public TimeInterval withZone(ZoneId zone) {
    return new TimeInterval(from.withZoneSameInstant(zone), to.withZoneSameInstant(zone));
  }

  private boolean isInvalid() {
    return from == null || to == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TimeInterval that = (TimeInterval) o;

    if (allValuesPresent(this, that)) {
      return from.isEqual(that.from) &&
        to.isEqual(that.to);
    }

    if (allValuesNull(this, that)) {
      return true;
    }

    if (onlyFromValuesNull(this, that)) {
      return to.isEqual(that.to);
    }

    if (onlyToValuesNull(this, that)) {
      return from.isEqual(that.from);
    }

    return false;
  }

  private boolean onlyFromValuesNull(TimeInterval thisTimeInterval, TimeInterval thatTimeInterval) {
    return (thisTimeInterval.from == null && thisTimeInterval.to != null) &&
      (thatTimeInterval.from == null && thatTimeInterval.to != null);
  }

  private boolean onlyToValuesNull(TimeInterval thisTimeInterval, TimeInterval thatTimeInterval) {
    return (thisTimeInterval.to == null && thisTimeInterval.from != null) &&
      (thatTimeInterval.to == null && thatTimeInterval.from != null);
  }

  private boolean allValuesPresent(TimeInterval thisTimeInterval, TimeInterval thatTimeInterval) {
    return thisTimeInterval.from != null &&
      thatTimeInterval.from != null &&
      thisTimeInterval.to != null &&
      thatTimeInterval.to != null;
  }

  private boolean allValuesNull(TimeInterval thisTimeInterval, TimeInterval thatTimeInterval) {
    return thisTimeInterval.from == null &&
      thatTimeInterval.from == null &&
      thisTimeInterval.to == null &&
      thatTimeInterval.to == null;
  }

  @Override
  public int hashCode() {
    if (from == null && to == null) {
      return 0;
    }
    if (from == null) {
      return to.toInstant().hashCode();
    }
    if (to == null) {
      return from.toInstant().hashCode();
    }
    return Objects.hash(from.toInstant(), to.toInstant());
  }

  @Override
  public String toString() {
    return (from == null ? "null" : formatter.format(from)) + "/" + (to == null ? "null" : formatter.format(to));
  }

  @Override
  public int compareTo(TimeInterval timeInterval) {
    int compare = Long.compare(this.getFrom().toEpochSecond(), timeInterval.getFrom().toEpochSecond());
    if (compare != 0) {
      return compare;
    }
    return Long.compare(this.getTo().toEpochSecond(), timeInterval.getTo().toEpochSecond());
  }
}
