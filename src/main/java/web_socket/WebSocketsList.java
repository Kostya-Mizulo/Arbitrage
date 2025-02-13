package web_socket;

import exchanges.Exchanges;
import exchanges.binance.BinanceSubscriptionString;
import exchanges.bingx.BingxSubscriptionString;
import exchanges.bybit.BybitSubscriptionString;
import exchanges.kucoin.KucoinSubscriptionString;
import http_classes.kucoin.ApiForWebsocketConn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebSocketsList {

    private ArrayList<WebSocketClient> webSockets = new ArrayList<>();


    public void startWebSockets() {
        startBybitWebSockets(null);
        //startBinanceWebSocket(null);
        //startBingxWebSocket(null);
        startKucoinWebSockets(null);
    }

    private void startBybitWebSockets(ArrayList<String> coinsToOpen){
        ArrayList<String> bybitSubscriptionString = null;
        if (coinsToOpen == null){
            bybitSubscriptionString = BybitSubscriptionString.getSubscription();
        }
        if (coinsToOpen != null) {
            bybitSubscriptionString = BybitSubscriptionString.getSubscriptionToReopen(coinsToOpen);
        }



        String uri = "wss://stream.bybit.com/v5/public/spot";

        int whichSubscriptionStringNow = 0;
        int countOfSubscriptionStrings = bybitSubscriptionString.size();
        boolean isCoinsLeft = true;

        while (isCoinsLeft) {
            WebSocketClient webSocket = new WebSocketClient(uri, Exchanges.BYBIT, this);
            webSockets.add(webSocket);

            for (int i = 0; i < 10; i++){
                System.out.println("Отправил подписку: " + bybitSubscriptionString.get(whichSubscriptionStringNow));
                webSocket.sendMessage(bybitSubscriptionString.get(whichSubscriptionStringNow));
                whichSubscriptionStringNow++;
                if (whichSubscriptionStringNow == countOfSubscriptionStrings) {
                    isCoinsLeft = false;
                    break;
                }
            }
        }
    }

    private void startBinanceWebSocket(ArrayList<String> coinsToOpen) {

        ArrayList<String> binanceSubscriptionString = null;
        if (coinsToOpen == null){
            binanceSubscriptionString = BinanceSubscriptionString.getSubscription();
        }
        if (coinsToOpen != null) {
            binanceSubscriptionString = BinanceSubscriptionString.getSubscriptionToReopen(coinsToOpen);
        }


        String uri = "wss://stream.binance.com:9443/stream?streams=";

        for (int i = 0; i < binanceSubscriptionString.size(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append(uri).append(binanceSubscriptionString.get(i));
            String fullUri = sb.toString();
            WebSocketClient webSocket = new WebSocketClient(fullUri, Exchanges.BINANCE, this);

            webSockets.add(webSocket);
        }
    }

    private void startBingxWebSocket(ArrayList<String> coinsToOpen){
        ArrayList<String> bingxSubscriptionString = null;
        if (coinsToOpen == null){
            bingxSubscriptionString = BingxSubscriptionString.getSubscription();
        }
        if (coinsToOpen != null) {
            bingxSubscriptionString = BingxSubscriptionString.getSubscriptionToReopen(coinsToOpen);
        }

        String uri = "wss://open-api-ws.bingx.com/market";

        int whichSubscriptionStringNow = 0;
        int countOfSubscriptionStrings = bingxSubscriptionString.size();
        boolean isCoinsLeft = true;

        while (isCoinsLeft) {
            WebSocketClient webSocket = new WebSocketClient(uri, Exchanges.BINGX, this);
            webSockets.add(webSocket);

            for (int i = 0; i < 10; i++){
                System.out.println("Отправил подписку: " + bingxSubscriptionString.get(whichSubscriptionStringNow));
                webSocket.sendMessage(bingxSubscriptionString.get(whichSubscriptionStringNow));
                whichSubscriptionStringNow++;
                if (whichSubscriptionStringNow == countOfSubscriptionStrings) {
                    isCoinsLeft = false;
                    break;
                }
            }
        }

    }



    private void startKucoinWebSockets(ArrayList<String> coinsToOpen){

        ArrayList<String> kucoinSubscriptionString = null;
        if (coinsToOpen == null){
            kucoinSubscriptionString = KucoinSubscriptionString.getSubscription();
        }
        if (coinsToOpen != null) {
            kucoinSubscriptionString = KucoinSubscriptionString.getSubscriptionToReopen(coinsToOpen);
        }


        int whichSubscriptionStringNow = 0;
        int countOfSubscriptionStrings = kucoinSubscriptionString.size();
        boolean isCoinsLeft = true;

        while (isCoinsLeft) {
            String token;
            try {
                token = ApiForWebsocketConn.sendPostRequest();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String uri = "wss://ws-api-spot.kucoin.com/?token=" + token;

            WebSocketClient webSocket = new WebSocketClient(uri, Exchanges.KUCOIN, this);
            webSockets.add(webSocket);

            for (int i = 0; i < 10; i++){
                System.out.println("Отправил подписку: " + kucoinSubscriptionString.get(whichSubscriptionStringNow));
                webSocket.sendMessage(kucoinSubscriptionString.get(whichSubscriptionStringNow));
                whichSubscriptionStringNow++;
                if (whichSubscriptionStringNow == countOfSubscriptionStrings) {
                    isCoinsLeft = false;
                    break;
                }
            }
        }
    }





    public void initiateSendingPingPongMessages(){
        for (WebSocketClient webSocket : webSockets) {
            webSocket.doPingPong();
        }
    }

    public ArrayList<WebSocketClient> getWebSockets() {
        return webSockets;
    }

    public void reopenClosedWebsocket(WebSocketClient closedWebsocket) {
        Exchanges exchange = closedWebsocket.getExchange();
        HashMap<String, Double> coinsFromWebSocket = closedWebsocket.getCoinsFromWebSocket();
        ArrayList<String> coins = new ArrayList<String>();
        System.out.println("Какие монеты добавил в новый сокет:");
        for (Map.Entry<String, Double> entry : coinsFromWebSocket.entrySet()){
            coins.add(entry.getKey());
            System.out.println(entry.getKey());
        }

        switch (exchange){
            case BYBIT: {
                startBybitWebSockets(coins);
                break;
            }
            case BINANCE: {
                startBinanceWebSocket(coins);
                break;
            }
            case BINGX: {
                startBingxWebSocket(coins);
                break;
            }
            case KUCOIN: {
                startKucoinWebSockets(coins);
                break;
            }
        }
        webSockets.remove(closedWebsocket);

    }



}
