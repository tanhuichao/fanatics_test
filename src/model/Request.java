package model;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public abstract class Request {
    RequestConfig requestConfig;
    URIBuilder uriBuilder;
    String token;
    Map<String, String> parameters;
    StringEntity entity;
    CloseableHttpClient httpClient;
    CloseableHttpResponse response;


    public Request() {
        httpClient = HttpClientBuilder.create().build();
    }

    public void setRequestConfig (int connectTimeout, int connectionRequestTimeout, int socketTimeout, boolean redirectsEnabled) {
        requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setRedirectsEnabled(redirectsEnabled).build();
    }

    public void setEntity(StringEntity entity) {
        this.entity = entity;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void buildUri(String baseUri, Map<String, String> queryParams) throws URISyntaxException {
        uriBuilder = new URIBuilder(baseUri);
        if (queryParams != null && queryParams.size() > 0) {
            queryParams.forEach((k, v) -> {
                uriBuilder.setParameter(k, v);
            });
        }
    }

    public abstract CloseableHttpResponse execute() throws URISyntaxException, IOException;
}
