import java.util.ArrayList;
import java.util.HashMap;
import edu.utec.sdk.moveon.common.MoveonResponseParser;
import edu.utec.sdk.moveon.core.MoveonClient;
import edu.utec.sdk.moveon.core.request.receive.ReceiveRequest;
import edu.utec.sdk.moveon.crendentials.P12CertificateFileCredentials;

public class TestSimple3 {

  public static void main(String[] args) throws Exception {

    MoveonClient client = new MoveonClient();
    client.setApiFullUrl("https://acme.com/restService/index.php");
    client.setResponseCharset("UTF-8");

    P12CertificateFileCredentials credentials = new P12CertificateFileCredentials();
    credentials.setCertificateFileAbsolutePath("/../ssl.p12");
    credentials.setCertificatePassword("***");

    client.setCredentials(credentials);

    client.configure();

    ReceiveRequest request = new ReceiveRequest();
    request.setMethod("get");
    request.setId("123456");

    String xml = client.receiveRequest(request);
    System.out.println(xml);
    String dataString = MoveonResponseParser.getReceivedData(xml);
    HashMap<?, ?> data = (HashMap<?, ?>) MoveonResponseParser.getData(dataString);

    ArrayList<?> rows = (ArrayList<?>) data.get("rows");
    System.out.println(rows.size());

  }

}
