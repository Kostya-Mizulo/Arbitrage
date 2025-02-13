package exchanges.bingx;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.Map;

public class BingxOnMessageHandler {
    public static Map.Entry handleMessageFromServer(String json) {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("data") && jsonObject.get("data") instanceof JSONObject) {
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONObject kObject = dataObject.getJSONObject("K");



            String symbol = kObject.getString("s");


            //Вырезаем только основное значение монеты----------------
            String upperSymbol = symbol.toUpperCase();
            String[] suffixes = {"USDT", "USDC", "USD"};
            for (String suffix : suffixes) {
                if (upperSymbol.endsWith(suffix)) {
                    int endIndex = upperSymbol.lastIndexOf(suffix);
                    String cleanedSymbol = upperSymbol.substring(0, endIndex);
                    cleanedSymbol = cleanedSymbol.replaceAll("[-/.]", "");

                    Double lastPrice = kObject.getDouble("c");
                    Map.Entry<String, Double> pair = new AbstractMap.SimpleEntry<>(cleanedSymbol, lastPrice);
                    return pair;
                }
            }
            //----------------

            Double lastPrice = kObject.getDouble("c");

            Map.Entry<String, Double> pair = new AbstractMap.SimpleEntry<>(symbol, lastPrice);
            return pair;
        } else {
            System.out.println(json);
            return null;
        }
    }
    public static String createPongString(String ping){

        JSONObject jsonPing = new JSONObject(ping);
        String idPing = jsonPing.getString("ping");

        String pongString = "{\"pong\":\"" + idPing + "\"}";

        return pongString;
    }
}
