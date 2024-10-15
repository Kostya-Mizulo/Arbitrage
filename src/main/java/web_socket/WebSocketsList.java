package web_socket;

import exchanges.Exchanges;
import exchanges.binance.BinanceSubscriptionString;
import exchanges.bybit.BybitSubscriptionString;

import java.util.ArrayList;

public class WebSocketsList {

    private ArrayList<WebSocketClient> webSockets = new ArrayList<>();


    public void startWebSockets() {
        startBybitWebSockets();
        startBinanceWebSocket();
    }

    private void startBybitWebSockets(){
        ArrayList<String> bybitSubscriptionString = BybitSubscriptionString.getSubscription();
        String uri = "wss://stream.bybit.com/v5/public/spot";

        for (int i = 0; i < bybitSubscriptionString.size(); i++){
            WebSocketClient webSocket = new WebSocketClient(uri, Exchanges.BYBIT);

            webSocket.sendMessage(bybitSubscriptionString.get(i));
            webSockets.add(webSocket);
        }
    }

    private void startBinanceWebSocket() {
        ArrayList<String> binanceSubscriptionString = BinanceSubscriptionString.getSubscription();
        String uri = "wss://stream.binance.com:9443/stream?streams=";

        for (int i = 0; i < binanceSubscriptionString.size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(uri).append(binanceSubscriptionString.get(i));
            String fullUri = sb.toString();
            WebSocketClient webSocket = new WebSocketClient(fullUri, Exchanges.BINANCE);

            webSockets.add(webSocket);
        }
    }



    public ArrayList<WebSocketClient> getWebSockets() {
        return webSockets;
    }



}
