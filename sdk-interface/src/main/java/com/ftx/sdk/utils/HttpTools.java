package com.ftx.sdk.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class HttpTools {

    private static Logger logger = LoggerFactory.getLogger(HttpTools.class);

    private static final String UTF_8 = "UTF-8";

    private static SSLSocketFactory ssf;
    private static SSLConnectionSocketFactory sslConnectionSocketFactory;


    private static final Pattern blankPattern = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            return true;
        }
    };

    /**
     * 忽视证书 Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {
        private X509Certificate[] certificates;

        public void checkClientTrusted(X509Certificate[] certificates, String authType)
                throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }

        }

        public void checkServerTrusted(X509Certificate[] ax509certificate, String s)
                throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    };

    static {

        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);

        // Prepare SSL Context
        try
        {
            TrustManager[] tm = {ignoreCertificationTrustManger};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        }
        catch (Exception e)
        {
        }
    }

    public static String urlPadding(String urlStr){
        urlStr = blankPattern.matcher(urlStr).replaceAll("");
        if (!(urlStr.startsWith("http://") || urlStr.startsWith("https://")))
            urlStr = "http://" + urlStr;
        if (urlStr.endsWith("/"))
            urlStr = urlStr.substring(0, urlStr.length() -1);
        return urlStr;
    }

    public static String doGetWithHeaders(String url, Map<String, String> params, Map<String, String> headers) {
        return doGet(url, params, UTF_8, headers);
    }

    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, UTF_8, null);
    }

    public static String doGet(String url, Map<String, String> params, String charset, Map<String, String> headers) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            if (pairs.size() > 0) {
                //UrlEncodedFormEntity将list拼接车成param1=value1&param2=value2
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            logger.debug("request url: " + url);
            HttpGet httpGet = new HttpGet(url);
            if (null != headers && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    if (!Strings.isNullOrEmpty(entry.getKey()) && !Strings.isNullOrEmpty(entry.getValue())) {
                        httpGet.setHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            return execute(httpGet);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doGetByCookie(String url, Map<String, String> params, Map<String, String> cookie) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            if (pairs.size() > 0) {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, UTF_8));
            }
            HttpGet httpGet = new HttpGet(url);
            if (null != cookie) {
                StringBuilder cookieBuf = new StringBuilder();
                for (Entry<String, String> entry : cookie.entrySet()) {
                    cookieBuf.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .append(";");
                }
                httpGet.setHeader("Cookie", cookieBuf.toString());
            }
            return execute(httpGet);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }


    public static String doPost(String url, Map<String, String> params) {
        return doPost(url, params, UTF_8);
    }

    public static String doPost(String url, Map<String, String> params, String charset) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            HttpPost httpPost = new HttpPost(url);
            if (pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doPostWithEmpty(String url, Map<String, String> params) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPairWithEmpty(params);
            HttpPost httpPost = new HttpPost(url);
            if (pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doPostByJson(String url, String param) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (!Strings.isNullOrEmpty(param)) {
                httpPost.setEntity(new StringEntity(param, ContentType.APPLICATION_JSON));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doPostByJsonUrlencoded(String url, String param) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (!Strings.isNullOrEmpty(param)) {
                httpPost.setEntity(new StringEntity(param, ContentType.APPLICATION_FORM_URLENCODED));
            }
            return execute(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    public static String doPost_IgnoreCertification(String url, Map<String, String> params) {
        return doPost_IgnoreCertification(url, params, UTF_8);
    }

    public static String doPost_IgnoreCertification(String url, Map<String, String> params, String charset) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = buildPair(params);
            HttpPost httpPost = new HttpPost(url);
            if (pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }
            return execute_IgnoreCertification(httpPost);
        } catch (Exception e) {
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通过HttpURLConnection POST请求 (包括http和https请求)
     * @param url
     * @param params
     * @return
     */
    public static String doPostByConnection(String url, String params) {
        return doPostByConnection(url, params, UTF_8);
    }

    public static String doPostByConnectionWithHeaders(String url, String params, Map<String, String> headers){
        return doPostByConnectionWithHeaders(url, params, headers, UTF_8);
    }

    /**
     *  通过HttpURLConnection POST请求
     * @param url
     * @param params
     * @param encoding
     * @return
     */
    public static String doPostByConnection(String url, String params, String encoding) {
        StringBuffer resultSb = new StringBuffer();
        HttpURLConnection urlCon = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {

            URL dataUrl = new URL(url);
            urlCon = (HttpURLConnection) dataUrl.openConnection();
            if (url.indexOf("https://") >= 0) {
                ((HttpsURLConnection)urlCon).setSSLSocketFactory(ssf);
            }
//            String dataSb = null;
//            List<NameValuePair> pairs = buildPair(params);
//            if (pairs.size() > 0) {
//                dataSb =  EntityUtils.toString(new UrlEncodedFormEntity(pairs, encoding));
//            }
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("content-type", "text/json");
            urlCon.setRequestProperty("Proxy-Connection", "Keep-Alive");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setConnectTimeout(60000);
            urlCon.setReadTimeout(15000);
            urlCon.getOutputStream().write(params.getBytes(encoding));
            urlCon.getOutputStream().flush();

            is = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is,encoding));
            String line = null;
            while ((line = reader.readLine()) != null) {
                resultSb.append(line + "\r\n");
            }
            return resultSb.toString();
        } catch(Exception e) {
            logger.error("doPostByConnection HttpUrlConnection request error : {}", e.getMessage(), e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != urlCon) {
                    urlCon.disconnect();
                }
            } catch (Exception e) {
                logger.error("doPostByConnection HttpUrlConnection response close error : {}", e.getMessage(), e);
            }
        }
        return null;
    }

    public static String doPostByConnectionWithHeaders(String url, String params, Map<String, String> headers, String encoding){
        StringBuffer resultSb = new StringBuffer();
        HttpURLConnection urlCon = null;
        InputStream is = null;
        BufferedReader reader = null;

        try {
            URL dataUrl = new URL(url);
            urlCon = (HttpURLConnection) dataUrl.openConnection();
            if (url.indexOf("https://") >= 0) {
                ((HttpsURLConnection)urlCon).setSSLSocketFactory(ssf);
            }

            if( null != headers && 0 < headers.size() ) {
                for(Entry<String, String> header : headers.entrySet()) {
                    urlCon.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            urlCon.setRequestMethod("POST");
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setConnectTimeout(60000);
            urlCon.setReadTimeout(15000);
            urlCon.getOutputStream().write(params.getBytes(encoding));
            urlCon.getOutputStream().flush();
            is = urlCon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is,encoding));
            String line = null;
            while ((line = reader.readLine()) != null) {
                resultSb.append(line + "\r\n");
            }
            return resultSb.toString();
        } catch (IOException e) {
            logger.error("doPostByConnectionWithHeaders HttpUrlConnection request error : {}", e.getMessage(), e);
        } finally {
            try {
                if(null != reader){
                    reader.close();
                }
                if (null != is) {
                    is.close();
                }
                if(null != urlCon) {
                    urlCon.disconnect();
                }
            } catch (IOException e) {
                logger.error("doPostByConnectionWithHeaders HttpClient response close error : {}", e.getMessage(), e);
            }
        }
        return null;
    }

    private static List<NameValuePair> buildPair(Map<String, String> params) {
        List<NameValuePair> pairs = Lists.newArrayList();
        if (null != params && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (!Strings.isNullOrEmpty(entry.getKey()) && !Strings.isNullOrEmpty(entry.getValue())) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
        }
        return pairs;
    }

    private static List<NameValuePair> buildPairWithEmpty(Map<String, String> params) {
        List<NameValuePair> pairs = Lists.newArrayList();
        if (null != params && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (!Strings.isNullOrEmpty(entry.getKey())) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
        }
        return pairs;
    }

    private static String execute(HttpUriRequest requestMethod) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).setConnectTimeout(60000).setSocketTimeout(15000).build();
            httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            response = httpclient.execute(requestMethod);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String result = null == entity ? null : EntityUtils.toString(entity, UTF_8);
                EntityUtils.consume(entity);
                return result;
            } else {
                requestMethod.abort();
                logger.error("Execute HttpClient response error, request: {}, status code: {} , reason phrase: {} !",
                        requestMethod.getURI(), status, response.getStatusLine().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            requestMethod.abort();
            logger.error("uri: {}, HttpClient request error : {}", requestMethod.getURI(), e.getMessage(), e);
        } finally {
            try {
                if (null != response)
                    response.close();
                if (null != httpclient) {
                    httpclient.close();
                }
            } catch (IOException e) {
                logger.error("HttpClient response close error : {}", e.getMessage(), e);
            }
        }
        return null;
    }

    private static String execute_IgnoreCertification(HttpUriRequest requestMethod) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClientBuilder.create().setSSLSocketFactory(sslConnectionSocketFactory).build();
            response = httpclient.execute(requestMethod);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String result = null == entity ? null : EntityUtils.toString(entity, UTF_8);
                EntityUtils.consume(entity);
                return result;
            } else {
                requestMethod.abort();
                logger.error("Execute IgnoreCertification HttpClient response error, request: {}, status code: {} , reason phrase: {} !", requestMethod.getURI(), status, response.getStatusLine().getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            requestMethod.abort();
            logger.error("HttpClient request error : {}", e.getMessage(), e);
        } finally {
            try {
                if (null != response)
                    response.close();
                if (null != httpclient)
                    httpclient.close();
            } catch (IOException e) {
                logger.error("HttpClient response close error : {}", e.getMessage(), e);
            }
        }
        return null;
    }
}
