package uu.datamanagement.main.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uu.datamanagement.main.helper.exception.ErrorDefinition;

public class ValidationMessage {

  private ValidationResultSeverity severity = ValidationResultSeverity.OK;
  private String code;
  private String detail;
  private Map<String, Object> parameters = new HashMap<>();

  public ValidationMessage() {
  }

  @JsonCreator
  public ValidationMessage(
    @JsonProperty("result") ValidationResultSeverity severity,
    @JsonProperty("code") String code,
    @JsonProperty("detail") String detail,
    @JsonProperty("parameters") Map<String, Object> parameters) {
    this.severity = severity;
    this.code = code;
    this.detail = detail;
    this.parameters = parameters;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  public void addParameter(String param, Object value) {
    parameters.put(param, value);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("severity", severity)
      .append("code", code)
      .append("detail", detail)
      .append("parameters", parameters)
      .toString();
  }

  public static class ValidationMessageBuilder {

    private String code;
    private String detail;
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * @deprecated use factory methods forError or forCode as code, which force that code is always set
     */
    @Deprecated
    public ValidationMessageBuilder() {
    }

    private ValidationMessageBuilder(String code, String detail) {
      assert code != null;
      this.code = code;
      this.detail = detail;
    }

    public static ValidationMessageBuilder forError(ErrorDefinition error) {
      return new ValidationMessageBuilder(error.getErrorCode().getErrorCode(), error.getMessage());
    }


    public static ValidationMessageBuilder forCode(String code) {
      return new ValidationMessageBuilder(code, null);
    }

    /**
     * @deprecated use factory methods forError or forCode as code, which force that code is always set
     */
    @Deprecated
    public ValidationMessageBuilder code(String code) {
      this.code = code;
      return this;
    }

    public ValidationMessageBuilder detail(String detail) {
      this.detail = detail;
      return this;
    }

    public ValidationMessageBuilder addParameter(String key, Object value) {
      parameters.put(key, value);
      return this;
    }

    public ValidationMessageBuilder addParameters(Map<String, Object> parameters) {
      if (parameters != null && !parameters.isEmpty()) {
        this.parameters.putAll(parameters);
      }
      return this;
    }

    /**
     * @deprecated use factory methods forError or forCode as code, which force that code is always set
     */
    @Deprecated
    public ValidationMessageBuilder errorDefinition(ErrorDefinition errorDefinition) {
      this.code = errorDefinition.getErrorCode().getErrorCode();
      this.detail = errorDefinition.getMessage();
      return this;
    }

    public ValidationMessage build(ValidationResultSeverity severity) {
      return new ValidationMessage(severity, code, detail, parameters);
    }

    public ValidationMessage ok() {
      return new ValidationMessage(ValidationResultSeverity.OK, code, detail, parameters);
    }

    public ValidationMessage warning() {
      return new ValidationMessage(ValidationResultSeverity.WARNING, code, detail, parameters);
    }

    public ValidationMessage error() {
      return new ValidationMessage(ValidationResultSeverity.ERROR, code, detail, parameters);
    }
  }
}
