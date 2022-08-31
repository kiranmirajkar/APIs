import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.hasKey;


public class  Oauth2 {

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

    @Test
    public void getToken2() {
        logger.info("getToken2");
        Response response = given()
                .auth().preemptive()
                .basic(client_id, client_secret)
                .formParam("grant_type", "password")
                .formParam("scope", "nqt-scope openid")
                .formParam("username", "kmirajkar@denseair.net")
                .formParam("password", "Monisha2016")
                .log().all()
                .post("https://ids-dev.denseware.net/connect/token");



        // response.prettyPrint();
        System.out.println("Status code is  " + response.statusCode());
        access_token = response.getBody().path("access_token");
    }




    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken")
    public void WebTest() {

        logger.info("WebTest");


        Response WebTest = (Response) RestAssured
                .given().contentType(ContentType.JSON)
                .auth()
                .oauth2(access_token)
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/webtest/234/15/0000000000");


        assertEquals(WebTest.getStatusCode(), 200);
        System.out.println("Status code is  " + WebTest.statusCode());





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

        JsonPath js = new JsonPath(queryParameterVideoTest.asString());

        Assert.assertEquals(js.getString("height"),null );



        assertEquals(queryParameterVideoTest.getStatusCode(), 200);
        System.out.println("Status code is  " + queryParameterVideoTest.statusCode());

    }


    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken")

    public void addTestResults() {

        logger.info("addTestResults");

        Response  addTestResults;
        addTestResults = (Response) RestAssured
                .given()
                .auth()
                .oauth2(access_token)
                .contentType(ContentType.JSON)
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/ResultsV2/5");


            //   addTestResults.prettyPrint();




        assertEquals(addTestResults.getStatusCode(), 200);
        System.out.println("Status code is  " + addTestResults.statusCode());
    }




    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken")

    public void queryTestResults() {
        logger.info("queryTestResults");

        Response  queryTestResults= (Response) RestAssured
                .given()
                .auth()
                .oauth2(access_token)
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/ResultsV2/1");




      JsonPath j = new JsonPath(queryTestResults.asString());


        String p = j.getString("appStartedAt");
        queryTestResults.prettyPrint();
        Assert.assertTrue(p.equalsIgnoreCase("2022-03-29T03:25:27.439212"));






    assertEquals(queryTestResults.getStatusCode(), 200);
        System.out.println("Status code is  " + queryTestResults.statusCode());
    }


//    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken2")
//
//    public void particularNetwork() {
//
//        logger.info("particularNetwork");
//
//        Response  particularNetwork= (Response) RestAssured
//                .given()
//                .auth()
//                .oauth2(access_token)
//                .log().all()
//                .get("https://nqt-v2-api.denseware.net/nqt_v2/resultsv2/boundary/234/20?latitude=51.5737338&longitude=-0.7603");
//
//
//
//        assertEquals(particularNetwork.getStatusCode(), 200);
//        System.out.println("Status code is  " + particularNetwork.statusCode());
//    }









    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken")

    public void returnWKTObject() {

        logger.info("returnWKTObject");

        Response  returnWKTObject= (Response) RestAssured
                .given()
                .auth()
                .oauth2(access_token)
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/resultsv2/boundary/b6f03d25b814a779?latitude=51.5737338&longitude=-0.7603");


        assertEquals(returnWKTObject.getStatusCode(), 200);
        System.out.println("Status code is  " + returnWKTObject.statusCode());
    }


    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken")

    public void QueryComparisonScore() {

        logger.info("QueryComparisonScore");

        Response  QueryComparisonScore= (Response) RestAssured
                .given()
                .auth()
                .oauth2(access_token)
                .when().accept("application/json")
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/getComparisonScores?latitude=51.5737338&longitude=-0.7603");




        assertEquals(QueryComparisonScore.getStatusCode(), 200);
        System.out.println("Status code is  " + QueryComparisonScore.statusCode());
    }
}




