package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TradingHistoryForStocksVO {
    private BigDecimal marketCap;
    private BigDecimal currentRatio;
    private String tickerSymbol;
    private String tickerName;
    private LocalDate tradingDate;
    private BigDecimal closePrice;
    private Long volume;

    public TradingHistoryForStocksVO(BigDecimal marketCap,
                                     BigDecimal currentRatio,
                                     String tickerSymbol,
                                     String tickerName,
                                     LocalDate tradingDate,
                                     BigDecimal closePrice,
                                     Long volume) {
        this.marketCap = marketCap;
        this.currentRatio = currentRatio;
        this.tickerSymbol = tickerSymbol;
        this.tickerName=tickerName;
        this.tradingDate = tradingDate;
        this.closePrice = closePrice;
        this.volume = volume;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public Long getVolume() {
        return volume;
    }

    public String getTickerName() {
        return tickerName;
    }

    @Override
    public String toString() {
        return "TradingHistoryForStocksVO{" +
                "marketCap=" + marketCap +
                ", currentRatio=" + currentRatio +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", tradingDate=" + tradingDate +
                ", closePrice=" + closePrice +
                ", volume=" + volume +
                '}';
    }
}
