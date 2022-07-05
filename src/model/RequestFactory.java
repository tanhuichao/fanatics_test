package model;

import model.*;

public class RequestFactory {
    public Request getRequest(RequestType type) {
        if (type == RequestType.GET){
            return new GetRequest();
        }
        else if (type == RequestType.POST) {
            return new PostRequest();
        }
        else if (type == RequestType.PUT) {
            return new PutRequest();
        }
        else if (type == RequestType.DELETE) {
            return new DeleteRequest();
        }
        return null;
    }
}
