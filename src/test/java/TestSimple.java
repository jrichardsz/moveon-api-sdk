import edu.utec.sdk.moveon.core.MoveonClient;
import edu.utec.sdk.moveon.core.request.search.SearchPreferences;
import edu.utec.sdk.moveon.core.request.search.SearchRequest;
import edu.utec.sdk.moveon.crendentials.P12Base64CertificateContentCredentials;

public class TestSimple {

  public static void main(String[] args) throws Exception {

    MoveonClient client = new MoveonClient();
    client.setApiFullUrl("https://acme.com/restService/index.php");
    client.setResponseCharset("UTF-8");

    P12Base64CertificateContentCredentials credentials =
        new P12Base64CertificateContentCredentials();
    credentials.setCertificateContent("****");
    credentials.setCertificatePassword("****");

    client.setCredentials(credentials);

    client.configure();

    SearchPreferences searchPreferences = new SearchPreferences();

    searchPreferences.setFilters(
        "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"stay.name\",\"op\":\"cn\",\"data\":\"Alvarez\"}]}");
    searchPreferences.setVisibleColumns("stay.name;stay.start_date;stay.end_date");
    searchPreferences.setLocale("eng");
    searchPreferences.setSidx("stay.name;stay.start_date");
    searchPreferences.setSortName("stay.name");
    searchPreferences.setSortOrder("asc");
    searchPreferences.setSearch("true");
    searchPreferences.setPage("1");
    searchPreferences.setRows("100");
    searchPreferences.setSord("");

    SearchRequest request = new SearchRequest();
    request.setAction("list");
    request.setEntity("stay");
    request.setMethod("queue");
    request.setSearchPreferences(searchPreferences);

    System.out.println(client.registerRequest(request));

  }

}
