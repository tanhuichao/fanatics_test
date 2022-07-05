package model;

import model.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URISyntaxException;

public class GetRequest extends Request {

    public GetRequest() {
    }

    @Override
    public CloseableHttpResponse execute() throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Authorization", String.format("Bearer %s", token));
        httpGet.setHeader("Content-Type", "application/json;charset=utf8");
        return httpClient.execute(httpGet);
    }
}
