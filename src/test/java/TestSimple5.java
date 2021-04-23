import java.util.ArrayList;
import java.util.HashMap;
import edu.utec.sdk.moveon.core.MoveonClient;
import edu.utec.sdk.moveon.core.request.search.Rule;
import edu.utec.sdk.moveon.core.request.search.SearchFilter;
import edu.utec.sdk.moveon.core.request.search.SearchPreferences;
import edu.utec.sdk.moveon.core.request.search.SearchRequest;
import edu.utec.sdk.moveon.crendentials.P12CertificateFileCredentials;

public class TestSimple5 {

  public static void main(String[] args) throws Exception {

    MoveonClient client = new MoveonClient();
    client.setApiFullUrl("https://acme.com/restService/index.php");
    client.setResponseCharset("UTF-8");

    P12CertificateFileCredentials credentials = new P12CertificateFileCredentials();
    credentials.setCertificateFileAbsolutePath("/../ssl.p12");
    credentials.setCertificatePassword("***");

    client.setCredentials(credentials);

    client.configure();

    SearchPreferences searchPreferences = new SearchPreferences();

    searchPreferences.setVisibleColumns("stay.name;stay.start_date;stay.end_date");
    searchPreferences.setLocale("eng");
    searchPreferences.setSidx("stay.name;stay.start_date");
    searchPreferences.setSortName("stay.name");
    searchPreferences.setSortOrder("asc");
    searchPreferences.setSearch("true");
    searchPreferences.setPage("1");
    searchPreferences.setRows("100");
    searchPreferences.setSord("");

    SearchFilter filters = new SearchFilter();
    filters.setGroupOp("AND");
    ArrayList<Rule> rules = new ArrayList<Rule>();
    rules.add(new Rule("stay.name", "cn", "Alvarez"));
    filters.setRules(rules);

    SearchRequest requestCreate = new SearchRequest();
    requestCreate.setAction("list");
    requestCreate.setEntity("stay");
    requestCreate.setMethod("queue");
    requestCreate.setSearchPreferences(searchPreferences);
    requestCreate.setSearchFilters(filters);

    HashMap<?, ?> data = (HashMap<?, ?>) client.registerAndReceiveRequest(requestCreate);
    System.out.println(data);

  }

}
