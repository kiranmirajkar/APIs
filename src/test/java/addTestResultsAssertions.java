import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class addTestResultsAssertions {

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


        //addTestResults.prettyPrint();

        JsonPath js = addTestResults.jsonPath();
        String value1 = js.getString("details.floor");
        org.testng.Assert.assertEquals(value1.equals("level 3"),true);
        //String value2 = js.getString("details.url");
     //  org.testng.Assert.assertEquals(value2.equals("https://nqt-test.denseware.net/videos/2Qnhb0wN9XM_480p.txt"),true);




        assertEquals(addTestResults.getStatusCode(), 200);
        System.out.println("Status code is  " + addTestResults.statusCode());
    }

}
