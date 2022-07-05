package model;

import model.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;

import java.io.IOException;
import java.net.URISyntaxException;

public class PutRequest extends Request {
    @Override
    public CloseableHttpResponse execute() throws URISyntaxException, IOException {
        HttpPut httpPut = new HttpPut(uriBuilder.build());
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Authorization", String.format("Bearer %s", token));
        httpPut.setEntity(entity);
        httpPut.setHeader("Content-Type", "application/json;charset=utf8");
        return httpClient.execute(httpPut);
    }
}
