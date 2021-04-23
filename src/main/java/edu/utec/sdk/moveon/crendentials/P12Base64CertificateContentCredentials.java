package edu.utec.sdk.moveon.crendentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import edu.utec.sdk.moveon.core.MoveonClient;
import edu.utec.sdk.moveon.core.MoveonCredentials;

public class P12Base64CertificateContentCredentials implements MoveonCredentials {

  private String certificateContent;
  private String certificatePassword;

  @Override
  public void configureSecurity(MoveonClient moveonClient) throws Exception {
    if (getCertificateContent() == null || getCertificateContent().isEmpty()) {
      throw new Exception(
          "Certificate content as base64 is required to stabilish a connection to moveon api");
    }

    try {
      byte[] decodedCertificate = Base64.getMimeDecoder().decode(getCertificateContent());
      InputStream decodedCertificateStream = new ByteArrayInputStream(decodedCertificate);

      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(decodedCertificateStream, getCertificatePassword().toCharArray());

      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, getCertificatePassword().toCharArray());
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(kmf.getKeyManagers(), null, null);

      if (moveonClient.getHttpURLConnection() instanceof HttpsURLConnection) {
        ((HttpsURLConnection) moveonClient.getHttpURLConnection())
            .setSSLSocketFactory(sc.getSocketFactory());
      }
    } catch (Exception e) {
      throw new Exception("Failed to add certificate security to request connection.", e);
    }
  }

  public String getCertificateContent() {
    return certificateContent;
  }

  public void setCertificateContent(String certificateContent) {
    this.certificateContent = certificateContent;
  }

  public String getCertificatePassword() {
    return certificatePassword;
  }

  public void setCertificatePassword(String certificatePassword) {
    this.certificatePassword = certificatePassword;
  }
}
