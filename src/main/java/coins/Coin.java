package coins;

public class Coin {
    private final String coin;
    private double bybitPrice;
    private double binancePrice;





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
}
