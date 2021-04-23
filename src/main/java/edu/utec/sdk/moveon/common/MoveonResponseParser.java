package edu.utec.sdk.moveon.common;

import java.io.StringReader;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

public class MoveonResponseParser {

  public static String getStatus(XPath xpath, Document document) throws XPathExpressionException {
    return xpath.evaluate("/rest/queue/status", document);
  }

  public static String getResponse(XPath xpath, Document document) throws XPathExpressionException {
    return xpath.evaluate("/rest/queue/response", document);
  }

  public static String getQueueId(String xml) throws Exception {

    InputSource source = new InputSource(new StringReader(xml));

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(source);

    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();

    String response = getResponse(xpath, document);
    String status = getStatus(xpath, document);

    if (status != null && !status.contentEquals("success")) {
      throw new Exception("Moveon api returns an error:" + xml);
    }

    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<?, ?> responseData = mapper.readValue(response, Map.class);
      return "" + ((Integer) responseData.get("queueId")).intValue();
    } catch (Exception e) {
      throw new Exception("Failed to get queueId from moveon response: " + response, e);
    }
  }

  public static String getReceivedData(String xml) throws Exception {

    InputSource source = new InputSource(new StringReader(xml));

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(source);

    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();

    return xpath.evaluate("/rest/get/response", document);
  }

  public static Map<?, ?> getData(String xml) throws Exception {

    // these steps are due to ugly and stinky string moveon api response
    // it has xml with raw chars

    // remove quotes from start and end of xml
    String fix1 = xml.substring(1, xml.length() - 1);
    // fix wrong xml
    String fix2 = fix1.replace("<\\/", "</").replace("\\/>", "/>");

    // due to xml response could have any kind of fields,
    // we transform it to a map instead of bean
    XMLSerializer xmlSerializer = new XMLSerializer();
    JSON json = xmlSerializer.read(fix2);

    ObjectMapper mapper = new ObjectMapper();
    Map<?, ?> data = mapper.readValue(json.toString(2), Map.class);
    return data;

  }
}
