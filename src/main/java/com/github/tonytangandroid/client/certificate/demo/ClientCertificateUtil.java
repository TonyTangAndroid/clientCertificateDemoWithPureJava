package com.github.tonytangandroid.client.certificate.demo;


import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class ClientCertificateUtil {


    public static final String LOCATION = "xu.p12";
    public static final String PASSWORD = "123";

    private static KeyStore provideKeyStore() {


        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream resourceAsStream = ClientCertificateUtil.class.getResourceAsStream(LOCATION);
            keyStore.load(resourceAsStream, PASSWORD.toCharArray());
            return keyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static KeyManager[] getKeyManager() {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(provideKeyStore(), "123".toCharArray());
            return kmf.getKeyManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static SSLSocketFactory provideSSLSocketFactory() {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(getKeyManager(), null, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }


    public static X509TrustManager provideX509TrustManagerV2() throws Exception {


        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        X509TrustManager x509TrustManager = null;
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            System.out.println(trustManager);
            if (trustManager instanceof X509TrustManager) {
                x509TrustManager = (X509TrustManager) trustManager;
                break;
            }
        }

        return x509TrustManager;

    }


}
