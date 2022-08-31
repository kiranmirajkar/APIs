import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class VideoTestSchemaValidation {


    static Logger logger = Logger.getLogger(Oauth2.class);
    @BeforeTest
    public static void init(){
        PropertyConfigurator.configure("log4jproperties.properties");
        logger.info("performing testing API");
    }
    static String client_id = "NQT";
    static String client_secret = "8C1A0C62-7F45-4AE4-B8A6-522596712F21";
    static String access_token;
    static String access_token2;


    @Test(priority = 1)

    public void getToken() {
        logger.info("getToken");
        Response response = given()
                .auth().preemptive()
                .basic(client_id, client_secret)
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "nqt-scope")
                .log().all()
                .post("https://ids-dev.denseware.net/connect/token");


        // response.prettyPrint();
        System.out.println("Status code is  " + response.statusCode());
        access_token = response.getBody().path("access_token");
    }

    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken")

    public void VideoTest() {

        logger.info("queryParameterVideoTest");

        Response  queryParameterVideoTest= (Response) RestAssured
                .given()

                .auth()
                .oauth2(access_token)
                .log().all()

                .get("https://nqt-v2-api.denseware.net/nqt_v2/videotest/234/15/0000000000/6");
                     queryParameterVideoTest.prettyPrint();


                //.then().assertThat()
               // .body(matchesJsonSchemaInClasspath("videoSchema.json"));


        assertEquals(queryParameterVideoTest.getStatusCode(), 200);
        System.out.println("Status code is  " + queryParameterVideoTest.statusCode());




//        ResponseBody body = queryParameterVideoTest.body();
//        String bodyobj = body.asString();
//        System.out.println("my response is : " + bodyobj);
//        org.testng.Assert.assertEquals(bodyobj.contains("url"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("bitrate"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("approxDurationMs"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("audioSampleRate"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("contentLength"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("fps"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("height"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("width"),true);
//        org.testng.Assert.assertEquals(bodyobj.contains("qualityLabel"),true);
//
//        JsonPath js = queryParameterVideoTest.jsonPath();
//        String value1 = js.getString("details.qualityLabel");
//        org.testng.Assert.assertEquals(value1.equals("480p"),true);
//        String value2 = js.getString("details.url");
//        org.testng.Assert.assertEquals(value2.equals("https://nqt-test.denseware.net/videos/2Qnhb0wN9XM_480p.txt"),true);
//


//          //convert JSON to string
//        JsonPath j = new JsonPath(queryParameterVideoTest.asString());
//
//        //get values of JSON array after getting array size
//        int s = j.getInt("details.size()");
//        for(int i = 0; i < s; i++) {
//            String url = j.getString("details["+i+"].url");
//            String bitrate = j.getString("details["+i+"].bitrate");
//            String approxDurationMs = j.getString("details["+i+"].approxDurationMs");
//            System.out.println(url);
//            System.out.println(bitrate);
//            System.out.println(approxDurationMs);
//        }
//
//
//
//
//
//
//
//       JsonPath js = new JsonPath(queryParameterVideoTest.asString());
//
//      Assert.assertEquals(js.getString("height"),null );




    }



}



