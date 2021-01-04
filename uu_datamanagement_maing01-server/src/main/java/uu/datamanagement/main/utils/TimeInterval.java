package uu.datamanagement.main.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonDeserialize(using = TimeIntervalDeserializer.class)
public class TimeInterval implements Comparable<TimeInterval> {

  private static final DateTimeFormatter formatter = new DateTimeFormatter();

  private ZonedDateTime from;
  private ZonedDateTime to;
  private TimeResolution timeResolution;

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

  /**
   * Checks if this interval and the other interval are consequent, i.e. attribute from of one time interval matche attribute to of the other interval.
   *
   * @param o other interval
   * @return true if the intervals are consequent
   */
  public boolean isConsequent(TimeInterval o) {
    if (isInvalid() || o.isInvalid()) {
      return false;
    }
    TimeInterval thisInterval = withZone(ZoneOffset.UTC);
    TimeInterval otherInterval = o.withZone(ZoneOffset.UTC);
    return thisInterval.from.equals(otherInterval.to) || thisInterval.to.equals(otherInterval.from);
  }

  /**
   * Checks if this interval and the other interval are overlapping.
   *
   * @param o other interval
   * @return true if the intervals are overlapped
   */
  public boolean isOverlapping(TimeInterval o) {
    return this.getIntervalOverlap(o).isPresent();
  }

  /**
   * Merges two consequent intervals into one.
   *
   * @param o other interval
   * @return new interval created by merging this interval with the other interval
   * @throws IllegalArgumentException if the intervals are not consequent
   */
  public TimeInterval merge(TimeInterval o) {
    if (this.isConsequent(o)) {
      if (this.compareTo(o) <= 0) {
        return new TimeInterval(this.from, o.to);
      } else {
        return new TimeInterval(o.from, this.to);
      }
    } else {
      throw new IllegalArgumentException("Passed time intervals are not consequent and cannot be merged.");
    }
  }

  public TimeInterval mergeOverlapping(TimeInterval o) {
    if (this.getIntervalOverlap(o).isPresent()) {
      if (this.compareTo(o) <= 0) {
        return new TimeInterval(this.from, o.to);
      } else {
        return new TimeInterval(o.from, this.to);
      }
    } else {
      throw new IllegalArgumentException("Passed time intervals are not overlapping and cannot be merged.");
    }
  }

