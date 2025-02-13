package exchanges.binance;

import coins.CoinsList;

import java.util.ArrayList;
import java.util.HashMap;

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



    public static ArrayList<String> getSubscriptionToReopen(ArrayList<String> coins) {
        ArrayList<String> subscriptionStringList = new ArrayList<>();
        boolean isCoinsLeft = true;
        int coinPosition = 0;
        int countOfCoins = coins.size();

        while (isCoinsLeft) {
            StringBuilder subscriptionString = new StringBuilder();

            for (int i = 0; i < 10; i++){
                subscriptionString.append(coins.get(coinPosition).toLowerCase())
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
