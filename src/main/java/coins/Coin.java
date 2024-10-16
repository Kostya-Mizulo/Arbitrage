package coins;

import exchanges.Exchanges;


import java.util.Map;

public class Coin {
    private final String coin;
    private double bybitPrice;
    private double binancePrice;
    private double bingxPrice;
    private double spreadPercent;
    private Exchanges exchangeWithMinPrice;
    private Exchanges exchangeWithMaxPrice;
    private double minPrice;
    private double maxPrice;

    public Coin(String coin) {
        this.coin = coin;
    }




    public String getCoin() {
        return coin;
    }

    public double getBybitPrice() {
        return bybitPrice;
    }

    public void setBybitPrice(double bybitPrice) {
        this.bybitPrice = bybitPrice;
    }

    public double getBinancePrice() {
        return binancePrice;
    }

    public void setBinancePrice(double binancePrice) {
        this.binancePrice = binancePrice;
    }
    public double getBingxPrice() {
        return bingxPrice;
    }

    public void setBingxPrice(double bingxPrice) {
        this.bingxPrice = bingxPrice;
    }


    public void setMinMaxValue(){
        exchangeWithMinPrice = null;
        exchangeWithMaxPrice = null;
        minPrice = 0.0;
        maxPrice = 0.0;


        double[] prices = {bybitPrice, binancePrice, bingxPrice};
        Exchanges[] exchange = {Exchanges.BYBIT, Exchanges.BINANCE, Exchanges.BINGX};

        double minValue_local = prices[0];
        double maxValue_local = prices[0];
        Exchanges exchangeWithMin_local = exchange[0];
        Exchanges exchangeWithMax_local = exchange[0];

        for (int i = 0; i < prices.length; i++){
            if (prices[i] == 0.0) continue;

            if (prices[i] < minValue_local){
                minValue_local = prices[i];
                exchangeWithMin_local = exchange[i];
            }
            if (prices[i] > maxValue_local){
                maxValue_local = prices[i];
                exchangeWithMax_local = exchange[i];
            }
        }

        spreadPercent = ((maxValue_local - minValue_local)/minValue_local)*100;
        exchangeWithMinPrice = exchangeWithMin_local;
        exchangeWithMaxPrice = exchangeWithMax_local;
        minPrice = minValue_local;
        maxPrice = maxValue_local;

    }




    public double getSpreadPercent() {
        return spreadPercent;
    }
    public Exchanges getExchangeWithMinPrice() {
        return exchangeWithMinPrice;
    }

    public Exchanges getExchangeWithMaxPrice() {
        return exchangeWithMaxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

}
