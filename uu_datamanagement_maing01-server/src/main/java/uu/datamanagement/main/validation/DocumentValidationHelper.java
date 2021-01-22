package uu.datamanagement.main.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.utils.TimeInterval;
import uu.datamanagement.main.validation.ValidationMessage.ValidationMessageBuilder;
import uu.datamanagement.main.validation.exception.DocumentValidationException.Error;

@Component
public class DocumentValidationHelper {

  public ValidationResult timeSeriesIdIsSequential(GskDocument gskDocument) {
    ValidationResult validationResult = ValidationResult.success();

    List<Integer> sequence = new ArrayList<>();

    gskDocument.getGskSeries().forEach(gskSeries -> sequence.add(gskSeries.getTimeSeriesId()));

    List<Integer> collect = sequence.stream().sorted().collect(Collectors.toList());

    for (int i = 0; i < sequence.size(); i++) {
      if (!sequence.get(i).equals(collect.get(i))) {
        validationResult.addValidationMessage(ValidationMessageBuilder.forError(Error.TIME_SERIES_ID_IS_NOT_SEQUENCE).error());
      }
    }
    return validationResult;
  }

  public ValidationResult oneBlockListPresentInFile(GskDocument gskDocument) {
    ValidationResult validationResult = ValidationResult.success();

    List<String> result = new ArrayList<>();
    gskDocument.getGskSeries().forEach(gskSeries -> gskSeries.getAllBlocks().forEach(abstractGskBlock -> result.add(abstractGskBlock.getClass().getName())));

    result.forEach(s -> {
      if (!result.get(0).equals(s)) {
        validationResult.addValidationMessage(ValidationMessageBuilder.forError(Error.DOCUMENT_HAS_SEVERAL_TYPE_BLOCK).error());
      }
    });
    return validationResult;
  }

  public ValidationResult eachBlockContainNodes(GskDocument gskDocument) {
    ValidationResult validationResult = ValidationResult.success();

    gskDocument.getGskSeries().forEach(
      gskSeries -> gskSeries.getAllBlocks().forEach(
        abstractGskBlock -> {
          if (abstractGskBlock.getNodes().size() <= 3) {
            validationResult.addValidationMessage(ValidationMessageBuilder.forError(Error.LESS_THEN_THREE_NODE).error());
          }
        }
      ));
    return validationResult;
  }

  public ValidationResult allTimeIntervalEqualToGskTimeInterval(GskDocument gskDocument, Metadata metadata) {
    ValidationResult validationResult = ValidationResult.success();

    List<TimeInterval> timeIntervals = new ArrayList<>();
    gskDocument.getGskSeries().forEach(gskSeries -> gskSeries.getAllBlocks().forEach(abstractGskBlock -> timeIntervals.add(abstractGskBlock.getTimeInterval())));

    for (TimeInterval time : timeIntervals) {
      if (!time.isSubIntervalOf(metadata.getTimeInterval())) {
        validationResult.addValidationMessage(ValidationMessageBuilder.forError(Error.TIME_INTERVAL_NO_VALID).error());
      }
    }
    return validationResult;
  }

  public ValidationResult nodeNameAlwaysPresent(GskDocument gskDocument) {
    ValidationResult validationResult = ValidationResult.success();

    gskDocument.getGskSeries().forEach(gskSeries -> gskSeries.getAutoGskBlocks().forEach(autoGskBlock -> autoGskBlock.getAutoNodes().forEach(autoNode -> {
      if (autoNode.getNodeName().isEmpty()) {
        validationResult.addValidationMessage(ValidationMessageBuilder.forError(Error.AUTO_NODE_NAME_EMPTY).error());
      }
    })));
    return validationResult;
  }

  public ValidationResult checkAreaCodingName(GskDocument gskDocument) {
    ValidationResult validationResult = ValidationResult.success();

    gskDocument.getGskSeries().forEach(gskSeries -> validationResult.merge(checkAreaName(gskSeries.getArea())));
    return validationResult;
  }

  private ValidationResult checkAreaName(String area) {
    ValidationResult validationResult = ValidationResult.success();
    if (!area.matches("[A-Z0-9\\-]+")) {
      validationResult.addValidationMessage(ValidationMessageBuilder.forError(Error.AREA_EIC_NOT_VALID).error());
    }
    return validationResult;
  }

}
