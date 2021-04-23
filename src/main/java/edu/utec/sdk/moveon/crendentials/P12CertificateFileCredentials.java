package edu.utec.sdk.moveon.crendentials;

import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import edu.utec.sdk.moveon.core.MoveonClient;
import edu.utec.sdk.moveon.core.MoveonCredentials;

public class P12CertificateFileCredentials implements MoveonCredentials {

  private String certificateFileAbsolutePath;
  private String certificatePassword;

  @Override
  public void configureSecurity(MoveonClient moveonClient) throws Exception {
    if (getCertificateFileAbsolutePath() == null || getCertificateFileAbsolutePath().isEmpty()) {
      throw new Exception(
          "Certificate content as base64 is required to stabilish a connection to moveon api");
    }

    try {

      FileInputStream fis = new FileInputStream(getCertificateFileAbsolutePath());
      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(fis, getCertificatePassword().toCharArray());

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

  public String getCertificateFileAbsolutePath() {
    return certificateFileAbsolutePath;
  }

  public void setCertificateFileAbsolutePath(String certificateFileAbsolutePath) {
    this.certificateFileAbsolutePath = certificateFileAbsolutePath;
  }



  public String getCertificatePassword() {
    return certificatePassword;
  }

  public void setCertificatePassword(String certificatePassword) {
    this.certificatePassword = certificatePassword;
  }
}
