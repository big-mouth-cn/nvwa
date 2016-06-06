/*
 * Copyright 2015 big-mouth.cn
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.network.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * HttpClient Helper
 * @since 1.0
 * @author Allen
 */
public final class HttpClientHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
    public static final int DEFAULT_TIME_OUT = 60000;
    public static final String DEFAULT_CHARSET = "UTF-8";

    private HttpClientHelper() {
    }
    
    public static HttpClient https() {
        return https(443);
    }
    
    public static HttpClient https(int port) {
        try {
            SSLContext ctx = getInstance();
            ClientConnectionManager ccm = new BasicClientConnectionManager();
            return getHttpClient(ctx, ccm, port, DEFAULT_TIME_OUT);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static HttpClient https(File keystore, char[] pwd) {
        return https(keystore, pwd, 443);
    }
    
    public static HttpClient https(File keystore, char[] pwd, int port) {
        try {
            ClientConnectionManager ccm = new BasicClientConnectionManager();
            return getHttpClient(keystore, pwd, ccm, port, DEFAULT_TIME_OUT);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @SuppressWarnings("deprecation")
    private static HttpClient getHttpClient(File keystore, char[] pwd, ClientConnectionManager ccm, int port, int timeout) throws Exception {
        SchemeRegistry sr = ccm.getSchemeRegistry();
        KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
        truststore.load(new FileInputStream(keystore), pwd);
        SSLSocketFactory socketFactory = new SSLSocketFactory(truststore);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        sr.register(new Scheme("https", port, socketFactory));
        HttpClient httpClient = new DefaultHttpClient(ccm);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_KEEPALIVE, true);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
        return httpClient;
    }
    
    private static HttpClient getHttpClient(SSLContext ctx, ClientConnectionManager ccm, int port, int timeout) {
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", port, new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
        HttpClient httpClient = new DefaultHttpClient(ccm);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_KEEPALIVE, true);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
        return httpClient;
    }
    
    private static SSLContext getInstance() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");  
        ctx.init(null, new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws java.security.cert.CertificateException {
            }
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws java.security.cert.CertificateException {
            }
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, null);
        return ctx;
    }

    public static HttpClient http() {
        return http(DEFAULT_TIME_OUT);
    }

    public static HttpClient http(int timeout) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_KEEPALIVE, true);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
        return httpClient;
    }
    
    public static HttpGet get(String uri) {
        return get(uri, null, null);
    }
    
    public static HttpGet get(String uri, Response response) {
        return get(uri, response, null);
    }
    
    public static HttpGet get(String uri, Header[] headers) {
        return get(uri, null, headers);
    }
    
    public static HttpGet get(String url, Response response, Header[] headers) {
        HttpGet httpGet = new HttpGet(url);
        setRequestBaseHeader(httpGet);
        if (null != response) {
            setRequestCookie(httpGet, getCookieHeaders(response));
        }
        setRequestHeader(httpGet, headers);
        return httpGet;
    }
    
    
    public static HttpPost post(String uri) {
        return post(uri, null, null);
    }
    
    public static HttpPost post(String uri, Response response) {
        return post(uri, response, null);
    }
    
    public static HttpPost post(String uri, Header[] headers) {
        return post(uri, null, headers);
    }
    
    public static HttpPost post(String url, Response response, Header[] headers) {
        HttpPost httpPost = new HttpPost(url);
        setRequestBaseHeader(httpPost);
        if (null != response) {
            setRequestCookie(httpPost, getCookieHeaders(response));
        }
        setRequestHeader(httpPost, headers);
        return httpPost;
    }

    private static Header[] getCookieHeaders(Response response) {
        List<Header> cookies = Lists.newArrayList();
        List<Header> responseHeaders = response.getHeaders();
        for (Header header : responseHeaders) {
            if (StringUtils.equals(header.getName(), "Set-Cookie")) {
                cookies.add(header);
            }
        }
        return cookies.toArray(new Header[0]);
    }
    
    private static void setRequestBaseHeader(HttpRequestBase httpRequest) {
        httpRequest.setHeader("Connection", "keep-alive");
        httpRequest.setHeader("Proxy-Connection", "keep-alive");
        httpRequest.setHeader("Accept", "*/*");
        httpRequest.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        httpRequest.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
    }
    
    private static void setRequestHeader(HttpRequestBase httpRequest, Header[] headers) {
        List<Header> cookies = Lists.newArrayList();
        if (ArrayUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                if (header.getName().equals("Set-Cookie")) {
                    cookies.add(header);
                }
                else {
                    httpRequest.addHeader(header);
                }
            }
        }
        setRequestCookie(httpRequest, cookies.toArray(new Header[0]));
    }
    
    protected static void setRequestCookie(HttpRequestBase httpRequest, Header[] headers) {
        StringBuilder sb = new StringBuilder(128);
        if (ArrayUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                if (StringUtils.equals(header.getName(), "Set-Cookie")) {
                    String value = header.getValue();
                    if (StringUtils.isNotBlank(value)) {
                        // Because of the need to remove the effective range: path=/
                        String[] values = StringUtils.split(value, ";");
                        for (String string : values) {
                            String[] v = string.split("=");
                            if (v.length >= 2)
                                sb.append(v[0]).append("=").append(v[1]).append(";");
                        }
                    }
                }
            }
        }
        httpRequest.addHeader("Cookie", sb.toString());
    }
    
    public static void addByteArrayEntity(HttpEntityEnclosingRequestBase requestBase, byte[] b, ContentType contentType) {
        requestBase.setEntity(new ByteArrayEntity(b, contentType));
    }
    
    public static void addPair(HttpEntityEnclosingRequestBase requestBase, NameValuePair pair) throws UnsupportedEncodingException {
        addPair(requestBase, new NameValuePair[] { pair });
    }
    
    public static void addPair(HttpEntityEnclosingRequestBase requestBase, NameValuePair... pairs) throws UnsupportedEncodingException {
        List<NameValuePair> list = Lists.newArrayList(pairs);
        requestBase.setEntity(new UrlEncodedFormEntity(list, DEFAULT_CHARSET));
    }
    
    public static void addByteArrayEntity(HttpEntityEnclosingRequestBase requestBase, byte[] b) {
        requestBase.setEntity(new ByteArrayEntity(b));
    }
    
    public static void addPair(HttpGet httpGet, NameValuePair...pairs) throws URISyntaxException {
        URI uri = httpGet.getURI();
        StringBuilder param = new StringBuilder();
        
        for (int i = 0; i < pairs.length; i++) {
            NameValuePair nameValuePair = pairs[i];
            if (i == 0) {
                // first
                param.append("?");
            }
            param.append(nameValuePair.getName()).append("=").append(nameValuePair.getValue());
            if (i < pairs.length - 1) {
                param.append("&");
            }
        }
        httpGet.setURI(new URI(uri + param.toString()));
    }
    
    public static <T> T fromJson(String json, Class<T> cls) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, cls);
    }
    
    public static boolean isGZIPEncoding(HttpResponse response) {
        for (Header header : response.getAllHeaders()) {
            if (StringUtils.equals("Content-Encoding", header.getName())) {
                return StringUtils.equals("gzip", header.getValue());
            }
        }
        return false;
    }
    
    public static String getResponseBody(HttpResponse httpResponse) throws IllegalStateException, IOException {
        return getResponseBody(httpResponse, false);
    }
    
    public static String getResponseBody(HttpResponse httpResponse, boolean decode) throws IllegalStateException, IOException {
        return getResponseBody(httpResponse, decode, DEFAULT_CHARSET);
    }
    
    public static String getResponseBody(HttpResponse httpResponse, boolean decode, String charset) throws IllegalStateException, IOException {
        boolean isGzip = isGZIPEncoding(httpResponse);
        return isGzip ?
                getResponseBodyAsGZIP(httpResponse, decode, charset) : getResponseBodyAsString(httpResponse, decode, charset);
    }
    
    public static String getResponseBodyAsGZIP(HttpResponse httpResponse, boolean decode, String charset) throws IllegalStateException, IOException {
        if (null == httpResponse) {
            return null;
        }
        BufferedReader br = null;
        GZIPInputStream gzipis = null;
        InputStream is = null;
        try {
            is = httpResponse.getEntity().getContent();
            gzipis = new GZIPInputStream(is);
            br = new BufferedReader(new InputStreamReader(gzipis, charset));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ( (line = br.readLine()) != null ) {
                content.append(decode ? URLDecoder.decode(line, charset) : line);
            }
            return content.toString();
        }
        finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(is);
        }
    }
    
    public static String getResponseBodyAsString(HttpResponse httpResponse, boolean decode, String charset) throws IllegalStateException, IOException {
        if (null == httpResponse) {
            return null;
        }
        BufferedReader br = null;
        InputStream is = null;
        try {
            is = httpResponse.getEntity().getContent();
            br = new BufferedReader(new InputStreamReader(is, charset));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ( (line = br.readLine()) != null ) {
                content.append(decode ? URLDecoder.decode(line, charset) : line);
            }
            return content.toString();
        }
        finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(is);
        }
    }
    
    public static File getResponseBodyAsFile(HttpResponse httpResponse, String filePath) throws IllegalStateException, IOException {
        if (null == httpResponse) {
            return null;
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        File file = null;
        try {
            is = httpResponse.getEntity().getContent();
            bis = new BufferedInputStream(is);
            file = new File(filePath);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[2048];
            int len;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
        }
        finally {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(is);
        }
        return file;
    }
    
    public static byte[] getResponse(HttpResponse httpResponse) throws IOException {
        if (null == httpResponse) {
            return null;
        }
        ByteArrayOutputStream out = null;
        InputStream is = null;
        try {
            is = httpResponse.getEntity().getContent();
            out = new ByteArrayOutputStream();
            byte[] bytes = new byte[2048];
            int len;
            while ((len = is.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
            return out.toByteArray();
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(is);
        }
    }
    
    public static void printHttpRequestHeader(HttpRequestBase httpRequestBase) {
        if (null == httpRequestBase)
            return;
        org.apache.http.Header[] headers = httpRequestBase.getAllHeaders();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("===============Request Headers===============");
        printHeaders(headers);
    }
    
    public static void printHttpResponseHeader(HttpResponse httpResponse) {
        if (null == httpResponse)
            return;
        org.apache.http.Header[] headers = httpResponse.getAllHeaders();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("===============Response Headers===============");
        printHeaders(headers);
    }
    
    public static void printHeaders(org.apache.http.Header[] headers) {
        if (ArrayUtils.isEmpty(headers))
            return;
        for (org.apache.http.Header header : headers) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(header.getName() + " = " + header.getValue());
            }
        }
    }
}
