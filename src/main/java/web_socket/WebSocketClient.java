package web_socket;

import coins.CoinsList;
import exchanges.Exchanges;
import exchanges.binance.BinanceOnMessageHandler;
import exchanges.bingx.BingxOnMessageHandler;
import exchanges.bybit.BybitOnMessageHandler;
import exchanges.kucoin.KucoinOnMessageHandler;
import telegram.MyTelegramBot;

import javax.websocket.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@ClientEndpoint
public class WebSocketClient {

    private Exchanges exchange;
    public Session userSession = null;
    private HashMap<String, Double> coinsFromWebSocket = new HashMap<>();
    private String pingpong;
    private WebSocketsList webSocketsList;



    public WebSocketClient(String uri, Exchanges exchange, WebSocketsList webSocketsList) {
        System.out.println(uri);
        this.exchange = exchange;
        this.webSocketsList = webSocketsList;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, URI.create(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("CONNECTED!");
        this.userSession = userSession;

    }


    @OnMessage
    public void onMessage(String message) {
        redirectMessage(message);
    }



    @OnMessage
    public void onMessage(ByteBuffer message) {
        try {
            byte[] bytes = new byte[message.remaining()];
            message.get(bytes);

            String convertedMessage = decompressGzip(bytes);


            if (convertedMessage.contains("ping")) {
                String pong = BingxOnMessageHandler.createPongString(convertedMessage);
                sendMessage(pong);
                return;
            } else {
                redirectMessage(convertedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectMessage(String message) {


        switch (exchange){
            case BYBIT: {
                if (BybitOnMessageHandler.handleMessageFromServer(message) != null) {
                    Map.Entry<String, Double> entry = BybitOnMessageHandler.handleMessageFromServer(message);
                    coinsFromWebSocket.put(entry.getKey(), entry.getValue());
                }
                break;
            }

            case BINANCE: {
                if (BinanceOnMessageHandler.handleMessageFromServer(message) != null) {
                    Map.Entry<String, Double> entry = BinanceOnMessageHandler.handleMessageFromServer(message);
                    coinsFromWebSocket.put(entry.getKey(), entry.getValue());
                }
                break;
            }
            case BINGX: {
                if (BingxOnMessageHandler.handleMessageFromServer(message) != null) {
                    Map.Entry<String, Double> entry = BingxOnMessageHandler.handleMessageFromServer(message);
                    coinsFromWebSocket.put(entry.getKey(), entry.getValue());
                }
                break;
            }
            case KUCOIN: {
                if (KucoinOnMessageHandler.handleMessageFromServer(message) != null) {
                    Map.Entry<String, Double> entry = KucoinOnMessageHandler.handleMessageFromServer(message);
                    coinsFromWebSocket.put(entry.getKey(), entry.getValue());
                }
                break;
            }
        }
    }


    @OnClose
    public void onClose(Session userSession, CloseReason reason) {

        System.out.println("Session closed by: " + reason);
        MyTelegramBot.getBot().sendMessageToChat("Session closed: " + this.exchange);



        System.out.println("Какие монеты в сокете:");
        for (Map.Entry<String, Double> entry : coinsFromWebSocket.entrySet()){
            System.out.println(entry.getKey());
        }



        webSocketsList.reopenClosedWebsocket(this);
    }


    public void sendMessage(String message) {
        try {
            userSession.getBasicRemote().sendText(message);
            System.out.println("Отправил в сокет: "+message);
        } catch (Exception e) {
            System.out.println("Какая-тоо хуйня с сессией");
            e.printStackTrace();
        }
    }


    public void doPingPong(){
        switch (exchange){
            case BYBIT: {
                pingpong = "{\"op\": \"ping\"}";
                sendMessage(pingpong);
                break;
            }

            case BINANCE: {
//                if (pingpong == null) break;
//                sendMessage(pingpong);
//                pingpong = null;
                break;
            }
            case BINGX: {
                break;
            }
            case KUCOIN: {
                pingpong = "{\"type\": \"ping\"}";
                sendMessage(pingpong);
                break;
            }
        }
    }

    public HashMap<String, Double> getCoinsFromWebSocket() {
        return coinsFromWebSocket;
    }
    public Exchanges getExchange() {
        return exchange;
    }


    private String decompressGzip(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        }
    }

    private void deleteCoinPriceIfWebSocketClosed(){
        for (Map.Entry<String, Double> entry : coinsFromWebSocket.entrySet()){
            String coin = entry.getKey();
            switch (exchange){
                case BYBIT: {
                    CoinsList.coinsMap.get(coin).setBybitPrice(0.0);
                    break;
                }
                case BINANCE: {
                    CoinsList.coinsMap.get(coin).setBinancePrice(0.0);
                    break;
                }
                case BINGX: {
                    CoinsList.coinsMap.get(coin).setBingxPrice(0.0);
                    break;
                }
                case KUCOIN: {
                    CoinsList.coinsMap.get(coin).setKucoinPrice(0.0);
                    break;
                }
            }

        }
    }
}
