package bybit;

import coins.Coin;
import coins.CoinsList;

import java.util.ArrayList;

public class BybitSubscriptionString {
    private static String getSubscriptionStart(){
        String subscriptionStringStart = "{\"op\": \"subscribe\", \"args\":[";


        return subscriptionStringStart;
    }

    private static String getSubscriptionEnd() {
        return "]}";
    }


    public static ArrayList<String> getSubscription() {
        ArrayList<String> subscriptionStringList = new ArrayList<>();
        boolean isCoinsLeft = true;
        int coinPosition = 0;
        int countOfCoins = CoinsList.coinsList.size();

        while (isCoinsLeft) {
            StringBuilder subscriptionString = new StringBuilder();
            subscriptionString.append(getSubscriptionStart());

            for (int i = 0; i < 10; i++){
                subscriptionString.append("\"tickers.").append(CoinsList.coinsList.get(coinPosition).getCoin()).append("USDT\"");
                coinPosition++;
                if (i<9 && coinPosition < countOfCoins) {
                    subscriptionString.append(",");
                }

                if (coinPosition == countOfCoins) {
                    isCoinsLeft = false;
                    break;
                }
            }
            subscriptionString.append(getSubscriptionEnd());
            subscriptionStringList.add(String.valueOf(subscriptionString));
        }


        return subscriptionStringList;
    }
}