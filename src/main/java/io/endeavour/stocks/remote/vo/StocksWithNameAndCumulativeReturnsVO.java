package io.endeavour.stocks.remote.vo;

import java.math.BigDecimal;

public class StocksWithNameAndCumulativeReturnsVO {

    private String tickerSymbol;
    private String tickerName;
    private BigDecimal cumulativeReturns;

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public BigDecimal getCumulativeReturns() {
        return cumulativeReturns;
    }

    public void setCumulativeReturns(BigDecimal cumulativeReturns) {
        this.cumulativeReturns = cumulativeReturns;
    }
}
