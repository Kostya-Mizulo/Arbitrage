package coins;

import exchanges.Exchanges;

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


    public static ArrayList<String[]> create_list_of_coins_for_pump(){
        ArrayList<String[]> coinsList = new ArrayList<>();

        String filePathBybit = "src/main/resources/bybitCoins.txt";
        String filePathBingx = "src/main/resources/bingxCoins.txt";
        String filePathBinance = "src/main/resources/binanceCoins.txt";



        try (BufferedReader br = new BufferedReader(new FileReader(filePathBybit))) {
            String line;

            while ((line = br.readLine()) != null) {
                coinsList.add(new String[]{line, "Bybit"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        try (BufferedReader br = new BufferedReader(new FileReader(filePathBingx))) {
            String line;

            while ((line = br.readLine()) != null) {
                coinsList.add(new String[]{line, "Bingx"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        try (BufferedReader br = new BufferedReader(new FileReader(filePathBinance))) {
            String line;

            while ((line = br.readLine()) != null) {
                coinsList.add(new String[]{line, "Binance"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coinsList;
    }

}