package uu.datamanagement.main.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ValidationResultSeverity {
  OK(1),
  WARNING(2),
  ERROR(3);

  private final int importance;

  ValidationResultSeverity(int importance) {
    this.importance = importance;
  }

  public ValidationResultSeverity getHigherImportance(ValidationResultSeverity o) {
    return this.importance >= o.importance ? this : o;
  }

  public int getImportance() {
    return importance;
  }

  @JsonCreator
  public static ValidationResultSeverity fromName(String name) {
    return ValidationResultSeverity.valueOf(name);
  }

  @JsonValue
  public String toName() {
    return this.name();
  }

}
