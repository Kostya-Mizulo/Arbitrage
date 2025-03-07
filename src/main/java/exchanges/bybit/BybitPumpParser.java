package exchanges.bybit;

import io.restassured.response.Response;
import pump.PumpFinder;

import java.util.ArrayList;
import java.util.List;

public class BybitPumpParser {
    public static ArrayList<Double> parsePump(Response response){
        ArrayList<Double> price = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            price.add(Double.parseDouble(response.jsonPath().getString("result.list[" + i + "][4]")));
        }

        return price;
    }
}