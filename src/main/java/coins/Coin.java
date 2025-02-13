package coins;

import exchanges.Exchanges;


import java.util.Map;

public class Coin {
    private final String coin;
    private double bybitPrice;
    private double binancePrice;
    private double bingxPrice;
    private double kucoinPrice;
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

    public double getKucoinPrice() {
        return kucoinPrice;
    }

    public void setKucoinPrice(double kucoinPrice) {
        this.kucoinPrice = kucoinPrice;
    }



    public void setMinMaxValue(){
        exchangeWithMinPrice = null;
        exchangeWithMaxPrice = null;
        minPrice = 0.0;
        maxPrice = 0.0;


        double[] prices = {bybitPrice, binancePrice, bingxPrice, kucoinPrice};
        Exchanges[] exchange = {Exchanges.BYBIT, Exchanges.BINANCE, Exchanges.BINGX, Exchanges.KUCOIN};

        double minValue_local = Double.MAX_VALUE;
        double maxValue_local = Double.MIN_VALUE;
        Exchanges exchangeWithMin_local = null;
        Exchanges exchangeWithMax_local = null;

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


        exchangeWithMinPrice = (minValue_local > 0.0 && minValue_local < Double.MAX_VALUE) ? exchangeWithMin_local : null;
        exchangeWithMaxPrice = maxValue_local > 0.0 ? exchangeWithMax_local : null;
        minPrice = minValue_local;
        maxPrice = maxValue_local;

        if (exchangeWithMinPrice != null && exchangeWithMaxPrice != null) spreadPercent = ((maxPrice - minPrice)/minPrice)*100;
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
