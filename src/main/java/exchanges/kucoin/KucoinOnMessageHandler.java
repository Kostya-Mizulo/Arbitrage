package exchanges.kucoin;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.Map;

public class KucoinOnMessageHandler {
    public static Map.Entry handleMessageFromServer(String json) {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("data")) {
            JSONObject dataObject = jsonObject.getJSONObject("data");

            String topic = jsonObject.getString("topic");
            String topicDeletePrefix = topic.replace("/market/ticker:", "");
            //Вырезаем только основное значение монеты----------------
            String upperSymbol = topicDeletePrefix.toUpperCase();
            String[] suffixes = {"USDT", "USDC", "USD"};
            for (String suffix : suffixes){
                if (upperSymbol.endsWith(suffix)){
                    int endIndex = upperSymbol.lastIndexOf(suffix);
                    String cleanedSymbol = upperSymbol.substring(0, endIndex);
                    cleanedSymbol = cleanedSymbol.replaceAll("[-/.]", "");

                    Double lastPrice = dataObject.getDouble("price");
                    Map.Entry<String, Double> pair = new AbstractMap.SimpleEntry<>(cleanedSymbol, lastPrice);
                    return pair;
                }
            }
            //----------------

            Double lastPrice = dataObject.getDouble("price");

            Map.Entry<String, Double> pair = new AbstractMap.SimpleEntry<>(upperSymbol, lastPrice);
            return pair;
        } else {
            System.out.println(json);
            return null;
        }
    }
}
