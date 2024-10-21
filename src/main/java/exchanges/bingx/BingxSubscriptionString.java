package exchanges.bingx;

import coins.CoinsList;

import java.util.ArrayList;
import java.util.UUID;

public class BingxSubscriptionString {

    private static String getSubscriptionStart(){
        String uuid = UUID.randomUUID().toString();
        String subscriptionStringStart = "{\"id\": \"" + uuid+ "\",\"reqType\": \"sub\", \"dataType\": \"";


        return subscriptionStringStart;
    }

    private static String getSubscriptionEnd() {
        return "-USDT@kline_1min\"}";
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
