package rest_api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BybitApi extends RestApi {
    private static final String KLINE_URL = "https://api.bybit.com/v5/market/kline";

    public Response getKlinesForSymbol(String symbol){
        Response response = given().
                spec(requestSpecification())
                .and()
                .queryParam("symbol", symbol)
                .queryParam("interval", "1")
                .queryParam("limit", "20")
                .queryParam("category", "spot")
                .when()
                .get(KLINE_URL);

        return response;
    }
}