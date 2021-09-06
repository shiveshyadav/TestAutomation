package util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import manager.PropertyFileManager;
import org.json.JSONObject;
import org.junit.Assert;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class RestApiUtil {

    public RestApiUtil() {}
    private Response response = null;

    /**
     * Send Request util
     * @param headers
     * @param requestType
     * @param url
     * @param body
     * @return
     */
    public Response sendRestApiRequest(HashMap<String,String> headers, String requestType, String url, String body) {
        RequestSpecification request = RestAssured.given();
        if(null!=headers) {
            for (String key : headers.keySet())
                request.header(key, headers.get(key));
        }
        if (null != body) {
            request.body(body);
        }

        switch (requestType) {
            case "GET":
                if (null == request) {
                    response = RestAssured.when().get(url);
                } else {
                    response = request.get(url);
                }
                break;
            case "POST":
                if (null == request) {
                    response = RestAssured.when().post(url);
                } else {
                    response = request.post(url);
                }
                break;
            case "DELETE":
                if (null == request) {
                    response = RestAssured.when().delete(url);
                } else {
                    response = request.delete(url);
                }
                break;
            case "PUT":
                if (null == request) {
                    response = RestAssured.when().put(url);
                } else {
                    response = request.put(url);
                }
                break;
        }
        return response;
    }

    /**
     * Verify Status code
     * @param response
     * @param expectedCode
     */
    public void verifyStatusCode(Response response, int expectedCode) {
        Assert.assertEquals("Failed to match status code",expectedCode,response.getStatusCode());
    }


    /**
     * Rest API request
     * @param requestType
     * @param url
     * @param body
     * @param userName
     * @param password
     * @return
     */
    public Response sendRestApiRequest(String requestType, String url,String body, String userName, String password) {
        HashMap<String,String> header= new HashMap<>();
        String encoding = Base64.getEncoder().encodeToString((userName+":"+password).getBytes(StandardCharsets.UTF_8));
        header.put("Content-Type","application/json");
        if(!userName.isEmpty())
        header.put("Authorization","Basic " +encoding);
        response=this.sendRestApiRequest(header,requestType, url ,body);
        return response;
    }

    /**
     * REST api request with only URL
     * @param requestType
     * @param url
     * @return
     */
    public Response sendRestApiRequest(String requestType, String url) {
        sendRestApiRequest( requestType, url,"","","");
        return response;
    }

    /**
     * REST API request with URL , UserName, Password
     * @param requestType
     * @param url
     * @param userName
     * @param password
     * @return
     */
    public Response sendRestApiRequest(String requestType, String url,String userName, String password) {
        sendRestApiRequest( requestType, url,"",userName ,password);
        return response;
    }

    /**
     * REST API requst with URL, Request Body
     * @param requestType
     * @param url
     * @param body
     * @return
     */
    public Response sendRestApiRequest(String requestType, String url,String body) {
        sendRestApiRequest( requestType, url,body,"","");
        return response;
    }

    /**
     * Get Base URL from property file
     * @return
     */
    public String getBaseURL()
    {
        return PropertyFileManager.getInstance().getPropertyFileReader().getValueWithKey("url");
    }

    /**
     * Get property value using key
     * @param key
     * @return
     */
    public String getPropertyFileValue(String key)
    {
        return PropertyFileManager.getInstance().getPropertyFileReader().getValueWithKey(key);
    }

    /**
     * Validate response with key and sting value
     * @param key
     * @param value
     * @param response
     */
    public void ValidateResponse(String key,String value,Response response)
    {
        Assert.assertEquals("Fail to validate "+key+" in response",value,response.jsonPath().get(key));
    }

    /**
     * Validate response with key and int value
     * @param key
     * @param value
     * @param response
     */
    public void ValidateResponse(String key,Integer value,Response response)
    {
        Assert.assertEquals("Fail to validate "+key+" in response",value,response.jsonPath().get(key));
    }

    /**
     * Validate respsonse with key and boolean value
     * @param key
     * @param value
     * @param response
     */
    public void ValidateResponse(String key,boolean value,Response response)
    {
        Assert.assertEquals("Fail to validate "+key+" in response",value,response.jsonPath().get(key));
    }

    /**
     * Generate Json object
     * @param map
     * @return
     */
    public JSONObject createJsonObject(LinkedHashMap<String,Object> map)
    {
        JSONObject values = new JSONObject();
        for(String key: map.keySet())
        {
          values.put(key,map.get(key));
        }

        return values;
    }
}
