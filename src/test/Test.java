package test;

import model.RequestFactory;
import model.User;
import service.UserRequestService;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        final String token = "2c75cde9aba3327376bbd45c8fb0bb453cc9673e608f3dc10fab649b4117a8cb";
        RequestFactory factory = new RequestFactory();
        UserRequestService service = new UserRequestService(token, factory);
        List<User> users = service.getUsersByPage(3);

        service = new UserRequestService(token, factory);
        User lastUser = service.sortUserByName(users);
        System.out.println("Last user name is " + lastUser.getName());

        service = new UserRequestService(token, factory);
        lastUser.setName(lastUser.getName() + "1");
        lastUser.setId(366666666);
        service.updateUserName(lastUser);
/*
        service = new UserRequestService(token, factory);
        lastUser = service.getUserById(lastUser.getId());
        System.out.println("Last user name after changed is " + lastUser.getName());

        service = new UserRequestService(token, factory);
        service.deleteUser(lastUser.getId());

        service.getUserById(lastUser.getId());

 */
    }
}
