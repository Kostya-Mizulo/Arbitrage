package exchanges.bingx;

import io.restassured.response.Response;

import java.util.ArrayList;

public class BingxPumpParser {
    public static ArrayList<Double> parsePump(Response response){
        ArrayList<Double> price = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            price.add(response.jsonPath().getDouble("data[" + i + "][4]"));
        }

        return price;
    }
}
