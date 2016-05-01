package se.lantz.thisismycase.service.test;
import se.lantz.thisismycase.model.User;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserWebServiceClientTest {

    private Client client = ClientBuilder.newClient();
    private String basePath = "http://localhost:8080/casebycase/user";
    private String createUser = "http://localhost:8080/casebycase/user";
    private String getUserByUserId = "http://localhost:8080/casebycase/user/b236";
    private String updateUser = "http://localhost:8080/casebycase/user/b236";
    private String deleteUser = "http://localhost:8080/casebycase/user/b236";

    public void getRequest() {
        WebTarget webTarget = client.target(getUserByUserId);

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        User user = response.readEntity(User.class);

        System.out.println(response.getStatus());
        System.out.println(user);
    }

    public void postRequest(){
        WebTarget webTarget = client.target(basePath);

        User user = new User("username", "firstname", "lastname");

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        System.out.println(response.readEntity(User.class));
    }

    public void putRequest(){
        WebTarget webTarget = client.target(updateUser);

        User user = new User("jola", "josefine", "lantz");
        user.setFirstName("Greatest");
        user.setLastName("Ever");

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.put(Entity.entity(user, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        System.out.println(user);
    }

    public void deleteRequest(){
        WebTarget webTarget = client.target(deleteUser);

        Invocation.Builder invocationBuilder =  webTarget.request();
        Response response = invocationBuilder.delete();

        System.out.println(response.getStatus());
        System.out.println(response.readEntity(User.class));
    }

    public static void main(String[] args) {
        UserWebServiceClientTest restClient = new UserWebServiceClientTest();
        restClient.getRequest();
        restClient.postRequest();
        restClient.putRequest();
        restClient.deleteRequest();
    }

}
