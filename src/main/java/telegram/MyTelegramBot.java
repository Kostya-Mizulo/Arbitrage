package telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import session.Session;
import session.SessionStatus;
import web_socket.WebSocketClient;

import javax.websocket.CloseReason;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class MyTelegramBot extends TelegramLongPollingBot {
    private static MyTelegramBot bot;
    private String botUsername;
    private String botToken;
    private long chatId;
    private Session session = new Session();

    public MyTelegramBot(){
        bot = this;
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(fileInputStream);
            botUsername = properties.getProperty("botUsername");
            botToken = properties.getProperty("botToken");
            chatId = Long.parseLong(properties.getProperty("chatId"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setCommands();

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(() -> {
                    sendPing();
                }, 1, 30, TimeUnit.MINUTES);
    }

    @Override
    public String getBotUsername(){
        return botUsername;
    }

    @Override
    public String getBotToken(){
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            //Получаем обновление из телеграмма (сообщение)
            String messageText = update.getMessage().getText();
            if (chatId == 0) {
                chatId = update.getMessage().getChatId();
            }


            //Обработка команды /start
            if (messageText.equals("/start_arbitrage")
                    && (session.getSessionStatus() == SessionStatus.INITIAL)) {
                handleStartCommand();
            } else
            if (messageText.equals("/start_pump")
                    && (session.getSessionStatus() == SessionStatus.INITIAL)){
                handleStartPumpCommand();
            }
            else {
                switch (session.getSessionStatus()) {
                    case WAITING_FOR_INITIAL_DEPOSIT_INPUT: {
                        handleGetInitialDeposit(messageText);
                        break;
                    }
                    default:
                        sendMessageToChat("Я пока в душе не ебу че с этим делать");
                        WebSocketClient wsc = session.getWebSocketsList().getWebSockets().get(0);
                        wsc.onClose(wsc.userSession,
                                new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Gy"));
                }
            }
        }
    }

    public void sendMessageToChat(String text){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setCommands(){
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start_arbitrage", "Запустить внешнебиржевой арбитраж"));
        commands.add(new BotCommand("/start_pump", "Запустить сигналку пампа"));

        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(),null));
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void handleStartCommand(){
        session.setSessionStatus(SessionStatus.WAITING_FOR_INITIAL_DEPOSIT_INPUT);
        sendMessageToChat("Перед запуском сессии введи размер депозита целым числом в USDT");

    }

    private void handleStartPumpCommand(){
        session.setSessionStatus(SessionStatus.PUMP_FINDER_IN_PROGRESS);
        sendMessageToChat("Начинаем искать памп");

    }

    private void handleGetInitialDeposit(String text){
        if (isNumeric(text)){
            session.setDeposit(Integer.parseInt(text));
            session.setSessionStatus(SessionStatus.HAS_INITIAL_DEPOSIT_VALUE);
        } else {
            sendMessageToChat("Число, блять, введи, заебал!");
        }
    }

    public static MyTelegramBot getBot(){
        return bot;
    }

    private void sendPing() {
        sendMessageToChat("Pong!");
    }
}