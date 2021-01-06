package uu.datamanagement.main.helper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uu.datamanagement.main.abl.entity.AutoGSKBlock;
import uu.datamanagement.main.abl.entity.AutoNode;
import uu.datamanagement.main.abl.entity.GSKSeries;
import uu.datamanagement.main.abl.entity.ManualGSKBlock;
import uu.datamanagement.main.abl.entity.ManualNode;
import uu.datamanagement.main.utils.BusinessType;
import uu.datamanagement.main.utils.TimeInterval;

@Component
public class GSKSeriesParserHelper {

  private final ModelMapper modelMapper;

  public final List<String> ALLOWED_PARAMETERS = Arrays.asList("Area", "BusinessType", "ManualGSK_Block", "AutoGSK_Block", "CountryGSK_Block");

  @Inject
  public GSKSeriesParserHelper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public void fillingGSKSeries(XMLStreamReader xmlStreamReader, GSKSeries gskSeries, List<GSKSeries> gskSeriesList) {

    switch (ALLOWED_PARAMETERS.indexOf(xmlStreamReader.getLocalName())) {
      case 0:
        gskSeries.setArea(xmlStreamReader.getAttributeValue(1));
        break;
      case 1:
        gskSeries.setBusinessType(BusinessType.valueOf(xmlStreamReader.getAttributeValue(1)));
        break;
      case 2:
        listManualGSKBlock(xmlStreamReader, gskSeries, gskSeriesList);
        break;
      case 3:
        listAutoGSKBlock(xmlStreamReader, gskSeries, gskSeriesList);
        break;
      default:
        break;
    }
  }

  private void listAutoGSKBlock(XMLStreamReader xmlStreamReader, GSKSeries gskSeries, List<GSKSeries> gskSeriesList) {
    AutoGSKBlock autoGSKBlock = new AutoGSKBlock();
    List<AutoNode> manualNodes = new ArrayList<>();
    try {
      while (xmlStreamReader.hasNext()) {
        xmlStreamReader.next();

        if (xmlStreamReader.isStartElement()) {
          if (xmlStreamReader.getLocalName().equals("GSK_Name")) {
            autoGSKBlock.setGskName(xmlStreamReader.getAttributeValue(0));
          }
          if (xmlStreamReader.getLocalName().equals("TimeInterval")) {
            autoGSKBlock.setTimeInterval(getTimeIntervalFromFile(xmlStreamReader.getAttributeValue(0)));
          }
          if (xmlStreamReader.getLocalName().equals("AutoNodes")) {
            AutoNode autoNode = generateAutoNode(xmlStreamReader);
            manualNodes.add(autoNode);
          }
        }
        if (xmlStreamReader.isEndElement()) {
          if (xmlStreamReader.getLocalName().equals("AutoGSK_Block")) {
            break;
          }
        }
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }

    autoGSKBlock.setAutoNodes(manualNodes);
    List<AutoGSKBlock> gskBlocks = gskSeries.getAutoGSKBlocks() == null ? new ArrayList<>() : gskSeries.getAutoGSKBlocks();
    gskBlocks.add(autoGSKBlock);
    gskSeries.setAutoGSKBlocks(gskBlocks);
    gskSeriesList.add(gskSeries);
  }

  private AutoNode generateAutoNode(XMLStreamReader xmlStreamReader) {
    AutoNode autoNode = new AutoNode();
    try {
      while (xmlStreamReader.hasNext()) {
        xmlStreamReader.next();
        if (xmlStreamReader.isStartElement()) {
          if (xmlStreamReader.getLocalName().equals("NodeName")) {
            autoNode.setNodeName(xmlStreamReader.getAttributeValue(null, "v"));
          }
        }
        if (xmlStreamReader.isEndElement()) {
          if (xmlStreamReader.getLocalName().equals("AutoNodes")) {
            break;
          }
        }
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    return autoNode;

  }

  private void listManualGSKBlock(XMLStreamReader xmlStreamReader, GSKSeries gskSeries, List<GSKSeries> gskSeriesList) {
    ManualGSKBlock manualGSKBlock = new ManualGSKBlock();
    List<ManualNode> manualNodes = new ArrayList<>();
    try {
      while (xmlStreamReader.hasNext()) {
        xmlStreamReader.next();

        if (xmlStreamReader.isStartElement()) {
          if (xmlStreamReader.getLocalName().equals("GSK_Name")) {
            manualGSKBlock.setGskName(xmlStreamReader.getAttributeValue(0));
          }
          if (xmlStreamReader.getLocalName().equals("TimeInterval")) {
            manualGSKBlock.setTimeInterval(getTimeIntervalFromFile(xmlStreamReader.getAttributeValue(0)));
          }
          if (xmlStreamReader.getLocalName().equals("ManualNodes")) {
            ManualNode manualNode = generateManualNode(xmlStreamReader);
            manualNodes.add(manualNode);
          }
        }
        if (xmlStreamReader.isEndElement()) {
          if (xmlStreamReader.getLocalName().equals("ManualGSK_Block")) {
            break;
          }
        }
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }

    manualGSKBlock.setManualNodes(manualNodes);
    List<ManualGSKBlock> gskBlocks = gskSeries.getManualGSKBlock() == null ? new ArrayList<>() : gskSeries.getManualGSKBlock();
    gskBlocks.add(manualGSKBlock);
    gskSeries.setManualGSKBlock(gskBlocks);
    gskSeriesList.add(gskSeries);
  }

  private ManualNode generateManualNode(XMLStreamReader xmlStreamReader) {
    ManualNode manualNode = new ManualNode();
    try {
      while (xmlStreamReader.hasNext()) {
        xmlStreamReader.next();
        if (xmlStreamReader.isStartElement()) {
          if (xmlStreamReader.getLocalName().equals("NodeName")) {
            manualNode.setNodeName(xmlStreamReader.getAttributeValue(null, "v"));
          }
          if (xmlStreamReader.getLocalName().equals("Factor")) {
            manualNode.setFactor(xmlStreamReader.getAttributeValue(null, "v"));
          }
        }
        if (xmlStreamReader.isEndElement()) {
          if (xmlStreamReader.getLocalName().equals("ManualNodes")) {
            break;
          }
        }
      }
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    return manualNode;
  }

  private TimeInterval getTimeIntervalFromFile(String timeInterval) {
    String from = timeInterval.split("/")[0];
    String to = timeInterval.split("/")[1];
    return new TimeInterval(ZonedDateTime.parse(from), ZonedDateTime.parse(to));
  }

}
