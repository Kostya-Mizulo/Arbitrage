package coins;

import exchanges.Exchanges;

public class Spread {
    private Exchanges exchangeWithMinPrice;
    private Exchanges exchangeWithMaxPrice;
    private Coin coinOfSpread;
    private double minPrice;
    private double maxPrice;
    private double spreadPercent;

    public Spread(Exchanges exchangeWithMinPrice, Exchanges exchangeWithMaxPrice, Coin coinOfSpread, double minPrice, double maxPrice, double spreadPercent) {
        this.exchangeWithMinPrice = exchangeWithMinPrice;
        this.exchangeWithMaxPrice = exchangeWithMaxPrice;
        this.coinOfSpread = coinOfSpread;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.spreadPercent = spreadPercent;
    }

    public Exchanges getExchangeWithMinPrice() {
        return exchangeWithMinPrice;
    }

    public void setExchangeWithMinPrice(Exchanges exchangeWithMinPrice) {
        this.exchangeWithMinPrice = exchangeWithMinPrice;
    }

    public Exchanges getExchangeWithMaxPrice() {
        return exchangeWithMaxPrice;
    }

    public void setExchangeWithMaxPrice(Exchanges exchangeWithMaxPrice) {
        this.exchangeWithMaxPrice = exchangeWithMaxPrice;
    }

    public Coin getCoinOfSpread() {
        return coinOfSpread;
    }

    public void setCoinOfSpread(Coin coinOfSpread) {
        this.coinOfSpread = coinOfSpread;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getSpreadPercent() {
        return spreadPercent;
    }

    public void setSpreadPercent(double spreadPercent) {
        this.spreadPercent = spreadPercent;
    }


}
