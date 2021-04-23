# Method 1: Using p12 file

- create account in moveon
- create a .p12 file (https://blog.pavelsklenar.com/how-to-create-pkcs-12-for-your-application/)
- P12CertificateFileCredentials.java


# Method 2: Using p12 file as base64

- create account in moveon
- create a .p12 file (https://blog.pavelsklenar.com/how-to-create-pkcs-12-for-your-application/)
- convert .p12 file to base64 text

```
base64 -w 0 /tmp/certificate.p12
```
- P12Base64CertificateContentCredentials-java