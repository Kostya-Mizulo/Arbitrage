package session;

import coins.CoinsList;
import coins.Spread;
import telegram.MyTelegramBot;
import telegram.TelegramMessagesBuilder;
import web_socket.PriceFromWebSocketListExtractor;
import web_socket.WebSocketsList;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Session {

    //  public Session session;
    private int deposit;
    private SessionStatus sessionStatus = SessionStatus.INITIAL;
    private WebSocketsList webSocketsList = new WebSocketsList();
    private ScheduledExecutorService scheduler;
    private boolean hasCoinWithExpectedSpread;
    private double expectedSpread = 0.0;



    public Session(){
        sessionStatus = SessionStatus.INITIAL;
    }








    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;

        sessionStatusObserver();
        }




    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }


    private void sessionStatusObserver() {
        switch (sessionStatus){
            case HAS_INITIAL_DEPOSIT_VALUE: {
                setSessionStatus(SessionStatus.WAITING_TO_DOWNLOAD_COINS_LIST);
                break;
            }
            case WAITING_TO_DOWNLOAD_COINS_LIST: {
                loadCoinsList();
                break;
            }
            case COINS_LIST_DOWNLOADED: {
                setSessionStatus(SessionStatus.WAITING_FOR_WEB_SOCKETS_CONNECTION);
                break;
            }
            case WAITING_FOR_WEB_SOCKETS_CONNECTION: {
                startWebSocket();
                break;
            }
            case CONNECTED_TO_WEB_SOCKETS: {
                setSessionStatus(SessionStatus.WAITING_FOR_SPREAD_SIGNAL);
                break;
            }
            case WAITING_FOR_SPREAD_SIGNAL: {
                waitForSpreadSignal();
                break;
            }
        }
    }



    private void loadCoinsList(){
        CoinsList.addCoinsToCoinsList();
        setSessionStatus(SessionStatus.COINS_LIST_DOWNLOADED);
    }
    private void startWebSocket(){
        webSocketsList.startWebSockets();
        setSessionStatus(SessionStatus.CONNECTED_TO_WEB_SOCKETS);
    }

    private void waitForSpreadSignal(){
        scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    PriceFromWebSocketListExtractor.extractPriceFromWebSocketList(webSocketsList);

                    Thread.sleep(5);
                    Spread spread = CoinsList.findMaxSpreadWithExpectedPercentage(expectedSpread);
                    String message = TelegramMessagesBuilder.createMessageAboutSpreadAsSignalForTelegram(spread);
                    if (message != null) MyTelegramBot.getBot().sendMessageToChat(message);
                    webSocketsList.initiateSendingPingPongMessages();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scheduler.scheduleWithFixedDelay(task, 5, 10, TimeUnit.SECONDS);
    }






    public boolean isHasCoinWithExpectedSpread() {
        return hasCoinWithExpectedSpread;
    }

    public void setHasCoinWithExpectedSpread(boolean hasCoinWithExpectedSpread) {
        this.hasCoinWithExpectedSpread = hasCoinWithExpectedSpread;
    }

    public double getExpectedSpread() {
        return expectedSpread;
    }

    public void setExpectedSpread(double expectedSpread) {
        this.expectedSpread = expectedSpread;
    }
}
