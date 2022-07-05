package model;

import model.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;

import java.io.IOException;
import java.net.URISyntaxException;

public class DeleteRequest extends Request {
    @Override
    public CloseableHttpResponse execute() throws URISyntaxException, IOException {
        HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("Authorization", String.format("Bearer %s", token));
        httpDelete.setHeader("Content-Type", "application/json;charset=utf8");
        return httpClient.execute(httpDelete);
    }
}
