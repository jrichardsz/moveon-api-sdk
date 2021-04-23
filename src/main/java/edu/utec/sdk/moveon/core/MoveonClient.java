package edu.utec.sdk.moveon.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import edu.utec.sdk.moveon.common.MoveonResponseParser;
import edu.utec.sdk.moveon.core.request.receive.ReceiveRequest;
import edu.utec.sdk.moveon.core.request.search.SearchRequest;

public class MoveonClient {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private String apiFullUrl;
  private HttpURLConnection httpURLConnection;
  private String responseCharset;
  private String moveonContentType = MediaType.APPLICATION_FORM_URLENCODED.toString();
  private Map<String, String> headers;
  private MoveonCredentials credentials;

  /**
   * This constructor initializes a new HTTP POST request with content type is set to
   * multipart/form-data
   *
   * @param requestURL
   * @param charset
   * @param headers
   * @param queryParams
   * @throws IOException
   */
  public void configure() throws Exception {
    URL url = new URL(getApiFullUrl());
    httpURLConnection = (HttpURLConnection) url.openConnection();

    credentials.configureSecurity(this);

    httpURLConnection.setUseCaches(false);
    httpURLConnection.setDoOutput(true); // indicates POST method
    httpURLConnection.setDoInput(true);
    httpURLConnection.setRequestProperty("Content-Type", moveonContentType);
    if (getHeaders() != null && getHeaders().size() > 0) {
      Iterator<String> it = getHeaders().keySet().iterator();
      while (it.hasNext()) {
        String key = it.next();
        if (key != null && key.toLowerCase().contentEquals("content-type")) {
          logger.warn(
              "Content-Type header is skipped because moveon just support: " + moveonContentType);
        }
        String value = getHeaders().get(key);
        httpURLConnection.setRequestProperty(key, value);
      }
    }
  }


  public Map<String, String> getHeaders() {
    return headers;
  }


  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }


  /**
   * Convert the request fields to a byte array
   *
   * @param params
   * @return
   * @throws Exception
   */
  private byte[] searchRequestToBytes(SearchRequest request) throws Exception {

    String bodyTemplate = "method=%s&entity=%s&action=%s&data=%s";
    String filters = objectToJson(request.getSearchFilters());
    request.getSearchPreferences().setFilters(filters);
    String data = objectToJson(request.getSearchPreferences());
    String body = String.format(bodyTemplate, request.getMethod(), request.getEntity(),
        request.getAction(), data);
    logger.debug("body:" + body);
    try {
      return body.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new Exception("Failed to create form parameters as byte[].", e);
    }
  }

  private byte[] receiveRequestToBytes(ReceiveRequest request) throws Exception {

    String bodyTemplate = "method=%s&id=%s";
    String body = String.format(bodyTemplate, request.getMethod(), request.getId());
    logger.debug("body:" + body);
    try {
      return body.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new Exception("Failed to create form parameters as byte[].", e);
    }
  }

  private String objectToJson(Object obj) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new Exception("Failed to create obj as json string.", e);
    }
  }


  public String registerRequest(SearchRequest request) throws Exception {
    byte[] postDataBytes = searchRequestToBytes(request);
    return performHttpRequest(postDataBytes);
  }

  public String receiveRequest(ReceiveRequest request) throws Exception {
    byte[] postDataBytes = receiveRequestToBytes(request);
    return performHttpRequest(postDataBytes);
  }

  public Map<?, ?> registerAndReceiveRequest(SearchRequest request) throws Exception {
    byte[] postDataBytes = searchRequestToBytes(request);
    String registerResponse = performHttpRequest(postDataBytes);
    String queueId = MoveonResponseParser.getQueueId(registerResponse);
    if (queueId == null || queueId.isEmpty()) {
      throw new Exception(
          "queueId not found in moveon register response. Response: " + registerResponse);
    }

    configure();

    ReceiveRequest requestReceive = new ReceiveRequest();
    requestReceive.setMethod("get");
    requestReceive.setId(queueId);

    String receiveResponse = receiveRequest(requestReceive);
    String dataString = null;

    try {
      dataString = MoveonResponseParser.getReceivedData(receiveResponse);
    } catch (Exception e) {
      throw new Exception(
          "Failed to get <data> content from receive response. Response: " + receiveResponse);
    }

    if (dataString == null || dataString.isEmpty()) {
      throw new Exception(
          "<data> content not found in moveon receive response. Response: " + receiveResponse);
    }

    HashMap<?, ?> data = null;
    try {
      data = (HashMap<?, ?>) MoveonResponseParser.getData(dataString);
    } catch (Exception e) {
      throw new Exception(
          "Failed to parse <data> content from receive response. Response: " + receiveResponse);
    }

    return data;
  }

  private String performHttpRequest(byte[] postDataBytes) throws Exception {
    String response = "";
    getHttpURLConnection().getOutputStream().write(postDataBytes);
    // Check the http status
    int status = getHttpURLConnection().getResponseCode();
    if (status == HttpURLConnection.HTTP_OK) {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = getHttpURLConnection().getInputStream().read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      response = result.toString(getResponseCharset());
      getHttpURLConnection().disconnect();
    } else {
      throw new IOException("Server returned non-ok status: " + status);
    }
    return response;
  }

  public String getApiFullUrl() {
    return apiFullUrl;
  }

  public void setApiFullUrl(String apiFullUrl) {
    this.apiFullUrl = apiFullUrl;
  }

  public HttpURLConnection getHttpURLConnection() {
    return httpURLConnection;
  }

  public void httpURLConnection(HttpURLConnection httpURLConnection) {
    this.httpURLConnection = httpURLConnection;
  }

  public String getResponseCharset() {
    return responseCharset;
  }

  public void setResponseCharset(String responseCharset) {
    this.responseCharset = responseCharset;
  }

  public MoveonCredentials getCredentials() {
    return credentials;
  }

  public void setCredentials(MoveonCredentials credentials) {
    this.credentials = credentials;
  }

}
