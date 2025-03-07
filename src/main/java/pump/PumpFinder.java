package pump;

import exchanges.binance.BinancePumpParser;
import exchanges.bingx.BingxPumpParser;
import exchanges.bybit.BybitPumpParser;
import io.restassured.response.Response;
import rest_api.BinanceApi;
import rest_api.BingxApi;
import rest_api.BybitApi;
import telegram.MyTelegramBot;

import java.util.ArrayList;

public class PumpFinder {
    public static void findPump(String symbol, String exchange){
        switch (exchange){
            case "Bybit": {
                String symbolFull = symbol + "USDT";
                BybitApi bybitApi = new BybitApi();
                Response response = bybitApi.getKlinesForSymbol(symbolFull);
                ArrayList<Double> priceList = BybitPumpParser.parsePump(response);
                calcPumpAndSendIfExists(symbol, priceList, "Bybit");
                break;
            }
            case "Bingx": {
                String symbolFull = symbol + "-USDT";
                BingxApi bingxApi = new BingxApi();
                Response response = bingxApi.getKlinesForSymbol(symbolFull);
                ArrayList<Double> priceList = BingxPumpParser.parsePump(response);
                calcPumpAndSendIfExists(symbol, priceList, "Bingx");
                break;
            }
            case "Binance": {
                String symbolFull = symbol + "USDT";
                BinanceApi binanceApi = new BinanceApi();
                Response response = binanceApi.getKlinesForSymbol(symbolFull);
                ArrayList<Double> priceList = BinancePumpParser.parsePump(response);
                calcPumpAndSendIfExists(symbol, priceList, "Binance");
            }
        }
    }

    public static void calcPumpAndSendIfExists(String coin, ArrayList<Double> priceList, String exchange){
        System.out.println(coin);
        double currentPrice = priceList.get(0);

        double maxSpred = 0.0;

        for (int i = 1; i < priceList.size(); i++){
            double spred = ((currentPrice - priceList.get(i)) / priceList.get(i)) * 100;
            if (spred > 0 && spred > maxSpred) maxSpred = spred;
        }

        if (maxSpred >= 10) {
            String message = "На бирже " + exchange + " памп монеты " + coin + " на " + maxSpred + "%";
            MyTelegramBot.getBot().sendMessageToChat(message);
        }
    }
}