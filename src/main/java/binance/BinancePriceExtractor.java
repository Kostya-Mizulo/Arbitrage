package binance;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.Map;

public class BinancePriceExtractor {
    public static Map.Entry extractPriceFromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("data")) {
            JSONObject dataObject = jsonObject.getJSONObject("data");

            String symbol = dataObject.getString("s");

            //Вырезаем только основное значение монеты----------------
            String upperSymbol = symbol.toUpperCase();
            String[] suffixes = {"USDT", "USDC", "USD"};
            for (String suffix : suffixes){
                if (upperSymbol.endsWith(suffix)){
                    int endIndex = upperSymbol.lastIndexOf(suffix);
                    String cleanedSymbol = upperSymbol.substring(0, endIndex);
                    cleanedSymbol = cleanedSymbol.replaceAll("[-/.]", "");

                    Double lastPrice = dataObject.getDouble("p");
                    Map.Entry<String, Double> pair = new AbstractMap.SimpleEntry<>(cleanedSymbol, lastPrice);
                    return pair;
                }
            }
            //----------------

            Double lastPrice = dataObject.getDouble("lastPrice");

            Map.Entry<String, Double> pair = new AbstractMap.SimpleEntry<>(symbol, lastPrice);
            return pair;
        } else return null;
    }
}
