package steps;

import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.Node;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import util.RestPath;
import util.RestApiUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.List;

public class LoginAPISteps {

    private String url;
    private String usersUrl;
    private RestApiUtil endPointUtil=new RestApiUtil();
    private Response response;

    public LoginAPISteps()
    {
         this.usersUrl=endPointUtil.getBaseURL()+ RestPath.USERS;
    }

    @Given("Docker service is up and running")
    public void dockerServiceIsUpAndRunning() {
    }

    /**
     * Register user account
     * @param name
     * @param userName
     * @param password
     * @param email
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     * @param statusCode
     */
    @Given("register account with {string} {string} {string} {string} {string} {string} {string} {int}")
    public void theUserRegisteredAnAccountWith(String name, String userName,String password, String email, String dateOfBirth,String superPower,String isAdmin,int statusCode) {
        JSONObject body=userBody(0,name,userName,password,email,dateOfBirth,superPower,isAdmin);
        deleteUserIfPresent(usersUrl,body,userName,password);
        response=endPointUtil.sendRestApiRequest("POST", usersUrl,body.toString());
        endPointUtil.verifyStatusCode(response,statusCode);


    }

    /**
     * Register user account
     * @param name
     * @param userName
     * @param password
     * @param email
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     * @param statusCode
     */
    @When("register user with {string} {string} {string} {string} {string} {string} {string} {int}")
    public void theUserRegisteredAnAccountWithWhen(String name, String userName,String password, String email, String dateOfBirth,String superPower,String isAdmin,int statusCode) {

        JSONObject body=userBody(0,name,userName,password,email,dateOfBirth,superPower,isAdmin);
        response=endPointUtil.sendRestApiRequest("POST", usersUrl,body.toString());
        endPointUtil.verifyStatusCode(response,statusCode);
    }

    /**
     * Update registered user details
     * @param userName2
     * @param userName1
     * @param name
     * @param password
     * @param email
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     * @param statusCode
     */
    @When("{string} updated with {string} {string} {string} {string} {string} {string} {string} {int}")
    public void updateUserDetail(String userName2,String userName1,String name,String password, String email, String dateOfBirth,String superPower,String isAdmin,int statusCode) {
        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName2;
        response=endPointUtil.sendRestApiRequest("GET", url);
        int id= response.jsonPath().get("id");
        JSONObject body=userBody(id,name,userName1,password,email,dateOfBirth,superPower,isAdmin);
        response = endPointUtil.sendRestApiRequest("PUT", usersUrl, body.toString(),userName1,password);
    }

    /**
     * Request for registered user detail
     * @param userName
     * @throws MalformedURLException
     */
    @When("a client app attempts to request user {string} details")
    public void aClientAppAttemptsToRequestUserDetails(String userName) throws MalformedURLException {
        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName;
        response=endPointUtil.sendRestApiRequest("GET", url);
    }

    /**
     * Validate success response
     * @param statusCode
     * @param name
     * @param userName
     * @param email
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     */
    @Then("the success response have {int} {string} {string} {string} {string} {string} {string}")
    public void theSuccessResponseShouldBeAJSONObjectEqualTo(int statusCode,String name, String userName, String email, String dateOfBirth,String superPower,String isAdmin) {
            endPointUtil.verifyStatusCode(response,statusCode);
            validateUserResponse(name,userName,email,dateOfBirth,superPower,isAdmin);
    }

    /**
     * Check Error response code
     * @param statusCode
     * @param error
     * @param message
     * @param path
     */
    @Then("the error response have {int} {string} {string} {string}")
    public void theErrorResponseShouldBeAJSONObjectEqualTo(int statusCode,String error, String message, String path) {
        endPointUtil.verifyStatusCode(response,statusCode);
        endPointUtil.ValidateResponse("error",error, response);
        endPointUtil.ValidateResponse("message",message, response);
        endPointUtil.ValidateResponse("path",path, response);
        endPointUtil.ValidateResponse("status",statusCode, response);
    }

