package uu.datamanagement.main.utils;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public enum TimeResolution {

  PT15M(15, ChronoUnit.MINUTES),
  PT30M(30, ChronoUnit.MINUTES),
  PT60M(60, ChronoUnit.MINUTES);

  private final long amount;
  private final TemporalUnit unit;

  TimeResolution(long amount, TemporalUnit unit) {
    this.amount = amount;
    this.unit = unit;
  }

  public static Optional<TimeResolution> getForMinutes(int minutes) {
    return Arrays.stream(TimeResolution.values())
      .filter(tr -> minutes % tr.getAmount() == 0 && tr.getUnit() == ChronoUnit.MINUTES)
      .sorted(Comparator.comparing(TimeResolution::getAmount).reversed())
      .findFirst();
  }

  public long getAmount() {
    return amount;
  }

  public TemporalUnit getUnit() {
    return unit;
  }

}

