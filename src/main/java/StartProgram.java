import exchanges.bingx.BingxOnMessageHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.MyTelegramBot;

public class StartProgram {
    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyTelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("Перезапустить!");
        }
        System.out.println("Перезапустить!");
    }
}
