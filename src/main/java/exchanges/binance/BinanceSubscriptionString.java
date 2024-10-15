package exchanges.binance;

import coins.CoinsList;

import java.util.ArrayList;

public class BinanceSubscriptionString {



    public static ArrayList<String> getSubscription() {
        ArrayList<String> subscriptionStringList = new ArrayList<>();
        boolean isCoinsLeft = true;
        int coinPosition = 0;
        int countOfCoins = CoinsList.coinsList.size();

        while (isCoinsLeft) {
            StringBuilder subscriptionString = new StringBuilder();

            for (int i = 0; i < 10; i++){
                subscriptionString.append(CoinsList.coinsList.get(coinPosition).getCoin().toLowerCase())
                        .append("usdt").append("@kline_1s");

                coinPosition++;
                if (i<9 && coinPosition < countOfCoins) {
                    subscriptionString.append("/");
                }

                if (coinPosition == countOfCoins) {
                    isCoinsLeft = false;
                    break;
                }
            }
            subscriptionStringList.add(String.valueOf(subscriptionString));
        }


        return subscriptionStringList;
    }
}
