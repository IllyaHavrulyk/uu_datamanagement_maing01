package uu.datamanagement.main.helper;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.AutoGSKBlock;
import uu.datamanagement.main.abl.entity.AutoNode;
import uu.datamanagement.main.abl.entity.CountryGSKBlock;
import uu.datamanagement.main.abl.entity.GSKSeries;
import uu.datamanagement.main.abl.entity.ManualGSKBlock;
import uu.datamanagement.main.abl.entity.ManualNode;
import uu.datamanagement.main.inspiration.StaxStreamProcessor;
import uu.datamanagement.main.utils.TimeInterval;

@Component
public class GSKBlockHelper {

  public void generateGSKBlocks(GSKSeries gskSeries, InputStream inputStream) {
    gskSeries.setManualGSKBlock(generateManualGSKBlocks(inputStream));
    gskSeries.setAutoGSKBlocks(generateAutoGSKBlocks(inputStream));
    gskSeries.setCountryGSKBlock(generateCountryGSKBlocks(inputStream));
  }

  private List<CountryGSKBlock> generateCountryGSKBlocks(InputStream inputStream) {
    List<CountryGSKBlock> countryGSKBlockList = new ArrayList<>();

    try (StaxStreamProcessor processor = new StaxStreamProcessor(inputStream)) {

      while (processor.startElement("CountryGSK_Block", "GSKSeries")) {
        CountryGSKBlock countryGSKBlock = new CountryGSKBlock();
        if (processor.startElement("GSK_Name", "CountryGSK_Block")) {
          countryGSKBlock.setGskName(processor.getAttribute("v"));
        }
        if (processor.startElement("TimeInterval", "CountryGSK_Block")) {
          TimeInterval timeInterval = getTimeIntervalFromFile(processor.getAttribute("v"));
          countryGSKBlock.setTimeInterval(timeInterval);
        }
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    return countryGSKBlockList;
  }

  private List<ManualGSKBlock> generateManualGSKBlocks(InputStream inputStream) {
    List<ManualGSKBlock> manualGSKBlockList = new ArrayList<>();

    try (StaxStreamProcessor processor = new StaxStreamProcessor(inputStream)) {
      while (processor.startElement("ManualGSK_Block", "GSKSeries")) {
        if (processor.getReader().isStartElement()) {
          String localName = processor.getReader().getLocalName();
        }
        ManualGSKBlock manualGSKBlock = new ManualGSKBlock();
        List<ManualNode> manualNodes = parseManualNodes(processor);
        manualGSKBlock.setManualNodes(manualNodes);
        manualGSKBlockList.add(manualGSKBlock);
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    return manualGSKBlockList;
  }

  private List<AutoGSKBlock> generateAutoGSKBlocks(InputStream inputStream) {
    List<AutoGSKBlock> autoGSKBlockList = new ArrayList<>();

    try (StaxStreamProcessor processor = new StaxStreamProcessor(inputStream)) {

      while (processor.startElement("AutoGSK_Block", "GSKSeries")) {
        AutoGSKBlock manualGSKBlock = new AutoGSKBlock();
        List<AutoNode> autoNodes = parseAutoNodes(processor);
        manualGSKBlock.setAutoNodes(autoNodes);
        autoGSKBlockList.add(manualGSKBlock);
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    return autoGSKBlockList;
  }

  private List<AutoNode> parseAutoNodes(StaxStreamProcessor processor) {
    List<AutoNode> autoNodes = new ArrayList<>();

    try {
      while (processor.startElement("AutoNodes", "AutoGSK_Block")) {
        AutoNode autoNode = new AutoNode();
        if (processor.startElement("NodeName", "AutoNodes")) {
          autoNode.setNodeName(processor.getAttribute("v"));
        }
        autoNodes.add(autoNode);
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }

    return autoNodes;
  }

  private List<ManualNode> parseManualNodes(StaxStreamProcessor processor) throws XMLStreamException {
    List<ManualNode> manualNodes = new ArrayList<>();

    while (processor.startElement("ManualNodes", "ManualGSK_Block")) {
      ManualNode manualNode = new ManualNode();
      if (processor.startElement("NodeName", "ManualNodes")) {
        manualNode.setNodeName(processor.getAttribute("v"));
      }
      if (processor.startElement("Factor", "ManualNodes")) {
        manualNode.setFactor(processor.getAttribute("v"));
      }
      manualNodes.add(manualNode);
    }
    return manualNodes;
  }

  private TimeInterval getTimeIntervalFromFile(String timeInterval) {
    String from = timeInterval.split("/")[0];
    String to = timeInterval.split("/")[1];
    return new TimeInterval(ZonedDateTime.parse(from), ZonedDateTime.parse(to));
  }

}
