package io.endeavour.stocks.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.endeavour.stocks.entity.stocks.SectorLookup;

import java.math.BigDecimal;
import java.util.List;

public class TopThreeStockVO {


    private String tickerSymbol;
    private String tickerName;
    private BigDecimal marketCap;
    private int sectorId;

    public TopThreeStockVO(String tickerSymbol, String tickerName, BigDecimal marketCap,int sectorId) {
        this.tickerSymbol = tickerSymbol;
        this.tickerName = tickerName;
        this.marketCap = marketCap;
        this.sectorId=sectorId;
    }

    @JsonIgnore
    public int getSectorId() {
        return sectorId;
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