    /**
     * Check if user is already registered
     * @param userName
     */
    @Given("the user is already registered an account with {string}")
    public void theUserIsAlreadyRegisteredAnAccountWith(String userName) {

        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName;
        response=endPointUtil.sendRestApiRequest("GET", url);
        endPointUtil.verifyStatusCode(response,200);

    }


    /**
     * Update user details
     * @param userUrl
     * @param body
     */
    private void updateUser(String userUrl, ResponseBody body)
    {
        response = endPointUtil.sendRestApiRequest("PUT", userUrl, body.toString(),endPointUtil.getPropertyFileValue("adminuser"),endPointUtil.getPropertyFileValue("adminpassword"));
    }

    /**
     * Validate user response
     * @param name
     * @param userName
     * @param email
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     */
    private void validateUserResponse(String name, String userName, String email, String dateOfBirth,String superPower,String isAdmin)
    {
        endPointUtil.ValidateResponse("name",name, response);
        endPointUtil.ValidateResponse("username",userName, response);
        endPointUtil.ValidateResponse("email",email, response);
        endPointUtil.ValidateResponse("dateOfBirth",dateOfBirth, response);
        endPointUtil.ValidateResponse("superpower",superPower, response);
        endPointUtil.ValidateResponse("isAdmin",isAdmin.equals("true"), response);
    }

    /**
     *  Generate user post/put body json
     * @param id
     * @param name
     * @param userName
     * @param password
     * @param email
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     * @return
     */
    private JSONObject userBody(int id, String name, String userName,String password, String email, String dateOfBirth,String superPower,String isAdmin)
    {
        LinkedHashMap<String,Object> value= new LinkedHashMap<>();
        if(id!=0) value.put("id",id);
        value.put("name",name);
        value.put("username",userName);
        value.put("password",password);
        value.put("email",email);
        value.put("superpower",superPower);
        value.put("dateOfBirth",dateOfBirth);
        value.put("isAdmin", Boolean.valueOf(isAdmin));
        return endPointUtil.createJsonObject(value);

    }

    /**
     * Verify status code of response
     * @param statusCode
     */
    @Then("verify {int}")
    public void verifyGetStatusCode(int statusCode) {
        endPointUtil.verifyStatusCode(response,statusCode);
    }

    /**
     * Verify error response code
     * @param statusCode
     * @param message
     */
    @Then("verify {int} and error {string}")
    public void verifyGetStatusCode(int statusCode,String message) {
        endPointUtil.verifyStatusCode(response,statusCode);
        endPointUtil.ValidateResponse("message",message,response);
    }

    /**
     * Requests all active registered details
     * @param username
     * @param password
     */
    @When("a client app attempts to request all user details with {string} {string}")
    public void aClientAppAttemptsToRequestAllUserDetailsWith(String username, String password) {
        url=endPointUtil.getBaseURL()+ RestPath.GET_ALL_USER;
        response=endPointUtil.sendRestApiRequest("GET", url,username,password);
        //Validate size increased on adding of new user
        if(response.getStatusCode()==200) {
            List<Object> list1 = response.jsonPath().getList("userName");
            JSONObject body = userBody(0,"tempUser", "tempUser", "tempUser", "tempuser@somewhere.com", "1986-01-01", "temp", "false");
            response = endPointUtil.sendRestApiRequest("POST", usersUrl, body.toString());
            response = endPointUtil.sendRestApiRequest("GET", url, username, password);
            List<Object> list2 = response.jsonPath().getList("userName");
            deleteUser(usersUrl, body);
            Assert.assertTrue("Failed to fetch all users detail", list1.size() < list2.size());
        }
    }

    /**
     * Delete existing user
     * @param userName
     */
    @When("admin delete the {string}")
    public void adminDeleteTheAccountItIsDeleted(String userName) {
        String usersUrl=endPointUtil.getBaseURL()+ RestPath.USERS;
        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName;
        response=endPointUtil.sendRestApiRequest("GET", url);
        System.out.println(response.prettyPrint());
        deleteUser(usersUrl, response.getBody());
    }

