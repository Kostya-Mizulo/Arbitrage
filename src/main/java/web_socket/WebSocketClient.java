package web_socket;

import bybit.BybitPriceExtractor;

import javax.websocket.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ClientEndpoint
public class WebSocketClient {

    private String exchange;
    private Session userSession = null;
    private HashMap<String, Double> coinsFromWebSocket = new HashMap<>();



    public WebSocketClient(String uri, String exchange) {
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
        System.out.println(message);
        if (BybitPriceExtractor.extractPriceFromJson(message) != null) {
            Map.Entry<String, Double> entry = BybitPriceExtractor.extractPriceFromJson(message);
            coinsFromWebSocket.put(entry.getKey(), entry.getValue());
        }
    }


    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Session closed by: " + reason);
    }


    public void sendMessage(String message) {
        System.out.println("Отправил сообщение с подпиской:\n");
        System.out.println(message);
        try {
            userSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            System.out.println("Какая-тоо хуйня с сессией");
            e.printStackTrace();
        }
    }

    public HashMap<String, Double> getCoinsFromWebSocket() {
        return coinsFromWebSocket;
    }
    public String getExchange() {
        return exchange;
    }
}
