package rest_api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BingxApi extends RestApi {

    private static final String KLINE_URL = "https://open-api.bingx.com/openApi/spot/v2/market/kline";

    public Response getKlinesForSymbol(String symbol){
        Response response = given().
                spec(requestSpecification())
                .and()
                .queryParam("symbol", symbol)
                .queryParam("interval", "1m")
                .queryParam("limit", 20)
                .when()
                .get(KLINE_URL);

        return response;
    }
}
