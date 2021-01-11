package uu.datamanagement.main.helper.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.Test;
import uu.datamanagement.main.abl.entity.AutoGSKBlock;
import uu.datamanagement.main.abl.entity.CountryGSKBlock;
import uu.datamanagement.main.abl.entity.GSKDocument;
import uu.datamanagement.main.abl.entity.GSKSeries;
import uu.datamanagement.main.abl.entity.ManualGSKBlock;
import uu.datamanagement.main.abl.entity.Metadata;
import uu.datamanagement.main.api.exceptions.DeserializationException;
import uu.datamanagement.main.utils.BusinessType;
import uu.datamanagement.main.utils.DocumentType;
import uu.datamanagement.main.utils.TimeInterval;

public class GskDocumentParserTest {

  private final TimeInterval timeInterval = new TimeInterval(ZonedDateTime.parse("2019-02-19T23:00Z"), ZonedDateTime.parse("2019-02-20T23:00Z"));
  private final GskDocumentParser parser = new GskDocumentParser();

  @Test
  public void testHds() throws IOException {
    GSKDocument data;
    Metadata metadata;

    try (InputStream input = this.getClass().getResourceAsStream("gsk-hds.xml")) {
      data = parser.process(input);
      metadata = parser.getMetadata();
      data.setGskSeries(parser.getSeriesList());
    }

    assertNotNull(data);
    assertEquals("ID1", data.getDocumentIdentification());

    assertEquals(1, data.getGskSeries().size());
    GSKSeries series = data.getGskSeries().get(0);
    assertEquals(BusinessType.Z02, series.getBusinessType());
    assertEquals("ABC", series.getArea());
    assertEquals(1, series.getManualGSKBlock().size());

    ManualGSKBlock manualBlock = series.getLastManualBlock();
    assertEquals("ManualBlock1", manualBlock.getGskName());
    assertEquals(timeInterval, manualBlock.getTimeInterval());
    assertEquals(2, manualBlock.getManualNodes().size());
    assertEquals("Node1m", manualBlock.getManualNodes().get(0).getNodeName());
    assertEquals(BigDecimal.valueOf(0.536), manualBlock.getManualNodes().get(0).getFactor());

    assertEquals(1, series.getCountryGSKBlock().size());
    CountryGSKBlock countryBlock = series.getLastCountryBlock();
    assertEquals("CountryBlock1", countryBlock.getGskName());
    assertEquals(timeInterval, countryBlock.getTimeInterval());
    assertEquals(1, series.getAutoGSKBlocks().size());

    AutoGSKBlock autoBlock = series.getLastAutoBlock();
    assertEquals("AutoBlock1", autoBlock.getGskName());
    assertEquals(timeInterval, autoBlock.getTimeInterval());
    assertEquals(2, autoBlock.getAutoNodes().size());
    assertEquals("Node1a", autoBlock.getAutoNodes().get(0).getNodeName());
    assertEquals("Node2a", autoBlock.getAutoNodes().get(1).getNodeName());

    assertNotNull(metadata);
    assertEquals(DocumentType.B22, metadata.getDocumentType());
    assertEquals("SENDER-EIC", metadata.getSender());
    assertEquals("RECEIVER-EIC", metadata.getReceiver());
    assertEquals(ZonedDateTime.parse("2019-02-13T09:30:00Z"), metadata.getCreationDateTime());
    assertEquals(timeInterval, metadata.getTimeInterval());
    assertEquals("10Y1001C--00059P", metadata.getDomain());
  }

  @Test
  public void noDataSeries() throws IOException {
    GSKDocument data;

    try (InputStream input = this.getClass().getResourceAsStream("gsk-noSeries.xml")) {
      data = parser.process(input);
      data.setGskSeries(parser.getSeriesList());
    }

    assertTrue(data.getGskSeries().isEmpty());
  }

  @Test(expected = DeserializationException.class)
  public void deserializationFailed() throws IOException {
    GSKDocument data;

    try (InputStream input = this.getClass().getResourceAsStream("no-file.xml")) {
      data = parser.process(input);
      data.setGskSeries(parser.getSeriesList());
    }
  }

}