    /**
     * Verify user is deleted
     * @param userName
     */
    @Then("verify {string} is deleted")
    public void verifyUserIsDeleted(String userName) {
        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName;
        response=endPointUtil.sendRestApiRequest("GET", url);
        Assert.assertEquals("Failed to delete user",404,response.getStatusCode());
    }

//    /**
//     * Update user details
//     * @param userName
//     */
//    @When("admin update the {string}")
//    public void updateUser(String userName)
//        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName;
//        response=endPointUtil.sendRestApiRequest("GET", url);
//    }

    /**
     * Update the existing user details
     * @param userName
     * @param password
     * @param name
     * @param email
     * @param newpassword
     * @param dob
     * @param superpower
     * @param isadmin
     */
    @When("update the existing user details {string} {string} {string} {string} {string} {string} {string} {string}")
    public void updateTheExistingUserDetails(String userName,String password,String name, String email, String newpassword, String dob, String superpower, String isadmin) {
        url=endPointUtil.getBaseURL()+ RestPath.GET_USER_DETAIL +userName;
        String usersUrl=endPointUtil.getBaseURL()+ RestPath.USERS;
        response=endPointUtil.sendRestApiRequest("GET", url);
        int id= response.jsonPath().get("id");
        JSONObject body=userBody(id,name,userName,newpassword,email,dob,superpower,isadmin);
        response = endPointUtil.sendRestApiRequest("PUT", usersUrl, body.toString(),userName,password);
        System.out.println(response.getStatusCode());
    }

    /**
     *
     * @param userName
     * @param name
     * @param email
     * @param password
     * @param dateOfBirth
     * @param superPower
     * @param isAdmin
     */
    @Then("Validate the {string} is updated with {string} {string} {string} {string} {string} {string}")
    public void validateTheIsUpdatedWith(String userName, String name, String email, String password, String dateOfBirth, String superPower, String isAdmin) {
        validateUserResponse(name,userName,email,dateOfBirth,superPower,isAdmin);
    }

    /**
     * Access API using user name and password
     * @param userName
     * @param password
     */
    @When("a client app attempts to get access using {string} {string}")
    public void aClientAppAttemptsToGetAccessUsing(String userName, String password) {

        url=endPointUtil.getBaseURL()+ RestPath.GET_ACCESS;
        response=endPointUtil.sendRestApiRequest("GET", url,userName,password);

    }

    /**
     * Delete user if present in system
     * @param deleteUrl
     * @param body
     * @param userName
     * @param password
     */
    private void deleteUserIfPresent(String deleteUrl, JSONObject body , String userName, String password)
    {
        url=endPointUtil.getBaseURL()+RestPath.GET_USER_DETAIL+userName;
        response=endPointUtil.sendRestApiRequest("GET", url);
        if(response.statusCode()==200) {
            response = endPointUtil.sendRestApiRequest("DELETE", deleteUrl, body.toString(),endPointUtil.getPropertyFileValue("adminuser"),endPointUtil.getPropertyFileValue("adminpassword"));
        }
    }

    /**
     * Delete user
     * @param deleteUrl
     * @param body
     */
    private void deleteUser(String deleteUrl, JSONObject body)
    {
        response = endPointUtil.sendRestApiRequest("DELETE", deleteUrl, body.toString(),endPointUtil.getPropertyFileValue("adminuser"),endPointUtil.getPropertyFileValue("adminpassword"));
    }

    /**
     * Delete user
     * @param deleteUrl
     * @param body
     */
    private void deleteUser(String deleteUrl, ResponseBody body)
    {
        response = endPointUtil.sendRestApiRequest("DELETE", deleteUrl, body.asString(),endPointUtil.getPropertyFileValue("adminuser"),endPointUtil.getPropertyFileValue("adminpassword"));
    }
}
