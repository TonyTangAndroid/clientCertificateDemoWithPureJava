package com.github.tonytangandroid.client.certificate.demo;

import okhttp3.*;


public class ClientCertificateDemoWithOkHttp {

    public static final String HTTPS_URL = "https://installer-win.9s.microstrategy.com:2443/org/orgbasicinfo/2";

    public static void main(String[] args) throws Exception {


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(ClientCertificateUtil.provideSSLSocketFactory(), ClientCertificateUtil.provideX509TrustManagerV2());
        OkHttpClient okHttpClient = builder.build();
        Request request = new Request.Builder()
                .url(HTTPS_URL)
                .build();

        for (int i = 0; i < 10; i++) {
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody body = response.body();
            System.out.println("code: " + response.code() + ", result:" + (body == null ? "" : body.string()));
            System.out.println();
        }


    }

}
