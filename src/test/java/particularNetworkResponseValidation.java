import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.reflect.Array.get;
import static org.junit.Assert.assertEquals;

public class particularNetworkResponseValidation {

    static Logger logger = Logger.getLogger(Oauth2.class);
    @BeforeTest
    public static void init(){
        PropertyConfigurator.configure("log4jproperties.properties");
        logger.info("performing testing API");
    }
    static String client_id = "NQT";
    static String client_secret = "8C1A0C62-7F45-4AE4-B8A6-522596712F21";
    static String access_token;


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

    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken2")

    public void particularNetwork() {

        logger.info("particularNetwork");

        Response  particularNetwork= (Response) RestAssured
                .given().contentType(JSON)
                .auth()
                .oauth2(access_token)
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/resultsv2/boundary/234/20?latitude=51.5737338&longitude=-0.7603")
          .then().assertThat()
         .body(matchesJsonSchemaInClasspath("networkSchema.json"));







        assertEquals(particularNetwork.getStatusCode(), 200);
        System.out.println("Status code is  " + particularNetwork.statusCode());


        //get a field value from nested JSON
//        JsonPath j = new JsonPath(particularNetwork.asString());
//
//        //get a field value from nested JSON
//        String p = j.getString("appStartedAt");
//        System.out.println("*******appStartedAt******** " + p);
//
//        String t = j.getString("testStartedAt");
//        System.out.println("******TestStartedAt********" + t);
//
//        // particularNetwork.prettyPrint();
//
//
//


    }

    @org.testng.annotations.Test(priority = 2, dependsOnMethods = "getToken2")

    public void particularNetworkWKT() {

        logger.info("particularNetworkWKT");

        Response  particularNetworkWKT= (Response) RestAssured
                .given()
                .auth()
                .oauth2(access_token)
                .log().all()
                .get("https://nqt-v2-api.denseware.net/nqt_v2/resultsv2/boundary/wkt/234/20?latitude=51.5737338&longitude=-0.7603");



        assertEquals(particularNetworkWKT.getStatusCode(), 200);
        System.out.println("Status code is  " + particularNetworkWKT.statusCode());
    }


}
