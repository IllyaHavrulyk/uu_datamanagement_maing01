package uu.datamanagement.main.helper.parser;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.stream.events.XMLEvent;
import uu.datamanagement.main.abl.entity.AutoGskBlock;
import uu.datamanagement.main.abl.entity.AutoNode;
import uu.datamanagement.main.abl.entity.CountryGskBlock;
import uu.datamanagement.main.abl.entity.GskDocument;
import uu.datamanagement.main.abl.entity.GskSeries;
import uu.datamanagement.main.abl.entity.ManualGskBlock;
import uu.datamanagement.main.abl.entity.ManualNode;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.utils.BusinessType;
import uu.datamanagement.main.utils.DocumentType;
import uu.datamanagement.main.utils.TimeInterval;

public class GskDocumentParser extends AbstractXmlParser<GskDocument> {

  private GskDocument gskDocument = null;
  private Metadata metadata = null;
  private GskSeries currentSeries = null;
  private final List<GskSeries> seriesList = new ArrayList<>();

  @Override
  protected Map<String, Consumer<XMLEvent>> getEventList() {
    Map<String, Consumer<XMLEvent>> eventList = new HashMap<>();
    eventList.put("GSKDocument/DocumentIdentification", this::createGskDocument);
    eventList.put("GSKDocument/DocumentType", e -> this.metadata.setDocumentType(DocumentType.valueOf(getV(e))));
    eventList.put("GSKDocument/SenderIdentification", e -> this.metadata.setSender(getV(e)));
    eventList.put("GSKDocument/ReceiverIdentification", e -> this.metadata.setReceiver(getV(e)));
    eventList.put("GSKDocument/CreationDateTime", this::parseStringToCreationDateTime);
    eventList.put("GSKDocument/GSKTimeInterval", e -> this.metadata.setTimeInterval(parseStringToTimeInterval(e)));
    eventList.put("GSKDocument/Domain", e -> this.metadata.setDomain(getV(e)));
    eventList.put("GSKDocument/GSKSeries/TimeSeriesIdentification", this::createSeries);
    eventList.put("GSKDocument/GSKSeries/BusinessType", e -> this.currentSeries.setBusinessType(BusinessType.valueOf(getV(e))));
    eventList.put("GSKDocument/GSKSeries/Area", e -> this.currentSeries.setArea(getV(e)));
    eventList.put("GSKDocument/GSKSeries/ManualGSK_Block/GSK_Name", e -> this.currentSeries.getManualGSKBlock().add(new ManualGskBlock(getV(e))));
    eventList.put("GSKDocument/GSKSeries/ManualGSK_Block/TimeInterval", e -> this.currentSeries.getLastManualBlock().setTimeInterval(parseStringToTimeInterval(e)));
    eventList.put("GSKDocument/GSKSeries/ManualGSK_Block/ManualNodes/NodeName", e -> this.currentSeries.getLastManualBlock().getManualNodes().add(new ManualNode(getV(e))));
    eventList.put("GSKDocument/GSKSeries/ManualGSK_Block/ManualNodes/Factor", e -> this.currentSeries.getLastManualBlock().getLastNode().setFactor(new BigDecimal(getV(e))));
    eventList.put("GSKDocument/GSKSeries/AutoGSK_Block/GSK_Name", e -> this.currentSeries.getAutoGSKBlocks().add(new AutoGskBlock(getV(e))));
    eventList.put("GSKDocument/GSKSeries/AutoGSK_Block/TimeInterval", e -> this.currentSeries.getLastAutoBlock().setTimeInterval(parseStringToTimeInterval(e)));
    eventList.put("GSKDocument/GSKSeries/AutoGSK_Block/AutoNodes/NodeName", e -> this.currentSeries.getLastAutoBlock().getAutoNodes().add(new AutoNode(getV(e))));
    eventList.put("GSKDocument/GSKSeries/CountryGSK_Block/GSK_Name", e -> this.currentSeries.getCountryGSKBlock().add(new CountryGskBlock(getV(e))));
    eventList.put("GSKDocument/GSKSeries/CountryGSK_Block/TimeInterval", e -> this.currentSeries.getLastCountryBlock().setTimeInterval(parseStringToTimeInterval(e)));
    return eventList;
  }

  @Override
  protected GskDocument getResult() {
    return gskDocument;
  }

  private void createGskDocument(XMLEvent xmlEvent) {
    String id = getV(xmlEvent);
    gskDocument = new GskDocument();
    metadata = new Metadata();
    gskDocument.setDocumentIdentification(id);
  }

  private TimeInterval parseStringToTimeInterval(XMLEvent xmlEvent) {
    String interval = getV(xmlEvent);
    String from = interval.split("/")[0];
    String to = interval.split("/")[1];

    return new TimeInterval(ZonedDateTime.parse(from), ZonedDateTime.parse(to));
  }

  private void parseStringToCreationDateTime(XMLEvent xmlEvent) {
    String stringDateTime = getV(xmlEvent);
    metadata.setCreationDateTime(ZonedDateTime.parse(stringDateTime));
  }

  private void createSeries(XMLEvent xmlEvent) {
    currentSeries = new GskSeries();
    currentSeries.setTimeSeriesId(Integer.parseInt(getV(xmlEvent)));
    seriesList.add(currentSeries);
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public List<GskSeries> getSeriesList() {
    return seriesList;
  }
}
