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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        setCommands();
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
            if (messageText.equals("/start")
                    && (session.getSessionStatus() == SessionStatus.INITIAL)) {
                handleStartCommand();
            } else {
                switch (session.getSessionStatus()) {
                    case WAITING_FOR_INITIAL_DEPOSIT_INPUT: {
                        handleGetInitialDeposit(messageText);
                        break;
                    }
                    default:
                        sendMessageToChat("Я пока в душе не ебу че с этим делать");
                        sendMessageToChat("Значение депозита: " + session.getDeposit());
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
        commands.add(new BotCommand("/start", "Запустить внешнебиржевой арбитраж"));

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

    private void handleGetInitialDeposit(String text){
        if (isNumeric(text)){
            session.setDeposit(Integer.parseInt(text));
            session.setSessionStatus(SessionStatus.HAS_INITIAL_DEPOSIT_VALUE);
        } else {
            sendMessageToChat("Число, блять, введи, заебал!");
        }
    }
}