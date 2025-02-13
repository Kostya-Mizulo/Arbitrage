package exchanges.kucoin;

import coins.CoinsList;

import java.util.ArrayList;

public class KucoinSubscriptionString {
    private static String getSubscriptionStart(){
        String subscriptionStringStart = "{\"type\": \"subscribe\", \"topic\":\"/market/ticker:";


        return subscriptionStringStart;
    }

    private static String getSubscriptionEnd() {
        return "-USDT\", \"response\": true}";
    }


    public static ArrayList<String> getSubscription() {
        ArrayList<String> subscriptionStringList = new ArrayList<>();
        boolean isCoinsLeft = true;
        int coinPosition = 0;
        int countOfCoins = CoinsList.coinsList.size();

        while (isCoinsLeft) {
            StringBuilder subscriptionString = new StringBuilder();


            subscriptionString.append(getSubscriptionStart())
                    .append(CoinsList.coinsList.get(coinPosition).getCoin())
                    .append(getSubscriptionEnd());

            coinPosition++;
            if (coinPosition == countOfCoins) isCoinsLeft = false;


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


            subscriptionString.append(getSubscriptionStart())
                    .append(coins.get(coinPosition))
                    .append(getSubscriptionEnd());

            coinPosition++;
            if (coinPosition == countOfCoins) isCoinsLeft = false;


            subscriptionStringList.add(String.valueOf(subscriptionString));
        }


        return subscriptionStringList;
    }

}
