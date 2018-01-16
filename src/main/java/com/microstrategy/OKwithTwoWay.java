package com.microstrategy;

import okhttp3.*;


import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class OKwithTwoWay {
    private static final String KEYSTORE_LOCATION="/Users/ztang/tony_thompson_google_drive/usher/assets/cert.p12";
    private static final String KEYSTORE_PASS="123";

    public static final void main(String[] args) {
        KeyStore clientKeyStore;

        try(FileInputStream fileInputStream = new FileInputStream(KEYSTORE_LOCATION)) {
            clientKeyStore = KeyStore.getInstance("PKCS12");
            clientKeyStore.load(fileInputStream, KEYSTORE_PASS.toCharArray());

            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(clientKeyStore, KEYSTORE_PASS.toCharArray()).build();
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore)null);
            X509TrustManager x509TrustManager=null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                System.out.println(trustManager);

                if (trustManager instanceof X509TrustManager) {
                  x509TrustManager = (X509TrustManager) trustManager;
                  break;
                }
            }


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory,x509TrustManager);
            OkHttpClient okHttpClient = builder.build();
            Request request = request = new Request.Builder()
                    .url("https://env-53982.customer.cloud.microstrategy.com:3443/org/orgbasicinfo/2")
                    .build();

            for(int i = 0;i<10;i++){
                Response response = okHttpClient.newCall(request).execute();
                System.out.println("HTTP Response Code: " + response.code());
                System.out.println("Connection: " + response.header("Connection"));
                System.out.println("response body : \n" + response.body().string());
                System.out.println();
            }

            /*for(int i=0;i<10;i++){
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                            Headers responseHeaders = response.headers();
                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            }

                            System.out.println(responseBody.string());
                        }
                    }
                });
            }*/



        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }catch (NoSuchAlgorithmException|CertificateException nsce){
            System.out.println(nsce.getMessage());
        }catch (KeyStoreException kse){
            System.out.println(kse.getMessage());
        }catch(UnrecoverableKeyException uke){
            System.out.println(uke.getMessage());
        }catch (KeyManagementException kme){
            System.out.println(kme.getMessage());
        }


    }

}
