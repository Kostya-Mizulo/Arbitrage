package exchanges.bingx;

import coins.CoinsList;

import java.util.ArrayList;

public class BingxSubscriptionString {

    private static String getSubscriptionStart(){
        String subscriptionStringStart = "{\"reqType\": \"sub\", \"dataType\": \"";


        return subscriptionStringStart;
    }

    private static String getSubscriptionEnd() {
        return "-USDT@markPrice\"}";
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
}