  public Optional<TimeInterval> getIntervalOverlap(TimeInterval o) {
    if (this.isSubIntervalOf(o)) {
      return Optional.of(this);
    } else if (o.isSubIntervalOf(this)) {
      return Optional.of(o);
    }
    TimeInterval earlierTimeInterval;
    TimeInterval laterTimeInterval;
    if (this.compareTo(o) <= 0) {
      earlierTimeInterval = this;
      laterTimeInterval = o;
    } else {
      earlierTimeInterval = o;
      laterTimeInterval = this;
    }
    if (earlierTimeInterval.getTo().compareTo(laterTimeInterval.getFrom()) > 0) {
      ZonedDateTime startOverlapInterval = laterTimeInterval.getFrom();
      ZonedDateTime endOverlapInterval = earlierTimeInterval.getTo().compareTo(laterTimeInterval.getTo()) <= 0 ? earlierTimeInterval.getTo() : laterTimeInterval.getTo();
      return Optional.of(new TimeInterval(startOverlapInterval, endOverlapInterval));
    } else {
      return Optional.empty();
    }
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

  /**
   * Creates a new time interval by adding given amount of units to the current time interval. This methods shifts both the beginning and end of the time interval.
   *
   * @param amount the amount of the unit to add to the result, may be negative
   * @param unit the unit of the amount to add
   * @return new shifted time interval
   */
  public TimeInterval plus(long amount, TemporalUnit unit) {
    return new TimeInterval(this.from.plus(amount, unit), this.to.plus(amount, unit));
  }

  /**
   * Creates a new time interval by subtracting given amount of units to the current time interval. This methods shifts both the beginning and end of the time interval.
   *
   * @param amount the amount of the unit to subtract to the result, may be negative
   * @param unit the unit of the amount to subtract
   * @return new shifted time interval
   */
  public TimeInterval minus(long amount, TemporalUnit unit) {
    return new TimeInterval(this.from.minus(amount, unit), this.to.minus(amount, unit));
  }

  public List<TimeInterval> splitByTimeResolution(String timeResolutionString) {
    TimeResolution timeResolution = TimeResolution.valueOf(timeResolutionString);
    return splitByTimeResolution(timeResolution);
  }

  public List<TimeInterval> splitByTimeResolution(TimeResolution timeResolution) {
    ZonedDateTime tempDateTime = this.getFrom();
    List<TimeInterval> intervals = Lists.newArrayList();
    do {
      ZonedDateTime dateTimeTo = tempDateTime.plus(timeResolution.getAmount(), timeResolution.getUnit());
      intervals.add(new TimeInterval(tempDateTime, dateTimeTo));
      tempDateTime = dateTimeTo;
    } while (tempDateTime.isBefore(this.getTo()));
    return intervals;
  }

  /**
   * Returns sub-interval of this interval.
   *
   * @param timeResolution timeResolution of the positions
   * @param startPosition starting position beginning with 1 (equal to the start of this interval)
   * @param endPosition end position
   * @return new sub-interval of this interval
   */
  public TimeInterval getSubInterval(TimeResolution timeResolution, int startPosition, int endPosition) {
    long fromAmountAdd = timeResolution.getAmount() * (startPosition - 1);
    long toAmountAdd = timeResolution.getAmount() * endPosition;
    ZonedDateTime newFrom = this.getFrom().plus(fromAmountAdd, timeResolution.getUnit());
    ZonedDateTime newTo = this.getFrom().plus(toAmountAdd, timeResolution.getUnit());
    return new TimeInterval(newFrom, newTo);
  }

  /**
   * Returns highest possible time resolution of this time interval.
   *
   * <p>Examples:
   * <br>For 30-minutes interval, this is {@link TimeResolution#PT30M}
   * <br>For 45-minutes interval, this is {@link TimeResolution#PT15M}
   * <br>For 24-hours interval, this is {@link TimeResolution#PT60M}
   *
   * @return highest possible time resolution of this time interval
   */
  public TimeResolution timeResolution() {
    if (timeResolution == null) {
      int minutes = (int) Duration.between(this.getFrom(), this.getTo()).toMillis() / (1000 * 60);
      Optional<TimeResolution> tr = TimeResolution.getForMinutes(minutes);
      timeResolution = tr.orElseThrow(() -> new IllegalArgumentException("Cannot get time resolution of this interval (" + this + "). "
        + "Duration in minutes (" + minutes + ") does not match any supported resolution."));
    }
    return timeResolution;
  }

  /**
   * Returns a position of this interval inside another cover interval. This interval must be a sub-interval of the cover interval.
   *
   * @param coverInterval another cover interval
   * @return position of this interval inside the cover interval beginning with 1
   */
  public int getPositionInInterval(TimeInterval coverInterval) {
    if (coverInterval == null || this.isInvalid() || coverInterval.isInvalid()) {
      throw new IllegalArgumentException("Both intervals must be non-null and valid!");
    }
    if (this.getFrom().isBefore(coverInterval.getFrom()) || this.getTo().isAfter(coverInterval.getTo())) {
      throw new IllegalArgumentException(String.format("This interval %s is not a sub-interval of %s.", this, coverInterval));
    }
    long intervalResolution = Duration.between(this.getFrom(), this.getTo()).toMillis();
    long intervalOffset = Duration.between(coverInterval.getFrom(), this.getFrom()).toMillis();
    if (intervalOffset % intervalResolution != 0) {
      throw new IllegalArgumentException(String.format("Position of interval %s inside interval %s is not integer.", this, coverInterval));
    }
    return (int) (intervalOffset / intervalResolution) + 1;
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
