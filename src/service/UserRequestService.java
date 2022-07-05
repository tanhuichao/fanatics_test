package service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Request;
import model.RequestFactory;
import model.RequestType;
import model.User;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
public class UserRequestService {
    private Logger logger = LogManager.getLogger(UserRequestService.class);
    private String baseUri = "https://gorest.co.in/public/v2/users";
    //private final String token = "2c75cde9aba3327376bbd45c8fb0bb453cc9673e608f3dc10fab649b4117a8cb";
    private String token;
    private Map<String, String> parameters = new HashMap<>();
    private Gson gson = new Gson();
    private RequestFactory requestFactory;

    public UserRequestService () {
    }
    public UserRequestService (String token, RequestFactory requestFactory) {
        this.token = token;
        this.requestFactory = requestFactory;
    }
    /*
       1. Retrieve page 3 of the list of all users
     */
    public List<User> getUsersByPage(int page) {
        Request request = requestFactory.getRequest(RequestType.GET);
        parameters.put("page", String.valueOf(page));
        CloseableHttpResponse response = null;
        List<User> users = null;
        try {
            request.setRequestConfig(3000, 3000, 3000, true);
            request.setToken(token);
            request.buildUri(baseUri, parameters);
            response = request.execute();
            if (response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    if (header.getName().equalsIgnoreCase("X-Pagination-Pages")) {
                        // 2. Using a logger, log the total number of pages from the previous request.
                        logger.info("Total page is " + header.getValue());
                        break;
                    }
                }
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String result = EntityUtils.toString(responseEntity, "UTF-8");
                    users = gson.fromJson(result, new TypeToken<ArrayList<User>>() {
                    }.getType());
                }
            }
            else {
                HttpEntity reponseEntity = response.getEntity();
                String responseString = EntityUtils.toString(reponseEntity, "UTF-8");
                logger.error(responseString);
            }
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (ParseException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        finally {
            try {
                if (request.getHttpClient() != null) {
                    request.getHttpClient().close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return users;
    }

    /*
    3. Sort the retrieved user list by name
    4. After sorting, log the name of the last user.
     */
    public User sortUserByName(List<User> users) {
        User lastUser = null;
        if (users == null || users.size() < 1) {
            return lastUser;
        }
        users.sort(Comparator.comparing(User::getName));
        int size = users.size();
        lastUser = users.get(size - 1);
        logger.info("Last user name is " + lastUser.getName());
        return lastUser;
    }

    /*
    5. Update that user's name to a new value and use the correct http method to save it.
    */
    public boolean updateUserName(User user) {
        Request request = requestFactory.getRequest(RequestType.PUT);
        CloseableHttpResponse response = null;
        try {
            request.setRequestConfig(3000, 3000, 3000, true);
            request.setToken(token);
            request.buildUri(baseUri + "/" + user.getId(), parameters);
            String jsonString = gson.toJson(user);
            StringEntity entity = new StringEntity(jsonString, "UTF-8");
            request.setEntity(entity);
            response = request.execute();
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            else {
                HttpEntity reponseEntity = response.getEntity();
                String responseString = EntityUtils.toString(reponseEntity, "UTF-8");
                logger.error(responseString);
            }
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (ParseException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        finally {
            try {
                if (request.getHttpClient() != null) {
                    request.getHttpClient().close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return false;
    }

    /*
    6. Delete that user
     */
    public boolean deleteUser(int id) {
        Request request = requestFactory.getRequest(RequestType.DELETE);
        CloseableHttpResponse response = null;
        try {
            request.setRequestConfig(3000, 3000, 3000, true);
            request.setToken(token);
            request.buildUri(baseUri + "/" + id, parameters);
            response = request.execute();
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Delete user with id " + id);
                return true;
            }
            else {
                HttpEntity reponseEntity = response.getEntity();
                String responseString = EntityUtils.toString(reponseEntity, "UTF-8");
                logger.error(responseString);
            }
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (ParseException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        finally {
            try {
                if (request.getHttpClient() != null) {
                    request.getHttpClient().close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return false;
    }

    /*
    7. Attempt to retrieve a nonexistent user with ID 5555. Log the resulting http response code.
     */
    public User getUserById(int id) {
        Request request = requestFactory.getRequest(RequestType.GET);;
        CloseableHttpResponse response = null;
        User user = null;
        try {
            request.setRequestConfig(3000, 3000, 3000, true);
            request.setToken(token);
            request.buildUri(baseUri + "/" + id, parameters);
            response = request.execute();
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("retrieve an user with ID " + id + " get status " + statusCode);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String result = EntityUtils.toString(responseEntity);
                    user = new Gson().fromJson(result, User.class);
                }
            }
            else {
                HttpEntity reponseEntity = response.getEntity();
                String responseString = EntityUtils.toString(reponseEntity, "UTF-8");
                logger.error(responseString);
            }
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (ParseException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        finally {
            try {
                if (request.getHttpClient() != null) {
                    request.getHttpClient().close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return user;
    }
}
