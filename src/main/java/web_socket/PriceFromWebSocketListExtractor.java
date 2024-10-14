package web_socket;

import coins.Coin;
import coins.CoinsList;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PriceFromWebSocketListExtractor {

    public static void extractPriceFromWebSocketList(WebSocketsList webSocketsList){
        for (WebSocketClient webSocket : webSocketsList.getWebSockets()) {
            for (Map.Entry<String, Double> priceMap : webSocket.getCoinsFromWebSocket().entrySet()){
                Coin coin = CoinsList.coinsMap.get(priceMap.getKey());
                switch (webSocket.getExchange()){
                    case "Bybit": {
                        coin.setBybitPrice(priceMap.getValue());
                        break;
                    }
                    case "Binance": {
                        coin.setBinancePrice(priceMap.getValue());
                        break;
                    }
                }
            }
        }
    }
}
