package uu.datamanagement.main.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.app.exception.AppErrorMap;
import uu.app.exception.AppErrorMap.AppError;

public class ValidationResult {

  private List<ValidationMessage> validationMessages = new ArrayList<>();
  private ZonedDateTime timestamp;
  private ValidationResultSeverity severity = ValidationResultSeverity.OK;

  public ValidationResult() {
    timestamp = ZonedDateTime.now();
  }

  protected ValidationResult(List<ValidationMessage> messages, ValidationResultSeverity severity) {
    this(messages, severity, ZonedDateTime.now());
  }

  @JsonCreator
  public ValidationResult(
    @JsonProperty("validationMessages") List<ValidationMessage> messages,
    @JsonProperty("result") ValidationResultSeverity severity,
    @JsonProperty("timestamp") ZonedDateTime timestamp) {
    this.validationMessages = messages;
    this.severity = severity;
    this.timestamp = timestamp;
  }

  public static ValidationResult fromErrorMap(AppErrorMap uuAppErrorMap) {
    ValidationResult result = new ValidationResult();
    for (Entry<String, AppError> appErrorEntry : uuAppErrorMap.getUuAppErrorMap().entrySet()) {
      AppError appError = appErrorEntry.getValue();
      ValidationMessage validationMessage = ValidationMessage.ValidationMessageBuilder.forCode(appErrorEntry.getKey())
        .detail(appError.getMessage())
        .addParameters((Map<String, Object>) appError.getParamMap())
        .build(ValidationResultSeverity.fromName(appError.getType().name()));
      result.addValidationMessage(validationMessage);
    }
    return result;
  }

  public static ValidationResult success() {
    return new ValidationResult();
  }

  public static ValidationResult error(ValidationMessage... messages) {
    return error(Arrays.asList(messages));
  }

  public static ValidationResult error(Collection<ValidationMessage> messages) {
    return new ValidationResult(new ArrayList<>(messages), ValidationResultSeverity.ERROR);
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ValidationResult merge(ValidationResult validationResult) {
    this.validationMessages.addAll(validationResult.getValidationMessages());
    this.severity = this.severity.getHigherImportance(validationResult.getSeverity());
    return this;
  }

  public boolean isValid() {
    return ValidationResultSeverity.ERROR != this.severity;
  }

  public List<ValidationMessage> getValidationMessages() {
    return Collections.unmodifiableList(validationMessages);
  }

  public void setValidationMessages(List<ValidationMessage> validationMessages) {
    this.validationMessages = validationMessages;
    this.severity = this.validationMessages.stream()
      .map(ValidationMessage::getSeverity)
      .max(Comparator.comparingInt(ValidationResultSeverity::getImportance))
      .orElse(ValidationResultSeverity.OK);
  }

  public void addValidationMessages(List<ValidationMessage> validationMessages) {
    setValidationMessages(ListUtils.union(this.validationMessages, validationMessages));
  }

  public void addValidationMessage(ValidationMessage validationMessage) {
    this.severity = validationMessage.getSeverity().getHigherImportance(this.severity);
    this.validationMessages.add(validationMessage);
  }

  public ValidationResultSeverity getSeverity() {
    return severity;
  }

  public void setSeverity(ValidationResultSeverity severity) {
    this.severity = severity;
  }

  public void setResult(ValidationResultSeverity result) {
    this.severity = result;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this)
      .append("timestamp", timestamp)
      .append("severity", severity);
    if (CollectionUtils.isNotEmpty(validationMessages)) {
      validationMessages.stream()
        .limit(10)
        .forEach(validationMessage -> builder.append("validationMessage", validationMessage));
      builder.append("totalCountOfValidationMessages", validationMessages.size());
    }
    return builder.toString();
  }
}
