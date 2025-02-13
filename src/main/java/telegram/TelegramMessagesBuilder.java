package telegram;

import coins.Spread;

public class TelegramMessagesBuilder {
    public static String createMessageAboutSpreadAsSignalForTelegram(Spread spread) {
        if (spread == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append("Найден спред!\n").append(spread.getCoinOfSpread().getCoin()).append("\n")
                .append(spread.getExchangeWithMinPrice()).append(" -> ")
                .append(spread.getExchangeWithMaxPrice())
                .append("\nMin Price: ").append(spread.getMinPrice())
                .append("\nMax Price: ").append(spread.getMaxPrice())
                .append("\nСпред: ").append(spread.getSpreadPercent());

        return sb.toString();
    }
}
