package exchanges.binance;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Collections;

public class BinancePumpParser {
    public static ArrayList<Double> parsePump(Response response){
        ArrayList<Double> price = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            price.add(i, Double.parseDouble(response.jsonPath().getString("[" + i + "][4]")));
        }
        Collections.reverse(price);

        return price;
    }
}