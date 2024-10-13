package web_socket;

import binance.BinanceSubscriptionString;
import bybit.BybitSubscriptionString;

import java.util.ArrayList;

public class WebSocketsList {

    private ArrayList<WebSocketClient> webSockets = new ArrayList<>();


    public void startWebSockets() {
        //startBybitWebSockets();
        startBinanceWebSocket();
    }

    private void startBybitWebSockets(){
        ArrayList<String> bybitSubscriptionString = BybitSubscriptionString.getSubscription();
        String uri = "wss://stream.bybit.com/v5/public/spot";

        for (int i = 0; i < bybitSubscriptionString.size(); i++){
            WebSocketClient webSocket = new WebSocketClient(uri, "Bybit");

            webSocket.sendMessage(bybitSubscriptionString.get(i));
            webSockets.add(webSocket);
        }
    }

    private void startBinanceWebSocket() {
        ArrayList<String> binanceSubscriptionString = BinanceSubscriptionString.getSubscription();
        String uri = "wss://ws-api.binance.com:443/ws-api/v3";

        for (int i = 0; i < binanceSubscriptionString.size(); i++){
            WebSocketClient webSocket = new WebSocketClient(uri, "Binance");

            webSocket.sendMessage(binanceSubscriptionString.get(i));
            webSockets.add(webSocket);
        }
    }



    public ArrayList<WebSocketClient> getWebSockets() {
        return webSockets;
    }



}
