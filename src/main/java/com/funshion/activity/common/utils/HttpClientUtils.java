package com.funshion.activity.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhutao on 2016/5/6.
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final CloseableHttpClient HTTPCLIENT;

    private static final int DEFULT_SOCKET_TIMEOUT = 10000;

    private static int SOCKET_TIMEOUT;

    private static final int DEFULT_CONNECT_TIMEOUT = 10000;

    private static int CONNECT_TIMEOUT;

    private static final RequestConfig REQUESTCONFIG;

    private static final int DEFULT_MAX_TOTAL = 300;

    private static final int DEFULT_MAX_PER_ROUTE = 50;


    static {
        SOCKET_TIMEOUT = DEFULT_SOCKET_TIMEOUT;
        CONNECT_TIMEOUT = DEFULT_CONNECT_TIMEOUT;
        logger.debug("########## HTTPClient : socket.timeout:{}, connect.timeout:{} ##########", SOCKET_TIMEOUT, CONNECT_TIMEOUT);
        REQUESTCONFIG = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(DEFULT_MAX_TOTAL);
        manager.setDefaultMaxPerRoute(DEFULT_MAX_PER_ROUTE);
        HTTPCLIENT = HttpClients.custom().setConnectionManager(manager).build();

    }


    public static <T> T get(String url, Class<T> clazz) throws Exception {
        String data = get(url, null, "UTF-8");
        return JSON.parseObject(data, clazz);
    }

    public static <T> T get(String url, Map<String, String> params, Class<T> clazz) throws Exception {
        String data = get(url, params, "UTF-8");
        return JSON.parseObject(data, clazz);
    }

    public static String get(String url) throws Exception {
        return get(url, null, "UTF-8");
    }

    public static String get(String url, int timeout) throws Exception {
        return get(url, null, "UTF-8", timeout);
    }

    public static String get(String url, Map<String, String> params) throws Exception {
        return get(url, params, "UTF-8");
    }

    public static String get(String url, Map<String, String> params, int timeout) throws Exception {
        return get(url, params, "UTF-8", timeout);
    }

    private static String get(String url, Map<String, String> params, String charset) throws Exception {
        return get(url, params, charset, DEFULT_CONNECT_TIMEOUT);
    }

    private static String get(String url, Map<String, String> params, String charset, int timeout) throws Exception {
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            if (url.indexOf("?") > -1) {
                url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            } else {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
        }
        CloseableHttpResponse response = null;
        String value = "";
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        httpGet.setConfig(requestConfig);
        try {
            response = HTTPCLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                value = entity != null ? EntityUtils.toString(entity, charset) : null;
            } else {
                String errorMsg = "Request of: " + url + " Error. HTTP statusCode:" + statusCode + " , msg: " + response.toString();
                throw new Exception(errorMsg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static <T> T post(String url, Map<String, String> params, Class<T> clazz) throws Exception {
        String data = post(url, params);
        return JSON.parseObject(data, clazz);
    }

    public static String post(String url, Map<String, String> params, int timeout) throws Exception {
        return post(url, params, Consts.UTF_8, timeout);
    }

    public static String post(String url, Map<String, String> params) throws Exception {
        return post(url, params, Consts.UTF_8, DEFULT_CONNECT_TIMEOUT);
    }


    private static String post(String url, Map<String, String> params, Charset charset, int timeout) throws Exception {
        HttpEntity httpEntity = null;
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> param : params.entrySet()) {
                nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
            httpEntity = new UrlEncodedFormEntity(nvps, charset);
        }
        return post(url, httpEntity, charset, timeout);
    }

    public static <T, K> T post(String url, K params, Class<T> clazz) throws Exception {
        String data = post(url, new StringEntity((params instanceof String) ? params.toString() : JSON.toJSONString(params), Consts.UTF_8), Consts.UTF_8, DEFULT_CONNECT_TIMEOUT);
        return JSON.parseObject(data, clazz);
    }

    public static String post(String url, HttpEntity httpEntity, Charset charset) throws Exception {
        return post(url, httpEntity, charset, DEFULT_CONNECT_TIMEOUT);
    }

    public static String post(String url, HttpEntity httpEntity, Charset charset, int timeout) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).build();
        httpPost.setConfig(requestConfig);
        if (httpEntity != null) {
            if (httpEntity instanceof StringEntity && !(httpEntity instanceof UrlEncodedFormEntity)) {
                ((StringEntity) httpEntity).setContentType(ContentType.APPLICATION_JSON.getMimeType());
            }
            httpPost.setEntity(httpEntity);
        }
        CloseableHttpResponse response = null;
        String value = "";
        try {
            response = HTTPCLIENT.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                value = entity != null ? EntityUtils.toString(entity, charset) : null;
            } else {
                String errorMsg = "Request of: " + url + " Error. HTTP statusCode:" + statusCode + " , msg: " + response.toString();
                throw new Exception(errorMsg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String post(String url, String body) {
        return post(url, body, DEFULT_CONNECT_TIMEOUT);
    }

    public static String post(String url, String body, int timeOut) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(body, Consts.UTF_8));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity, Consts.UTF_8);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * download file
     *
     * @param url
     * @param fileName
     */
    public static boolean download(String url, String fileName) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(REQUESTCONFIG);
        CloseableHttpResponse response = null;
        File file = null;
        FileOutputStream outputStream = null;
        try {
            response = HTTPCLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                InputStream inputStream = response.getEntity().getContent();

                file = new File(fileName);
                outputStream = new FileOutputStream(file);

                int eof = 0;
                int count = 0;
                int length = 100 * 1024;
                byte[] buf = new byte[length];
                while ((eof = inputStream.read(buf, 0, length)) != -1) {
                    count += eof;
                    outputStream.write(buf, 0, eof);
                }

                logger.debug("Url:{}, FileName:{}, Total:{}", url, fileName, count);

                return true;
            } else {
                logger.error("Request of: " + url + " raised Error: " + response.toString());
                return false;
            }
        } catch (Exception e) {
            logger.error("", e);
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * Close httpclient when vm exit<br>
     * Release resource cached by HttpClient
     */
    public static void close() {
        try {
            HTTPCLIENT.close();
            logger.info("########## HTTPClient closed ##########");
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 解析出url：a=123&b=1212&c=4545
     * 存入map中
     */
    public static Map<String, String> requestParam(String URL) {
        Map<String, String> mapRequest = null;
        String[] arrSplit = null;

        if (URL == null) {
            return mapRequest;
        }
        arrSplit = URL.split("&");
        mapRequest = new HashMap<String, String>();
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("=");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static byte[] sendCaptcha(String url, HttpServletResponse servletResponse)
            throws Exception {
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFULT_CONNECT_TIMEOUT)
                .setConnectTimeout(DEFULT_CONNECT_TIMEOUT)
                .build();
        httpGet.setConfig(requestConfig);
        try {
            response = HTTPCLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toByteArray(entity);
            } else {
                String errorMsg = "Request of: " + url + " Error. HTTP statusCode:" + statusCode + " , msg: "
                        + response.toString();
                throw new Exception(errorMsg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String doGetWithHeaders(String url, Map<String, String> headers, Map<String, String> params)
            throws Exception {
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            if (url.indexOf("?") > -1) {
                url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, "UTF-8"));
            } else {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, "UTF-8"));
            }
        }
        CloseableHttpResponse response = null;
        String value = "";
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            httpGet.addHeader(e.getKey(), e.getValue());
        }

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFULT_CONNECT_TIMEOUT)
                .setConnectTimeout(DEFULT_CONNECT_TIMEOUT)
                .build();
        httpGet.setConfig(requestConfig);
        try {
            response = HTTPCLIENT.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                value = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
            } else {
                String errorMsg = "Request of: " + url + " Error. HTTP statusCode:" + statusCode + " , msg: "
                        + response.toString();
                throw new Exception(errorMsg);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static byte[] postWithByteRsp(String url, String body) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DEFULT_CONNECT_TIMEOUT)
                    .setConnectTimeout(DEFULT_CONNECT_TIMEOUT).build();
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(body, Consts.UTF_8));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toByteArray(entity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
