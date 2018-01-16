package com.microstrategy.helper.ssl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class Helper {


    private static KeyStore provideKeyStore() {


        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(new File("/Users/ztang/tony_thompson_google_drive/usher/assets/cert.p12")), "123".toCharArray());
            return keyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static KeyManager[] getKeyManager() {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
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

}
