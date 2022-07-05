package test;
import com.google.gson.Gson;
import model.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BufferedHeader;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import service.UserRequestService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UnitTest {
    @InjectMocks
    UserRequestService service = new UserRequestService();
    @Mock RequestFactory mockFactory;
    @Mock Request mockRequest;
    @Mock CloseableHttpResponse mockResponse;
    @Mock
    CloseableHttpClient mockClient;
    @Mock HttpEntity mockEntity;
    @Mock
    StatusLine mockStatusLine;
    @Mock
    BufferedHeader mockHeader;

    @Before
    public void before() throws URISyntaxException, IOException {
        Mockito.when(mockRequest.execute()).thenReturn(mockResponse);
        Mockito.when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(200);
        Mockito.when(mockResponse.getEntity()).thenReturn(mockEntity);
        Mockito.when(mockRequest.getHttpClient()).thenReturn(mockClient);
        Mockito.lenient().doNothing().when(mockClient).close();
    }

    public void prepareForPageTest() throws IOException {
        Mockito.when(mockFactory.getRequest(RequestType.GET)).thenReturn(mockRequest);
        Header[] headers = new Header[1];
        headers[0] = mockHeader;
        Mockito.when(mockResponse.getAllHeaders()).thenReturn(headers);
        Mockito.when(mockHeader.getName()).thenReturn("X-Pagination-Pages");
        Mockito.when(mockHeader.getValue()).thenReturn("210");
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User(98652, "TestUser1", "Test1@test.com", Gender.MALE, Status.ACTIVE);
        User user2 = new User(65231, "TestUser2", "Test2@test.com", Gender.MALE, Status.INACTIVE);
        User user3 = new User(25896, "TestUser3", "Test3@test.com", Gender.FEMALE, Status.ACTIVE);
        User user4 = new User(75632, "TestUser4", "Test4@test.com", Gender.MALE, Status.INACTIVE);
        User user5 = new User(30219, "TestUser5", "Test5@test.com", Gender.FEMALE, Status.ACTIVE);
        mockUsers.add(user1);
        mockUsers.add(user2);
        mockUsers.add(user3);
        mockUsers.add(user4);
        mockUsers.add(user5);
        String jsonString = new Gson().toJson(mockUsers);
        Mockito.when(mockEntity.getContent()).thenReturn(IOUtils.toInputStream(jsonString, Charset.defaultCharset()));
    }

    @Test
    public void testGetUsersByPage() {
        try {
            prepareForPageTest();
            int pageNum = 3;
            List<User> results = service.getUsersByPage(pageNum);
            assertEquals(results.size(), 5);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testGetUsersOnNonExistPage() {
        try {
            prepareForPageTest();
            int pageNum = 10000000;
            String jsonString = "";
            Mockito.when(mockEntity.getContent()).thenReturn(IOUtils.toInputStream(jsonString, Charset.defaultCharset()));
            List<User> results = service.getUsersByPage(pageNum);
            assertEquals(results,null);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testSortUsers() {
        List<User> users = new ArrayList<>();
        User user3 = new User(98652, "Bill Clinton", "bill.c@test.com", Gender.MALE, Status.ACTIVE);
        User user2 = new User(65231, "George W. Bush", "george.b@test.com", Gender.MALE, Status.ACTIVE);
        User user1 = new User(25896, "Ronald Reagan", "ronald.r@test.com", Gender.MALE, Status.ACTIVE);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        User lastUser = service.sortUserByName(users);
        assertEquals(users.get(0).getName(), "Bill Clinton");
        assertEquals(users.get(1).getName(), "George W. Bush");
        assertEquals(users.get(2).getName(), "Ronald Reagan");
        assertEquals(lastUser.getName(), "Ronald Reagan");
    }

    @Test
    public void testSortInvalidUsers() {
        List<User> users = null;
        User lastUser = service.sortUserByName(users);
        assertEquals(lastUser, null);
    }

    @Test
    public void testUpdateUser() {
        Mockito.when(mockFactory.getRequest(RequestType.PUT)).thenReturn(mockRequest);
        User user = new User(77777, "Jimmy Carter", "jimmy.c@test.com", Gender.MALE, Status.ACTIVE);
        boolean success = service.updateUserName(user);
        assertEquals(success, true);
    }

    @Test
    public void testUpdateUserFailed() {
        Mockito.when(mockFactory.getRequest(RequestType.PUT)).thenReturn(mockRequest);
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(404);
        String jsonString = "User not found";
        User user = new User(77777, "Jimmy Carter", "jimmy.c@test.com", Gender.MALE, Status.ACTIVE);
        boolean success = service.updateUserName(user);
        assertEquals(success, false);
    }

    @Test
    public void testDeleteUser() {
        Mockito.when(mockFactory.getRequest(RequestType.DELETE)).thenReturn(mockRequest);
        User user = new User(23659, "Gerald Rudolph Ford", "gerald.f@test.com", Gender.MALE, Status.ACTIVE);
        boolean success = service.deleteUser(user.getId());
        assertEquals(success, true);
    }

    @Test
    public void testDeleteUserFailed() {
        Mockito.when(mockFactory.getRequest(RequestType.DELETE)).thenReturn(mockRequest);
        String jsonString = "User not found";
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(404);
        User user = new User(23659, "Gerald Rudolph Ford", "gerald.f@test.com", Gender.MALE, Status.ACTIVE);
        boolean success = service.deleteUser(user.getId());
        assertEquals(success, false);
    }
}
