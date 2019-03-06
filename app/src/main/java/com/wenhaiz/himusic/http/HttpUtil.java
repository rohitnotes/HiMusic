package com.wenhaiz.himusic.http;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.wenhaiz.himusic.utils.MD5Util;
import com.wenhaiz.himusic.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    static final String SP_COOKIES = "cookies_share";

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    private static HashMap<String, String> sHeaders = new HashMap<>();

    static {
        sHeaders.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");
        sHeaders.put("referer", API.XiaMi.BASE);
    }


    private static final OkHttpClient sClient = SingletonHolder.sClient;


    public static OkHttpClient getHttpClient() {
        return sClient;
    }


    private static class SingletonHolder {
        private static final OkHttpClient sClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new LoadCookiesInterceptor())
                .addInterceptor(new SaveCookiesInterceptor())
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
    }


    public static void get(String url, HttpCallback callback) {
        get(url, null, callback);
    }


    public static void get(String url, @Nullable HashMap<String, Object> params,
                           final HttpCallback httpCallback) {
        Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                uriBuilder.appendQueryParameter(key, params.get(key).toString());
            }
        }

        Request.Builder builder = new Request.Builder()
                .url(uriBuilder.toString())
                .headers(Headers.of(sHeaders))
                .get();
        sendRequest(builder.build(), httpCallback);
    }


    public static void post(String url, @Nullable HashMap<String, Object> params,
                            HttpCallback httpCallback) {
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(MEDIA_JSON, gson.toJson(params).trim());

        Request.Builder builder = new Request.Builder()
                .url(url)
                .headers(Headers.of(sHeaders))
                .post(body);
        Request request = builder.build();
        sendRequest(request, httpCallback);
    }


    private static void sendRequest(Request request, final HttpCallback httpCallback) {
        Call call = getHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (httpCallback == null || call.isCanceled()) {
                    return;
                }
                httpCallback.onHttpFailure("0", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (httpCallback == null || call.isCanceled()) {
                    return;
                }
                httpCallback.onHttpResponse(response);
            }

        });

    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ssfFactory;
    }


    public static String getXiamiUrl(String api, HashMap<String, Object> params) {
        String token = SharedPreferencesUtils.getToken();
        Gson gson = new Gson();
        String paramsJson = "";
        if (params != null && !params.isEmpty()) {
            paramsJson = gson.toJson(params).trim().replaceAll(" \\s*", "");
            System.out.println(paramsJson);
        }

        String origin = token + "_xmMain_" + api + "_" + paramsJson;
        System.out.println(origin);

        return API.XiaMi.BASE + api + "?_q=" + URLEncoder.encode(paramsJson).replace("%3A",":")
                + "&_s=" + MD5Util.hexDigest(origin);
    }
}
