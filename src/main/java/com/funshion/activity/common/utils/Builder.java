package com.funshion.activity.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Builder {

    private static final Logger logger = LoggerFactory.getLogger(Builder.class);

    private final static CloseableHttpClient HTTPCLIENT;

    private static final int DEFULT_SOCKET_TIMEOUT = 60000;

    private static final int DEFULT_CONNECT_TIMEOUT = 60000;


    private static final int DEFULT_MAX_TOTAL = 300;

    private static final int DEFULT_MAX_PER_ROUTE = 50;

    static {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(DEFULT_MAX_TOTAL);
        manager.setDefaultMaxPerRoute(DEFULT_MAX_PER_ROUTE);
        HTTPCLIENT = HttpClients.custom().setConnectionManager(manager).build();
    }


    private RequestMethod method;

    private int connectTimeout;

    private int socketTimeout;

    private String url;

    private HttpEntity httpEntity;

    private Charset charset = Consts.UTF_8;

    private HttpHost host;

    private Class clazz;

    private Map<String, String> headers;

    private Map<String, String> params;

    public static Builder create() {
        return new Builder();
    }

    public Class getClazz() {
        return clazz;
    }

    public Builder setClazz(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public Builder setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Builder setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public Builder setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Builder setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public Builder setHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public Builder setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public HttpHost getHost() {
        return host;
    }

    public Builder setHost(HttpHost host) {
        this.host = host;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Builder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Builder setHeaders(String key, String value) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Builder setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public <T> T execute() throws Exception {
        RequestConfig.Builder requestConfig = RequestConfig.custom();
        if (getSocketTimeout() != 0) {
            requestConfig.setSocketTimeout(getSocketTimeout());
        } else {
            requestConfig.setSocketTimeout(DEFULT_SOCKET_TIMEOUT);
        }
        if (getConnectTimeout() != 0) {
            requestConfig.setConnectTimeout(getConnectTimeout());
        } else {
            requestConfig.setConnectTimeout(DEFULT_CONNECT_TIMEOUT);
        }
        if (getHost() != null) {
            requestConfig.setProxy(getHost());
        }
        if (getMethod() != null) {
            switch (getMethod()) {
                case GET:
                    return get(getUrl(), requestConfig.build(), getHeaders(), getParams(), getCharset(), getClazz());
                case POST:
                    return post(getUrl(), requestConfig.build(), getHttpEntity(), getCharset(), getClazz());
                default:
                    return get(getUrl(), requestConfig.build(), getHeaders(), getParams(), getCharset(), getClazz());
            }
        } else {
            return get(getUrl(), requestConfig.build(), getHeaders(), getParams(), getCharset(), getClazz());
        }
    }

    private <T> T get(String url, RequestConfig requestConfig, Map<String, String> headers, Map<String, String> params, Charset charset, Class clazz) throws Exception {

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

        httpGet.setConfig(requestConfig);
        try {
            if (!CollectionUtils.isEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
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
                logger.error(e.getMessage(), e);
            }
        }
        if (clazz != null) {
            return (T) JSON.parseObject(value, clazz);
        } else {
            return (T) value;
        }
    }

    private <T> T post(String url, RequestConfig requestConfig, HttpEntity httpEntity, Charset charset, Class clazz) throws Exception {

        HttpPost httpPost = new HttpPost(url);
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
                logger.error(e.getMessage(), e);
            }
        }
        if (clazz != null) {
            return (T) JSON.parseObject(value, clazz);
        } else {
            return (T) value;
        }

    }

}
