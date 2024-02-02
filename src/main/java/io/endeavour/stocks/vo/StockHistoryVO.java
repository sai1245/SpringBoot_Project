package io.endeavour.stocks.vo;

import java.math.BigDecimal;
import java.util.List;

public class StockHistoryVO {
    private BigDecimal marketCap;
    private BigDecimal currentRatio;
   private List<TradingHistoryVO> tradinghistory;

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    public List<TradingHistoryVO> getTradinghistory() {
        return tradinghistory;
    }

    public void setTradinghistory(List<TradingHistoryVO> tradinghistory) {
        this.tradinghistory = tradinghistory;
    }
}
