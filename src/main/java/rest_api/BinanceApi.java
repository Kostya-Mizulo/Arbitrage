package rest_api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BinanceApi extends RestApi{
    private static final String KLINE_URL = "https://api.binance.com/api/v3/klines";

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
