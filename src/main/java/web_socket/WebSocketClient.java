package web_socket;

import exchanges.Exchanges;
import exchanges.binance.BinanceOnMessageHandler;
import exchanges.bingx.BingxOnMessageHandler;
import exchanges.bybit.BybitOnMessageHandler;

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
    private Session userSession = null;
    private HashMap<String, Double> coinsFromWebSocket = new HashMap<>();
    private String pingpong;



    public WebSocketClient(String uri, Exchanges exchange) {
        System.out.println(uri);
        this.exchange = exchange;
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

            if ("Ping".equals(convertedMessage)) {
                sendMessage("Pong");
                System.out.println("Send Pong");
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
        }
    }


    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Session closed by: " + reason);
    }


    public void sendMessage(String message) {
        try {
            userSession.getBasicRemote().sendText(message);
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
}
