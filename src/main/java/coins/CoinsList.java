package coins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CoinsList {
    public static ArrayList<Coin> coinsList = new ArrayList<>();
    public static HashMap<String, Coin> coinsMap = new HashMap<>();

    public static void addCoinsToCoinsList() {
        String filePath = "src/main/resources/coins.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                coinsList.add(new Coin(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Coin coin : coinsList) {
            coinsMap.put(coin.getCoin(), coin);
        }
    }


    public static String printAllCoins(){
        StringBuilder ms = new StringBuilder();
        for (Map.Entry<String, Coin> entry : coinsMap.entrySet()){
            ms.append(entry.getKey()).append(":\n").append("Bybit: ").append(entry.getValue().getBybitPrice())
                    .append("\n").append("Binance: ").append(entry.getValue().getBinancePrice())
                    .append("\nSpread: ").append(entry.getValue().getSpreadPercent())
                    .append("\n\n");
        }
        return ms.toString();
    }

    public static Spread findMaxSpreadWithExpectedPercentage(double expectedPercentage){

        ArrayList<Spread> spreadList = new ArrayList<>();

        for (Coin coin : coinsList){
            coin.setMinMaxValue();

            if (coin.getSpreadPercent() >= expectedPercentage) {
                spreadList.add(new Spread(coin.getExchangeWithMinPrice(),
                        coin.getExchangeWithMaxPrice(),
                        coin,
                        coin.getMinPrice(),
                        coin.getMaxPrice(),
                        coin.getSpreadPercent()));
            }
        }

        if (spreadList.isEmpty()) return null;
        Optional<Spread> maxSpread = spreadList.stream().max(Comparator.comparingDouble(Spread::getSpreadPercent));

        return maxSpread.get();

    }



}
