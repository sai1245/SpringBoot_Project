package io.endeavour.stocks.entity.stocks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "stock_fundamentals",schema = "endeavour")
public class StockFundamentals {
    @Column(name = "ticker_symbol")
    @Id
    private String tickerSymbol;
    @Column(name = "market_cap")
    private BigDecimal marketCap;
    @Column(name = "current_ratio")
    private BigDecimal currentRatio;

    @OneToOne
    @JoinColumn(name = "sector_id", referencedColumnName = "sector_id")
    private SectorLookup sectorLookup;

    @OneToOne
    @JoinColumn(name = "subsector_id", referencedColumnName = "subsector_id")
    private SubSectorLookup subSectorLookup;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "ticker_symbol")
    private StocksLookup stocksLookup;

//    @OneToOne
//    @JoinColumn(name = "ticker_symbol", referencedColumnName = "ticker_symbol")
//    @MapsId
//    public StocksLookup stocksLookup;

    public int getSectorId(){
        return sectorLookup.getSectorID();
    }

    public String getSectorName(){
        return sectorLookup.getSectorName();
    }

    public int getSubSectorId(){
        return subSectorLookup.getSubSectorID();
    }

    public String getSubSectorName(){
        return subSectorLookup.getSubSectorName();
    }
    public String getTickername(){
        return stocksLookup.getTickerName();
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

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

    @JsonIgnore
    public SectorLookup getSectorLookup() {
        return sectorLookup;
    }

    public void setSectorLookup(SectorLookup sectorLookup) {
        this.sectorLookup = sectorLookup;
    }

    @JsonIgnore
    public SubSectorLookup getSubSectorLookup() {
        return subSectorLookup;
    }

    public void setSubSectorLookup(SubSectorLookup subSectorLookup) {
        this.subSectorLookup = subSectorLookup;
    }


    @JsonIgnore
    public StocksLookup getStocksLookup() {
        return stocksLookup;
    }

    public void setStocksLookup(StocksLookup stocksLookup) {
        this.stocksLookup = stocksLookup;
    }
}