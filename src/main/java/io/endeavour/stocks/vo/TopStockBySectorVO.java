package io.endeavour.stocks.vo;

import java.math.BigDecimal;

public class TopStockBySectorVO {

    private int sectorId;
    private String sectorName;
    private String tickerSymbol;
    private String tickerName;
    private BigDecimal marketCap;

    public TopStockBySectorVO(int sectorId, String sectorName, String tickerSymbol, String tickerName, BigDecimal marketCap) {
        this.sectorId = sectorId;
        this.sectorName = sectorName;
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
    }

    public int getSectorId() {
        return sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }
}
