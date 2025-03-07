package rest_api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestApi {
    private static final String MEXC_BASE_URI = "https://contract.mexc.com/api";
    private static final String BYBIT_BASE_URI = "https://api.bybit.com";
    private static final String BINANCE_BASE_URI = "";
    private static final String BINGX_BASE_URI = "";




    protected RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }
}