package coins;

import exchanges.Exchanges;


import java.util.Map;

public class Coin {
    private final String coin;
    private double bybitPrice;
    private double binancePrice;



    private double spreadPercent;





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


    public void setMinMaxValue(){
        double[] prices = {bybitPrice, binancePrice};
        Exchanges[] exchange = {Exchanges.BYBIT, Exchanges.BINANCE};

        double minValue = prices[0];
        double maxValue = prices[0];
        Exchanges exchangeWithMin = null;
        Exchanges exchangeWithMax = null;

        for (int i = 0; i < prices.length; i++){
            if (prices[i] == 0.0) continue;

            if (prices[i] < minValue){
                minValue = prices[i];
                exchangeWithMin = exchange[i];
            }
            if (prices[i] > maxValue){
                maxValue = prices[i];
                exchangeWithMax = exchange[i];
            }
        }

        spreadPercent = ((maxValue - minValue)/minValue)*100;

    }


    public double getSpreadPercent() {
        return spreadPercent;
    }

}
